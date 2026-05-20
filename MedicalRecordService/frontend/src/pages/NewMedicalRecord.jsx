import { useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { createMedicalRecord } from '../api/medicalRecords'
import './NewMedicalRecord.css'

function NewMedicalRecord() {
  const { patientId } = useParams()
  const navigate = useNavigate()

  const [form, setForm] = useState({
    doctor_id: '',
    notes: '',
    weight: '',
    height: '',
  })
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(false)

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value })
  }

  const handleSubmit = (e) => {
    e.preventDefault()
    setLoading(true)
    setError(null)

    const payload = {
      patient_id: parseInt(patientId),
      doctor_id: parseInt(form.doctor_id),
      notes: form.notes || null,
      weight: form.weight ? parseFloat(form.weight) : null,
      height: form.height ? parseFloat(form.height) : null,
    }

    createMedicalRecord(payload)
      .then((res) => navigate(`/records/${res.data.id}`))
      .catch(() => setError('Failed to create medical record. Please try again.'))
      .finally(() => setLoading(false))
  }

  return (
    <div className="new-record-container">
      <div className="new-record-card">
        <div className="new-record-header">
          <button onClick={() => navigate(-1)} className="btn-back">← Back</button>
          <h2>New Medical Record</h2>
          <span className="patient-label">Patient #{patientId}</span>
        </div>

        {error && <p className="form-error">{error}</p>}

        <form onSubmit={handleSubmit} className="new-record-form">
          <label>Doctor ID *</label>
          <input type="number" name="doctor_id" value={form.doctor_id} onChange={handleChange} required />

          <label>Weight (kg)</label>
          <input type="number" name="weight" step="0.1" value={form.weight} onChange={handleChange} />

          <label>Height (m)</label>
          <input type="number" name="height" step="0.01" value={form.height} onChange={handleChange} />

          <label>Notes</label>
          <textarea name="notes" value={form.notes} onChange={handleChange} placeholder="General observations..." rows={3} />

          <button type="submit" disabled={loading}>
            {loading ? 'Creating...' : 'Create Record'}
          </button>
        </form>
      </div>
    </div>
  )
}

export default NewMedicalRecord