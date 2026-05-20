"""
medical_procedure.py (router)
------------------------------
Endpoints for the MedicalProcedure entity.
A medical procedure is a clinical intervention performed on a patient
as part of a medical record (e.g. surgery, blood test, imaging).

Endpoints:
- POST   /procedures/                              Register a new medical procedure
- GET    /procedures/{procedure_id}                Get a procedure by its ID
- GET    /procedures/record/{medical_record_id}    Get all procedures for a medical record
"""

from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session

from app.database.session import get_db
from app.models.medical_procedure import MedicalProcedure
from app.schemas.medical_procedure import MedicalProcedureCreate, MedicalProcedureResponse

router = APIRouter(
    prefix="/procedures",
    tags=["Medical Procedures"],
)


@router.post("/", response_model=MedicalProcedureResponse, status_code=status.HTTP_201_CREATED)
def create_procedure(procedure: MedicalProcedureCreate, db: Session = Depends(get_db)):
    """
    Register a new medical procedure linked to a medical record.

    Receives the procedure data (name, date, notes), persists it to the
    database, and returns the created procedure including its generated id.
    """
    db_procedure = MedicalProcedure(**procedure.model_dump())

    db.add(db_procedure)
    db.commit()
    db.refresh(db_procedure)

    return db_procedure


@router.get("/{procedure_id}", response_model=MedicalProcedureResponse)
def get_procedure(procedure_id: int, db: Session = Depends(get_db)):
    """
    Retrieve a single medical procedure by its ID.

    Returns 404 if no procedure with the given ID exists.
    """
    db_procedure = db.query(MedicalProcedure).filter(MedicalProcedure.id == procedure_id).first()

    if db_procedure is None:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"Procedure with id {procedure_id} not found"
        )

    return db_procedure


@router.get("/record/{medical_record_id}", response_model=list[MedicalProcedureResponse])
def get_procedures_by_record(medical_record_id: int, db: Session = Depends(get_db)):
    """
    Retrieve all medical procedures belonging to a specific medical record.

    Returns an empty list if the record has no procedures registered yet.
    """
    procedures = db.query(MedicalProcedure).filter(
        MedicalProcedure.medical_record_id == medical_record_id
    ).all()

    return procedures