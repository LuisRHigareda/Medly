"""
diagnosis.py (router)
---------------------
Endpoints for the Diagnosis entity.
A diagnosis always belongs to a medical record and has an optional status
(ACTIVE, RESOLVED, CHRONIC, IN_TREATMENT).

Endpoints:
- POST   /diagnoses/                              Create a new diagnosis
- GET    /diagnoses/{diagnosis_id}                Get a diagnosis by its ID
- GET    /diagnoses/record/{medical_record_id}    Get all diagnoses for a medical record
"""

from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session

from app.database.session import get_db
from app.models.diagnosis import Diagnosis
from app.schemas.diagnosis import DiagnosisCreate, DiagnosisResponse

router = APIRouter(
    prefix="/diagnoses",
    tags=["Diagnoses"],
)


@router.post("/", response_model=DiagnosisResponse, status_code=status.HTTP_201_CREATED)
def create_diagnosis(diagnosis: DiagnosisCreate, db: Session = Depends(get_db)):
    """
    Create a new diagnosis linked to a medical record.

    Receives the diagnosis data, persists it to the database, and returns
    the created diagnosis including its generated id.
    """
    db_diagnosis = Diagnosis(**diagnosis.model_dump())

    db.add(db_diagnosis)
    db.commit()
    db.refresh(db_diagnosis)

    return db_diagnosis


@router.get("/{diagnosis_id}", response_model=DiagnosisResponse)
def get_diagnosis(diagnosis_id: int, db: Session = Depends(get_db)):
    """
    Retrieve a single diagnosis by its ID.

    Returns 404 if no diagnosis with the given ID exists.
    """
    db_diagnosis = db.query(Diagnosis).filter(Diagnosis.id == diagnosis_id).first()

    if db_diagnosis is None:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"Diagnosis with id {diagnosis_id} not found"
        )

    return db_diagnosis


@router.get("/record/{medical_record_id}", response_model=list[DiagnosisResponse])
def get_diagnoses_by_record(medical_record_id: int, db: Session = Depends(get_db)):
    """
    Retrieve all diagnoses belonging to a specific medical record.

    Returns an empty list if the record has no diagnoses yet.
    """
    diagnoses = db.query(Diagnosis).filter(Diagnosis.medical_record_id == medical_record_id).all()

    return diagnoses