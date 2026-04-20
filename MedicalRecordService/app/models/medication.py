"""
medication.py
-------------
SQLAlchemy model for the medication table.
This is a catalog of available medications. It is referenced by
prescribed_medication when a doctor prescribes a drug to a patient.
"""

from sqlalchemy import Column, Integer, String
from sqlalchemy.orm import relationship

from app.database.base import Base


class Medication(Base):
    """Represents a medication in the catalog (e.g. Ibuprofen, Amoxicillin)."""

    __tablename__ = "medication"

    id = Column(Integer, primary_key=True, autoincrement=True)
    name = Column(String(200), nullable=False, unique=True)

    # One medication can appear in many prescriptions
    prescriptions = relationship("PrescribedMedication", back_populates="medication")
