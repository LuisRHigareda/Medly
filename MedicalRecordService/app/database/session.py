"""
session.py
----------
Configures the SQLAlchemy database engine and session factory for
MedicalRecordService. Connection parameters are loaded from environment
variables defined in the .env file.
"""

import os
from dotenv import load_dotenv
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker

# Load environment variables from .env file
load_dotenv()

# Build the MySQL connection URL from environment variables
DATABASE_URL = (
    f"mysql+pymysql://{os.getenv('DB_USER')}:{os.getenv('DB_PASSWORD')}"
    f"@{os.getenv('DB_HOST')}:{os.getenv('DB_PORT')}/{os.getenv('DB_NAME')}"
)

# Create the SQLAlchemy engine (manages the connection pool)
engine = create_engine(DATABASE_URL)

# Session factory — sessions created here do not auto-commit or auto-flush
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)


def get_db():
    """
    FastAPI dependency that provides a database session for a single request.

    Yields a SQLAlchemy Session and ensures it is closed when the request
    finishes, even if an exception is raised.

    Usage:
        @app.get("/example")
        def example(db: Session = Depends(get_db)):
            ...
    """
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()
