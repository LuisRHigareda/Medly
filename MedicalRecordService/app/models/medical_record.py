"""
medical_record.py
-----------------
SQLAlchemy model for the medical_record table.
This is the core entity of MedicalRecordService — every piece of clinical
data (diagnoses, allergies, medications, procedures) links back to a record.

Note: patient_id is stored as a plain integer with no foreign key constraint
because the Patient entity belongs to UserService (a separate database).
"""

from sqlalchemy import Column, Integer, SmallInteger, Text, DECIMAL, DateTime, ForeignKey, func
from sqlalchemy.orm import relationship

from app.database.base import Base


class MedicalRecord(Base):
    """Represents a patient's medical record.

    Each record is identified by patient_id (an external reference to
    UserService) and serves as the parent for all clinical sub-entities.
    """

    __tablename__ = "medical_record"

    id = Column(Integer, primary_key=True, autoincrement=True)

    # External references to UserService — no FK constraints
    patient_id = Column(Integer, nullable=False)
    doctor_id  = Column(Integer, nullable=False)

    notes = Column(Text, nullable=True)

    # FK to blood_type lookup table
    blood_type_id = Column(SmallInteger, ForeignKey("blood_type.id"), nullable=True)

    weight = Column(DECIMAL(5, 2), nullable=True)   # in kilograms
    height = Column(DECIMAL(5, 2), nullable=True)   # in meters

    # Timestamp set automatically when the record is created
    created_at = Column(DateTime, server_default=func.now(), nullable=False)

    # --- Relationships ---
    blood_type = relationship("BloodType", back_populates="medical_records")
    diagnoses = relationship("Diagnosis", back_populates="medical_record", cascade="all, delete-orphan")
    allergies = relationship("Allergy", back_populates="medical_record", cascade="all, delete-orphan")
    prescribed_medications = relationship("PrescribedMedication", back_populates="medical_record", cascade="all, delete-orphan")
    procedures = relationship("MedicalProcedure", back_populates="medical_record", cascade="all, delete-orphan")
