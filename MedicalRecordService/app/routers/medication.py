"""
medication.py (router)
----------------------
Endpoints for the Medication entity.
Medication is a catalog of available drugs that can be referenced
when prescribing medications within a medical record.

Endpoints:
- POST   /medications/           Add a new medication to the catalog
- GET    /medications/           Get all medications in the catalog
- GET    /medications/{med_id}   Get a single medication by its ID
"""

from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session

from app.database.session import get_db
from app.models.medication import Medication
from app.schemas.medication import MedicationCreate, MedicationResponse

router = APIRouter(
    prefix="/medications",
    tags=["Medications"],
)


@router.post("/", response_model=MedicationResponse, status_code=status.HTTP_201_CREATED)
def create_medication(medication: MedicationCreate, db: Session = Depends(get_db)):
    """
    Add a new medication to the catalog.

    Receives the medication name, persists it to the database, and returns
    the created medication including its generated id.
    """
    db_medication = Medication(**medication.model_dump())

    db.add(db_medication)
    db.commit()
    db.refresh(db_medication)

    return db_medication


@router.get("/", response_model=list[MedicationResponse])
def get_medications(db: Session = Depends(get_db)):
    """
    Retrieve all medications available in the catalog.

    Returns an empty list if no medications have been added yet.
    """
    medications = db.query(Medication).all()

    return medications


@router.get("/{medication_id}", response_model=MedicationResponse)
def get_medication(medication_id: int, db: Session = Depends(get_db)):
    """
    Retrieve a single medication by its ID.

    Returns 404 if no medication with the given ID exists.
    """
    db_medication = db.query(Medication).filter(Medication.id == medication_id).first()

    if db_medication is None:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"Medication with id {medication_id} not found"
        )

    return db_medication