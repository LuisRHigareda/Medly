"""
prescribed_medication.py (router)
----------------------------------
Endpoints for the PrescribedMedication entity.
Links a medication from the catalog to a specific medical record,
including dosage instructions and the treatment period.

Endpoints:
- POST   /prescribed-medications/                              Prescribe a medication to a medical record
- GET    /prescribed-medications/{prescription_id}             Get a prescription by its ID
- GET    /prescribed-medications/record/{medical_record_id}    Get all prescriptions for a medical record
"""

from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session

from app.database.session import get_db
from app.models.prescribed_medication import PrescribedMedication
from app.schemas.prescribed_medication import PrescribedMedicationCreate, PrescribedMedicationResponse

router = APIRouter(
    prefix="/prescribed-medications",
    tags=["Prescribed Medications"],
)


@router.post("/", response_model=PrescribedMedicationResponse, status_code=status.HTTP_201_CREATED)
def create_prescribed_medication(prescription: PrescribedMedicationCreate, db: Session = Depends(get_db)):
    """
    Prescribe a medication within a medical record.

    Receives the prescription data (medication, dose, frequency, dates),
    persists it to the database, and returns the created prescription
    including its generated id.
    """
    db_prescription = PrescribedMedication(**prescription.model_dump())

    db.add(db_prescription)
    db.commit()
    db.refresh(db_prescription)

    return db_prescription


@router.get("/{prescription_id}", response_model=PrescribedMedicationResponse)
def get_prescribed_medication(prescription_id: int, db: Session = Depends(get_db)):
    """
    Retrieve a single prescription by its ID.

    Returns 404 if no prescription with the given ID exists.
    """
    db_prescription = db.query(PrescribedMedication).filter(PrescribedMedication.id == prescription_id).first()

    if db_prescription is None:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"Prescription with id {prescription_id} not found"
        )

    return db_prescription


@router.get("/record/{medical_record_id}", response_model=list[PrescribedMedicationResponse])
def get_prescriptions_by_record(medical_record_id: int, db: Session = Depends(get_db)):
    """
    Retrieve all prescriptions belonging to a specific medical record.

    Returns an empty list if the record has no prescriptions yet.
    """
    prescriptions = db.query(PrescribedMedication).filter(
        PrescribedMedication.medical_record_id == medical_record_id
    ).all()

    return prescriptions