"""
allergy.py (router)
-------------------
Endpoints for the Allergy entity.
An allergy always belongs to a medical record and describes the allergen,
the patient's reaction, and the severity level.

Endpoints:
- POST   /allergies/                              Create a new allergy entry
- GET    /allergies/{allergy_id}                  Get an allergy by its ID
- GET    /allergies/record/{medical_record_id}    Get all allergies for a medical record
"""

from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session

from app.database.session import get_db
from app.models.allergy import Allergy
from app.schemas.allergy import AllergyCreate, AllergyResponse

router = APIRouter(
    prefix="/allergies",
    tags=["Allergies"],
)


@router.post("/", response_model=AllergyResponse, status_code=status.HTTP_201_CREATED)
def create_allergy(allergy: AllergyCreate, db: Session = Depends(get_db)):
    """
    Create a new allergy entry linked to a medical record.

    Receives the allergy data, persists it to the database, and returns
    the created allergy including its generated id.
    """
    db_allergy = Allergy(**allergy.model_dump())

    db.add(db_allergy)
    db.commit()
    db.refresh(db_allergy)

    return db_allergy


@router.get("/{allergy_id}", response_model=AllergyResponse)
def get_allergy(allergy_id: int, db: Session = Depends(get_db)):
    """
    Retrieve a single allergy entry by its ID.

    Returns 404 if no allergy with the given ID exists.
    """
    db_allergy = db.query(Allergy).filter(Allergy.id == allergy_id).first()

    if db_allergy is None:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"Allergy with id {allergy_id} not found"
        )

    return db_allergy


@router.get("/record/{medical_record_id}", response_model=list[AllergyResponse])
def get_allergies_by_record(medical_record_id: int, db: Session = Depends(get_db)):
    """
    Retrieve all allergies belonging to a specific medical record.

    Returns an empty list if the record has no allergies registered yet.
    """
    allergies = db.query(Allergy).filter(Allergy.medical_record_id == medical_record_id).all()

    return allergies