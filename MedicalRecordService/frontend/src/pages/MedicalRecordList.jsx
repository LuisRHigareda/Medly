import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { getMedicalRecordsByPatient } from '../api/medicalRecords'
import './MedicalRecordList.css'

function MedicalRecordList() {
  const { patientId } = useParams()
  const navigate = useNavigate()
  const [records, setRecords] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    getMedicalRecordsByPatient(patientId)
      .then((res) => setRecords(res.data))
      .catch(() => setError('Could not load records for this patient.'))
      .finally(() => setLoading(false))
  }, [patientId])

  return (
    <div className="list-container">
      <div className="list-header">
        <button onClick={() => navigate('/patients')} className="btn-back">
          ← Back
        </button>
        <h2>Patient #{patientId} — Medical Records</h2>
        <button
          onClick={() => navigate(`/patients/${patientId}/records/new`)}
          className="btn-new"
        >
          + New Record
        </button>
      </div>

      {loading && <p className="msg">Loading...</p>}
      {error && <p className="error-msg">{error}</p>}
      {!loading && !error && records.length === 0 && (
        <p className="msg">No records found for this patient.</p>
      )}

      <div className="records-list">
        {records.map((record) => (
          <div
            key={record.id}
            className="record-card"
            onClick={() => navigate(`/records/${record.id}`)}
          >
            <div className="record-card-header">Record #{record.id}</div>
            <div className="record-card-body">
              <span>Doctor ID: {record.doctor_id}</span>
              <span>Date: {new Date(record.created_at).toLocaleDateString()}</span>
            </div>
            {record.notes && <p className="record-card-notes">{record.notes}</p>}
          </div>
        ))}
      </div>
    </div>
  )
}

export default MedicalRecordList