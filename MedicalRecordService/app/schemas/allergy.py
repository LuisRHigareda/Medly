"""
allergy.py (schema)
-------------------
Pydantic schemas for the Allergy entity.

Schemas in this file:
- AllergyBase     : shared fields
- AllergyCreate   : input — what the client sends to create an allergy
- AllergyResponse : output — what the API returns
"""

from typing import Optional

from pydantic import BaseModel


class AllergyBase(BaseModel):
    """Fields shared between Create and Response schemas."""

    medical_record_id: int               # the record this allergy belongs to
    allergen: str                        # substance causing the allergy (e.g. "Penicillin")
    reaction: Optional[str] = None      # observed reaction (e.g. "hives", "anaphylaxis")
    severity: Optional[str] = None      # e.g. "mild", "moderate", "severe"


class AllergyCreate(AllergyBase):
    """
    Schema for creating a new allergy entry.
    The client must provide medical_record_id and allergen.
    reaction and severity are optional.
    """
    pass


class AllergyResponse(AllergyBase):
    """
    Schema for returning an allergy entry from the API.
    Adds the auto-generated id field.
    """

    id: int

    class Config:
        from_attributes = True