"""
medical_record.py (router)
--------------------------
Endpoints for the MedicalRecord entity.
A medical record is the core entity of this service — all other clinical
data (diagnoses, allergies, medications, procedures) links back to it.

Endpoints:
- POST   /medical-records/                              Create a new medical record
- GET    /medical-records/{record_id}                   Get a record by its ID
- GET    /medical-records/patient/{patient_id}          Get all records for a patient
- GET    /medical-records/{record_id}/prescription      Download a PDF prescription
"""

from fastapi import APIRouter, Depends, HTTPException, status
from fastapi.responses import Response
from sqlalchemy.orm import Session, joinedload, selectinload

from app.database.session import get_db
from app.models.medical_record import MedicalRecord
from app.models.diagnosis import Diagnosis
from app.models.prescribed_medication import PrescribedMedication
from app.schemas.medical_record import MedicalRecordCreate, MedicalRecordResponse
from app.utils.pdf_generator import build_prescription_pdf

# Create a router with a shared prefix and tag for API docs
router = APIRouter(
    prefix="/medical-records",
    tags=["Medical Records"],
)


@router.post("/", response_model=MedicalRecordResponse, status_code=status.HTTP_201_CREATED)
def create_medical_record(record: MedicalRecordCreate, db: Session = Depends(get_db)):
    """
    Create a new medical record for a patient.

    Receives the record data, persists it to the database, and returns
    the created record including its generated id and created_at timestamp.
    """
    # Build the SQLAlchemy model instance from the validated Pydantic data
    db_record = MedicalRecord(**record.model_dump())

    db.add(db_record)       # stage the new record
    db.commit()             # persist to the database
    db.refresh(db_record)   # reload to get generated fields (id, created_at)

    return db_record


@router.get("/{record_id}", response_model=MedicalRecordResponse)
def get_medical_record(record_id: int, db: Session = Depends(get_db)):
    """
    Retrieve a single medical record by its ID.

    Returns 404 if no record with the given ID exists.
    """
    db_record = db.query(MedicalRecord).filter(MedicalRecord.id == record_id).first()

    if db_record is None:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"Medical record with id {record_id} not found"
        )

    return db_record


@router.get("/patient/{patient_id}", response_model=list[MedicalRecordResponse])
def get_records_by_patient(patient_id: int, db: Session = Depends(get_db)):
    """
    Retrieve all medical records belonging to a specific patient.

    Returns an empty list if the patient has no records yet.
    patient_id is an external reference to UserService — it is not
    validated against any local table.
    """
    records = db.query(MedicalRecord).filter(MedicalRecord.patient_id == patient_id).all()

    return records


@router.get("/{record_id}/prescription")
def get_prescription_pdf(record_id: int, db: Session = Depends(get_db)):
    """
    Generate and download a PDF prescription for a medical record.

    Eagerly loads all related entities (blood type, diagnoses with status,
    allergies, prescribed medications with medication name, and procedures)
    in a single query to avoid N+1 problems, then delegates PDF construction
    to build_prescription_pdf().

    Returns the PDF as an inline attachment with content-type application/pdf.
    Returns 404 if no record with the given ID exists.
    """
    # Eagerly load all relationships so the PDF generator can access them
    # without triggering extra queries after the session is used
    db_record = (
        db.query(MedicalRecord)
        .options(
            joinedload(MedicalRecord.blood_type),
            selectinload(MedicalRecord.diagnoses).joinedload(Diagnosis.status),
            selectinload(MedicalRecord.allergies),
            selectinload(MedicalRecord.prescribed_medications).joinedload(PrescribedMedication.medication),
            selectinload(MedicalRecord.procedures),
        )
        .filter(MedicalRecord.id == record_id)
        .first()
    )

    if db_record is None:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"Medical record with id {record_id} not found"
        )

    pdf_bytes = build_prescription_pdf(db_record)

    return Response(
        content=pdf_bytes,
        media_type="application/pdf",
        headers={
            "Content-Disposition": f"inline; filename=prescription_{record_id}.pdf"
        },
    )