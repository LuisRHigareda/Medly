"""
models/__init__.py
------------------
Exposes all SQLAlchemy models from a single import point.
Importing this package ensures all models are registered with
the Base metadata, which is required for table creation and
relationship resolution.
"""

from app.models.blood_type import BloodType
from app.models.diagnosis_status import DiagnosisStatus
from app.models.medical_record import MedicalRecord
from app.models.diagnosis import Diagnosis
from app.models.allergy import Allergy
from app.models.medication import Medication
from app.models.prescribed_medication import PrescribedMedication
from app.models.medical_procedure import MedicalProcedure
