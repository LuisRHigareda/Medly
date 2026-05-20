"""
prescribed_medication.py
------------------------
SQLAlchemy model for the prescribed_medication table.
Links a medication from the catalog to a specific medical record,
including dosage instructions and the treatment period.
"""

from sqlalchemy import Column, Integer, SmallInteger, Date, DECIMAL, ForeignKey
from sqlalchemy.orm import relationship

from app.database.base import Base


class PrescribedMedication(Base):
    """Represents a medication prescribed within a specific medical record."""

    __tablename__ = "prescribed_medication"

    id = Column(Integer, primary_key=True, autoincrement=True)

    # FK to the parent medical record
    medical_record_id = Column(Integer, ForeignKey("medical_record.id", ondelete="CASCADE"), nullable=False)

    # FK to the medication catalog
    medication_id = Column(Integer, ForeignKey("medication.id"), nullable=False)

    frequency = Column(SmallInteger, nullable=True)   # times per day
    dose = Column(DECIMAL(6, 2), nullable=True)                 # dose amount (e.g. 500 mg)
    start_date = Column(Date, nullable=False)
    end_date = Column(Date, nullable=True)                      # None if ongoing

    # --- Relationships ---
    medical_record = relationship("MedicalRecord", back_populates="prescribed_medications")
    medication = relationship("Medication", back_populates="prescriptions")
