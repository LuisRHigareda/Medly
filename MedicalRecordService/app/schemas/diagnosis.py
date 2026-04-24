"""
diagnosis.py (schema)
---------------------
Pydantic schemas for the Diagnosis entity.

Schemas in this file:
- DiagnosisBase     : shared fields
- DiagnosisCreate   : input — what the client sends to create a diagnosis
- DiagnosisResponse : output — what the API returns
"""

from datetime import datetime
from typing import Optional

from pydantic import BaseModel


class DiagnosisBase(BaseModel):
    """Fields shared between Create and Response schemas."""

    medical_record_id: int           # the record this diagnosis belongs to
    name: str                        # name of the diagnosis (e.g. "Type 2 Diabetes")
    diagnosed_date: datetime         # date and time the diagnosis was made
    status_id: Optional[int] = None  # references diagnosis_status.id (1–4)


class DiagnosisCreate(DiagnosisBase):
    """
    Schema for creating a new diagnosis.
    The client must provide medical_record_id, name, and diagnosed_date.
    status_id is optional — defaults to None if not provided.
    """
    pass


class DiagnosisResponse(DiagnosisBase):
    """
    Schema for returning a diagnosis from the API.
    Adds the auto-generated id field.
    """

    id: int

    class Config:
        from_attributes = True