import api from './client'

export const getMedicalRecordsByPatient = (patientId) =>
  api.get(`/medical-records/patient/${patientId}`)

export const getMedicalRecordById = (id) =>
  api.get(`/medical-records/${id}`)

export const createMedicalRecord = (data) =>
  api.post('/medical-records/', data)

export const getDiagnosesByRecord = (recordId) =>
  api.get(`/diagnoses/record/${recordId}`)

export const createDiagnosis = (data) =>
  api.post('/diagnoses/', data)

export const getAllergiesByRecord = (recordId) =>
  api.get(`/allergies/record/${recordId}`)

export const createAllergy = (data) =>
  api.post('/allergies/', data)

export const getPrescriptionsByRecord = (recordId) =>
  api.get(`/prescribed-medications/record/${recordId}`)

export const createPrescription = (data) =>
  api.post('/prescribed-medications/', data)

export const getProceduresByRecord = (recordId) =>
  api.get(`/procedures/record/${recordId}`)

export const createProcedure = (data) =>
  api.post('/procedures/', data)

export const getMedications = () =>
  api.get('/medications/')

export const getPrescriptionPdf = (recordId) =>
  api.get(`/medical-records/${recordId}/prescription`, { responseType: 'blob' })
