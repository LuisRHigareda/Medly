"""
test_endpoints.py
-----------------
Integration tests for all MedicalRecordService endpoints.

Uses an in-memory SQLite database (configured in conftest.py) so the
real MySQL database is never affected. Each test is fully isolated —
tables are created fresh and dropped after every test function.

Test coverage:
- Medical Records  : create, get by id, get by patient, 404 on missing
- Diagnoses        : create, get by record
- Allergies        : create, get by record
- Medications      : create, list all, get by id
- Prescribed Meds  : create, get by record
- Procedures       : create, get by record
- PDF prescription : endpoint returns application/pdf
"""

import pytest
from app.models.blood_type import BloodType
from app.models.diagnosis_status import DiagnosisStatus


# Helpers — seed lookup tables that some entities depend on
def seed_blood_type(db_session):
    """Insert a blood type row so medical records can reference it."""
    bt = BloodType(id=1, type_name="O+")
    db_session.add(bt)
    db_session.commit()


def seed_diagnosis_status(db_session):
    """Insert a diagnosis status row so diagnoses can reference it."""
    ds = DiagnosisStatus(id=1, status_name="ACTIVE")
    db_session.add(ds)
    db_session.commit()


# Medical Records
class TestMedicalRecords:

    def test_create_medical_record(self, client, db_session):
        """POST /medical-records/ should return 201 and the created record."""
        seed_blood_type(db_session)
        response = client.post("/medical-records/", json={
            "patient_id": 1,
            "doctor_id": 2,
            "notes": "Test notes",
            "blood_type_id": 1,
            "weight": 70.5,
            "height": 1.68,
        })
        assert response.status_code == 201
        data = response.json()
        assert data["patient_id"] == 1
        assert data["doctor_id"] == 2
        assert data["id"] is not None

    def test_get_medical_record_by_id(self, client, db_session):
        """GET /medical-records/{id} should return the record."""
        seed_blood_type(db_session)
        create = client.post("/medical-records/", json={
            "patient_id": 1,
            "doctor_id": 2,
        })
        record_id = create.json()["id"]

        response = client.get(f"/medical-records/{record_id}")
        assert response.status_code == 200
        assert response.json()["id"] == record_id

    def test_get_medical_record_not_found(self, client):
        """GET /medical-records/{id} should return 404 for a missing record."""
        response = client.get("/medical-records/9999")
        assert response.status_code == 404

    def test_get_records_by_patient(self, client, db_session):
        """GET /medical-records/patient/{id} should return all records for a patient."""
        seed_blood_type(db_session)
        client.post("/medical-records/", json={"patient_id": 5, "doctor_id": 1})
        client.post("/medical-records/", json={"patient_id": 5, "doctor_id": 1})

        response = client.get("/medical-records/patient/5")
        assert response.status_code == 200
        assert len(response.json()) == 2

    def test_get_records_by_patient_empty(self, client):
        """GET /medical-records/patient/{id} should return empty list if no records."""
        response = client.get("/medical-records/patient/999")
        assert response.status_code == 200
        assert response.json() == []


# Diagnoses
class TestDiagnoses:

    def test_create_diagnosis(self, client, db_session):
        """POST /diagnoses/ should return 201 and the created diagnosis."""
        seed_diagnosis_status(db_session)
        record = client.post("/medical-records/", json={"patient_id": 1, "doctor_id": 1})
        record_id = record.json()["id"]

        response = client.post("/diagnoses/", json={
            "medical_record_id": record_id,
            "name": "Hypertension",
            "diagnosed_date": "2026-04-25T10:00:00",
            "status_id": 1,
        })
        assert response.status_code == 201
        assert response.json()["name"] == "Hypertension"

    def test_get_diagnoses_by_record(self, client, db_session):
        """GET /diagnoses/record/{id} should return all diagnoses for a record."""
        seed_diagnosis_status(db_session)
        record = client.post("/medical-records/", json={"patient_id": 1, "doctor_id": 1})
        record_id = record.json()["id"]

        client.post("/diagnoses/", json={
            "medical_record_id": record_id,
            "name": "Diabetes",
            "diagnosed_date": "2026-04-25T10:00:00",
        })

        response = client.get(f"/diagnoses/record/{record_id}")
        assert response.status_code == 200
        assert len(response.json()) == 1


# Allergies
class TestAllergies:

    def test_create_allergy(self, client):
        """POST /allergies/ should return 201 and the created allergy."""
        record = client.post("/medical-records/", json={"patient_id": 1, "doctor_id": 1})
        record_id = record.json()["id"]

        response = client.post("/allergies/", json={
            "medical_record_id": record_id,
            "allergen": "Penicillin",
            "reaction": "Hives",
            "severity": "moderate",
        })
        assert response.status_code == 201
        assert response.json()["allergen"] == "Penicillin"

    def test_get_allergies_by_record(self, client):
        """GET /allergies/record/{id} should return all allergies for a record."""
        record = client.post("/medical-records/", json={"patient_id": 1, "doctor_id": 1})
        record_id = record.json()["id"]
        client.post("/allergies/", json={"medical_record_id": record_id, "allergen": "Dust"})

        response = client.get(f"/allergies/record/{record_id}")
        assert response.status_code == 200
        assert len(response.json()) == 1


# Medications
class TestMedications:

    def test_create_medication(self, client):
        """POST /medications/ should return 201 and the created medication."""
        response = client.post("/medications/", json={"name": "Ibuprofen"})
        assert response.status_code == 201
        assert response.json()["name"] == "Ibuprofen"

    def test_get_all_medications(self, client):
        """GET /medications/ should return all medications in the catalog."""
        client.post("/medications/", json={"name": "Losartan"})
        client.post("/medications/", json={"name": "Metformin"})

        response = client.get("/medications/")
        assert response.status_code == 200
        assert len(response.json()) == 2

    def test_get_medication_by_id(self, client):
        """GET /medications/{id} should return a single medication."""
        created = client.post("/medications/", json={"name": "Aspirin"})
        med_id = created.json()["id"]

        response = client.get(f"/medications/{med_id}")
        assert response.status_code == 200
        assert response.json()["name"] == "Aspirin"

    def test_get_medication_not_found(self, client):
        """GET /medications/{id} should return 404 for a missing medication."""
        response = client.get("/medications/9999")
        assert response.status_code == 404


# Prescribed Medications
class TestPrescribedMedications:

    def test_create_prescribed_medication(self, client):
        """POST /prescribed-medications/ should return 201 and the prescription."""
        record = client.post("/medical-records/", json={"patient_id": 1, "doctor_id": 1})
        record_id = record.json()["id"]
        med = client.post("/medications/", json={"name": "Losartan"})
        med_id = med.json()["id"]

        response = client.post("/prescribed-medications/", json={
            "medical_record_id": record_id,
            "medication_id": med_id,
            "dose": 50.0,
            "frequency": 1,
            "start_date": "2026-04-25",
            "end_date": "2026-05-25",
        })
        assert response.status_code == 201
        assert response.json()["medication_id"] == med_id

    def test_get_prescriptions_by_record(self, client):
        """GET /prescribed-medications/record/{id} should return all prescriptions."""
        record = client.post("/medical-records/", json={"patient_id": 1, "doctor_id": 1})
        record_id = record.json()["id"]
        med = client.post("/medications/", json={"name": "Metformin"})
        med_id = med.json()["id"]
        client.post("/prescribed-medications/", json={
            "medical_record_id": record_id,
            "medication_id": med_id,
            "start_date": "2026-04-25",
        })

        response = client.get(f"/prescribed-medications/record/{record_id}")
        assert response.status_code == 200
        assert len(response.json()) == 1


# Medical Procedures
class TestMedicalProcedures:

    def test_create_procedure(self, client):
        """POST /procedures/ should return 201 and the created procedure."""
        record = client.post("/medical-records/", json={"patient_id": 1, "doctor_id": 1})
        record_id = record.json()["id"]

        response = client.post("/procedures/", json={
            "medical_record_id": record_id,
            "name": "Blood test",
            "date": "2026-04-25",
            "notes": "Fasting required",
        })
        assert response.status_code == 201
        assert response.json()["name"] == "Blood test"

    def test_get_procedures_by_record(self, client):
        """GET /procedures/record/{id} should return all procedures for a record."""
        record = client.post("/medical-records/", json={"patient_id": 1, "doctor_id": 1})
        record_id = record.json()["id"]
        client.post("/procedures/", json={
            "medical_record_id": record_id,
            "name": "X-Ray",
            "date": "2026-04-25",
        })

        response = client.get(f"/procedures/record/{record_id}")
        assert response.status_code == 200
        assert len(response.json()) == 1


# PDF Prescription
class TestPrescriptionPDF:

    def test_prescription_returns_pdf(self, client):
        """GET /medical-records/{id}/prescription should return a PDF file."""
        record = client.post("/medical-records/", json={"patient_id": 1, "doctor_id": 1})
        record_id = record.json()["id"]

        response = client.get(f"/medical-records/{record_id}/prescription")
        assert response.status_code == 200
        assert response.headers["content-type"] == "application/pdf"

    def test_prescription_not_found(self, client):
        """GET /medical-records/{id}/prescription should return 404 if record missing."""
        response = client.get("/medical-records/9999/prescription")
        assert response.status_code == 404
