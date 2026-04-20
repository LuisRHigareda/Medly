"""
diagnosis_status.py
-------------------
SQLAlchemy model for the diagnosis_status table.
Acts as a lookup/enum table with the possible states of a diagnosis:
ACTIVE, RESOLVED, CHRONIC, IN_TREATMENT.
Rows are pre-seeded in the database via SQL INSERT statements.
"""

from sqlalchemy import Column, String
from sqlalchemy.dialects.mysql import TINYINT
from sqlalchemy.orm import relationship

from app.database.base import Base


class DiagnosisStatus(Base):
    """Represents the status of a diagnosis (e.g. ACTIVE, RESOLVED).

    This table is read-only from the application's perspective —
    values are seeded directly in the database.
    """

    __tablename__ = "diagnosis_status"

    id = Column(TINYINT, primary_key=True, autoincrement=True)
    status_name = Column(String(30), nullable=False, unique=True)

    # One status can be assigned to many diagnoses
    diagnoses = relationship("Diagnosis", back_populates="status")
