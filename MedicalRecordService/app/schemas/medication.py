"""
medication.py (schema)
----------------------
Pydantic schemas for the Medication entity.
Medication is a simple catalog — it only stores the drug name.
It is referenced by PrescribedMedication when a doctor prescribes a drug.

Schemas in this file:
- MedicationBase     : shared fields
- MedicationCreate   : input — what the client sends to add a medication
- MedicationResponse : output — what the API returns
"""

from pydantic import BaseModel, ConfigDict


class MedicationBase(BaseModel):
    """Fields shared between Create and Response schemas."""

    name: str   # medication name, must be unique (e.g. "Ibuprofen 400mg")


class MedicationCreate(MedicationBase):
    """
    Schema for adding a new medication to the catalog.
    The client only needs to provide the name.
    """
    pass


class MedicationResponse(MedicationBase):
    """
    Schema for returning a medication from the API.
    Adds the auto-generated id field.
    """

    model_config = ConfigDict(from_attributes=True)

    id: int