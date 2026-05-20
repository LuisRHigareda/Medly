"""
medical_procedure.py
--------------------
SQLAlchemy model for the medical_procedure table.
Records a clinical procedure performed on a patient as part of a
medical record (e.g. surgery, blood test, imaging).
"""

from sqlalchemy import Column, Integer, String, Date, Text, ForeignKey
from sqlalchemy.orm import relationship

from app.database.base import Base


class MedicalProcedure(Base):
    """Represents a medical procedure linked to a specific medical record."""

    __tablename__ = "medical_procedure"

    id = Column(Integer, primary_key=True, autoincrement=True)

    # FK to the parent medical record
    medical_record_id = Column(Integer, ForeignKey("medical_record.id", ondelete="CASCADE"), nullable=False)

    date = Column(Date, nullable=False)
    name = Column(String(255), nullable=False)
    notes = Column(Text, nullable=True)   # additional observations or results

    # --- Relationships ---
    medical_record = relationship("MedicalRecord", back_populates="procedures")
