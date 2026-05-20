"""
medical_procedure.py (schema)
------------------------------
Pydantic schemas for the MedicalProcedure entity.
Represents a clinical procedure performed on a patient as part of
a medical record (e.g. surgery, blood test, imaging).

Schemas in this file:
- MedicalProcedureBase     : shared fields
- MedicalProcedureCreate   : input — what the client sends to register a procedure
- MedicalProcedureResponse : output — what the API returns
"""

from datetime import date
from typing import Optional

from pydantic import BaseModel, ConfigDict


class MedicalProcedureBase(BaseModel):
    """Fields shared between Create and Response schemas."""

    medical_record_id: int               # the record this procedure belongs to
    date: date                           # date the procedure was performed
    name: str                            # procedure name (e.g. "Blood test", "X-ray")
    notes: Optional[str] = None         # additional observations or results


class MedicalProcedureCreate(MedicalProcedureBase):
    """
    Schema for registering a new medical procedure.
    The client must provide medical_record_id, date, and name.
    notes is optional.
    """
    pass


class MedicalProcedureResponse(MedicalProcedureBase):
    """
    Schema for returning a medical procedure from the API.
    Adds the auto-generated id field.
    """

    model_config = ConfigDict(from_attributes=True)

    id: int