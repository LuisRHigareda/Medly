"""
main.py
-------
Entry point for MedicalRecordService.
Creates the FastAPI application instance and registers all routers.

This service is part of the Medly microservices system and is responsible
for managing clinical data: medical records, diagnoses, allergies,
medications, and medical procedures.
"""

from fastapi import FastAPI, Depends
from fastapi.middleware.cors import CORSMiddleware
from sqlalchemy.orm import Session
from sqlalchemy import text

from app.database.session import get_db
from app.routers import medical_record, diagnosis, allergy, medication, prescribed_medication, medical_procedure

# Create the FastAPI application instance
app = FastAPI(title="MedicalRecordService")

# Allow requests from the frontend dev server and Electron
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:5173", "http://127.0.0.1:5173"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Register routers — each one handles a group of related endpoints
app.include_router(medical_record.router)
app.include_router(diagnosis.router)
app.include_router(allergy.router)
app.include_router(medication.router)
app.include_router(prescribed_medication.router)
app.include_router(medical_procedure.router)


@app.get("/")
def root():
    """Root endpoint — confirms the service is running."""
    return {"message": "MedicalRecordService running"}


@app.get("/health")
def health(db: Session = Depends(get_db)):
    """
    Health check endpoint.

    Executes a lightweight query against the database to verify that
    the connection to medical_record_db is working correctly.
    Returns HTTP 200 with status 'ok' if everything is fine,
    or raises an exception if the database is unreachable.
    """
    db.execute(text("SELECT 1"))
    return {"status": "ok", "database": "connected"}
