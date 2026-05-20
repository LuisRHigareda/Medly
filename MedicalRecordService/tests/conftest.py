"""
conftest.py
-----------
Shared pytest fixtures for MedicalRecordService tests.

Sets up an in-memory SQLite database for testing so that the real
MySQL database (medical_record_db) is never touched during test runs.

Each test gets a fresh database with all tables created, ensuring
tests are fully isolated from each other.
"""

import pytest
from fastapi.testclient import TestClient
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker

from app.main import app
from app.database.base import Base
from app.database.session import get_db
from app.auth.jwt_bearer import verify_token

# Use a file-based SQLite database for tests so all connections share the same data.
# The file is created fresh for each test via setup/teardown.
TEST_DATABASE_URL = "sqlite:///./test_temp.db"


@pytest.fixture()
def client():
    """
    Provide a FastAPI TestClient backed by a temporary SQLite database.

    Creates all tables before the test and drops them after, so every
    test runs against a clean, isolated database.
    """
    # Create engine pointing to a temp file so all connections see the same data
    engine = create_engine(
        TEST_DATABASE_URL,
        connect_args={"check_same_thread": False},
    )

    # Create all tables from the SQLAlchemy models
    Base.metadata.create_all(bind=engine)

    TestingSessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

    def override_get_db():
        """Replace the real get_db dependency with one using the test database."""
        db = TestingSessionLocal()
        try:
            yield db
        finally:
            db.close()

    app.dependency_overrides[get_db] = override_get_db
    # Bypass JWT authentication in tests
    app.dependency_overrides[verify_token] = lambda: {"id": 1, "email": "test@medly.com", "role": "DOCTOR"}

    with TestClient(app) as test_client:
        yield test_client

    # Cleanup: remove overrides and drop all tables
    app.dependency_overrides.clear()
    Base.metadata.drop_all(bind=engine)
    engine.dispose()

    # Delete the temp database file
    import os
    if os.path.exists("test_temp.db"):
        os.remove("test_temp.db")


@pytest.fixture()
def db_session():
    """
    Provide a direct database session for seeding lookup data before a test.

    Depends on client so it uses the same engine and tables.
    """
    engine = create_engine(
        TEST_DATABASE_URL,
        connect_args={"check_same_thread": False},
    )
    TestingSessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)
    session = TestingSessionLocal()
    try:
        yield session
    finally:
        session.close()
        engine.dispose()
