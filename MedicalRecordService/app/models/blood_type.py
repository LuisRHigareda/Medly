"""
blood_type.py
-------------
SQLAlchemy model for the blood_type table.
Acts as a lookup/enum table with the 8 standard blood types.
Rows are pre-seeded in the database via SQL INSERT statements.
"""

from sqlalchemy import Column, String
from sqlalchemy.dialects.mysql import TINYINT
from sqlalchemy.orm import relationship

from app.database.base import Base


class BloodType(Base):
    """Represents a blood type (e.g. O+, A-, AB+).

    This table is read-only from the application's perspective —
    values are seeded directly in the database.
    """

    __tablename__ = "blood_type"

    id = Column(TINYINT, primary_key=True, autoincrement=True)
    type_name = Column(String(5), nullable=False, unique=True)

    # One blood type can appear in many medical records
    medical_records = relationship("MedicalRecord", back_populates="blood_type")
