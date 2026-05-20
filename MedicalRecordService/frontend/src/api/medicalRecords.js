import api from './client'

export const getMedicalRecordsByPatient = (patientId) =>
  api.get(`/api/medical-records/patient/${patientId}`)

export const getMedicalRecordById = (id) =>
  api.get(`/api/medical-records/${id}`)

export const createMedicalRecord = (data) =>
  api.post('/api/medical-records/', data)

export const getDiagnosesByRecord = (recordId) =>
  api.get(`/api/diagnoses/record/${recordId}`)

export const createDiagnosis = (data) =>
  api.post('/api/diagnoses/', data)

export const getAllergiesByRecord = (recordId) =>
  api.get(`/api/allergies/record/${recordId}`)

export const createAllergy = (data) =>
  api.post('/api/allergies/', data)

export const getPrescriptionsByRecord = (recordId) =>
  api.get(`/api/prescribed-medications/record/${recordId}`)

export const createPrescription = (data) =>
  api.post('/api/prescribed-medications/', data)

export const getProceduresByRecord = (recordId) =>
  api.get(`/api/procedures/record/${recordId}`)

export const createProcedure = (data) =>
  api.post('/api/procedures/', data)

export const getMedications = () =>
  api.get('/api/medications/')

export const getPrescriptionPdf = (recordId) =>
  api.get(`/api/medical-records/${recordId}/prescription`, { responseType: 'blob' })