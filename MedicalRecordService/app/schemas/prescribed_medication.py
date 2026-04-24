"""
prescribed_medication.py (schema)
----------------------------------
Pydantic schemas for the PrescribedMedication entity.
Represents a medication prescribed to a patient within a medical record,
including dosage instructions and the treatment period.

Schemas in this file:
- PrescribedMedicationBase     : shared fields
- PrescribedMedicationCreate   : input — what the client sends to prescribe a medication
- PrescribedMedicationResponse : output — what the API returns
"""

from datetime import date
from decimal import Decimal
from typing import Optional

from pydantic import BaseModel


class PrescribedMedicationBase(BaseModel):
    """Fields shared between Create and Response schemas."""

    medical_record_id: int                  # the record this prescription belongs to
    medication_id: int                      # references medication catalog
    frequency: Optional[int] = None        # times per day (e.g. 3 = three times a day)
    dose: Optional[Decimal] = None         # dose amount (e.g. 500.00 for 500mg)
    start_date: date                        # first day of treatment
    end_date: Optional[date] = None        # last day of treatment — None if ongoing


class PrescribedMedicationCreate(PrescribedMedicationBase):
    """
    Schema for prescribing a medication within a medical record.
    The client must provide medical_record_id, medication_id, and start_date.
    frequency, dose, and end_date are optional.
    """
    pass


class PrescribedMedicationResponse(PrescribedMedicationBase):
    """
    Schema for returning a prescribed medication from the API.
    Adds the auto-generated id field.
    """

    id: int

    class Config:
        from_attributes = True