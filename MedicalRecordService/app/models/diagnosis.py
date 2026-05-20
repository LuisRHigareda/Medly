"""
diagnosis.py
------------
SQLAlchemy model for the diagnosis table.
A diagnosis belongs to a medical record and has an optional status
(ACTIVE, RESOLVED, CHRONIC, IN_TREATMENT) from the diagnosis_status table.
"""

from sqlalchemy import Column, Integer, SmallInteger, String, DateTime, ForeignKey
from sqlalchemy.orm import relationship

from app.database.base import Base


class Diagnosis(Base):
    """Represents a medical diagnosis linked to a specific medical record."""

    __tablename__ = "diagnosis"

    id = Column(Integer, primary_key=True, autoincrement=True)

    # FK to the parent medical record
    medical_record_id = Column(Integer, ForeignKey("medical_record.id", ondelete="CASCADE"), nullable=False)

    name = Column(String(255), nullable=False)
    diagnosed_date = Column(DateTime, nullable=False)

    # FK to diagnosis_status lookup table (optional)
    status_id = Column(SmallInteger, ForeignKey("diagnosis_status.id"), nullable=True)

    # --- Relationships ---
    medical_record = relationship("MedicalRecord", back_populates="diagnoses")
    status = relationship("DiagnosisStatus", back_populates="diagnoses")
