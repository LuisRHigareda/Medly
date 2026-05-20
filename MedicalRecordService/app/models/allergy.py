"""
allergy.py
----------
SQLAlchemy model for the allergy table.
An allergy belongs to a medical record and describes the allergen,
the patient's reaction, and the severity level.
"""

from sqlalchemy import Column, Integer, String, ForeignKey
from sqlalchemy.orm import relationship

from app.database.base import Base


class Allergy(Base):
    """Represents an allergy entry linked to a specific medical record."""

    __tablename__ = "allergy"

    id = Column(Integer, primary_key=True, autoincrement=True)

    # FK to the parent medical record
    medical_record_id = Column(Integer, ForeignKey("medical_record.id", ondelete="CASCADE"), nullable=False)

    allergen = Column(String(150), nullable=False)    # substance causing the allergy
    reaction = Column(String(255), nullable=True)     # observed reaction (e.g. hives, anaphylaxis)
    severity = Column(String(50), nullable=True)      # e.g. mild, moderate, severe

    # --- Relationships ---
    medical_record = relationship("MedicalRecord", back_populates="allergies")
