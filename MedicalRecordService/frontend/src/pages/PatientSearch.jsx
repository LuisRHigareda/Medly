import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import './PatientSearch.css'

function PatientSearch() {
  const [patientId, setPatientId] = useState('')
  const navigate = useNavigate()

  const handleSearch = (e) => {
    e.preventDefault()
    if (patientId.trim()) {
      navigate(`/patients/${patientId}/records`)
    }
  }

  return (
    <div className="search-container">
      <div className="search-card">
        <h1>Medly</h1>
        <p>Medical Record Service</p>
        <form onSubmit={handleSearch} className="search-form">
          <label>Patient ID</label>
          <input
            type="number"
            value={patientId}
            onChange={(e) => setPatientId(e.target.value)}
            placeholder="Enter patient ID"
            required
          />
          <button type="submit">Search Records</button>
        </form>
      </div>
    </div>
  )
}

export default PatientSearch