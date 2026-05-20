"""
schemas/__init__.py
-------------------
Exposes all Pydantic schemas from a single import point.
Routers and services can import any schema directly from app.schemas
instead of importing from individual files.
"""

from app.schemas.medical_record import MedicalRecordBase, MedicalRecordCreate, MedicalRecordResponse
from app.schemas.diagnosis import DiagnosisBase, DiagnosisCreate, DiagnosisResponse
from app.schemas.allergy import AllergyBase, AllergyCreate, AllergyResponse
from app.schemas.medication import MedicationBase, MedicationCreate, MedicationResponse
from app.schemas.prescribed_medication import PrescribedMedicationBase, PrescribedMedicationCreate, PrescribedMedicationResponse
from app.schemas.medical_procedure import MedicalProcedureBase, MedicalProcedureCreate, MedicalProcedureResponse
