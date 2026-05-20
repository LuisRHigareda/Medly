import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { removeToken } from '../api/auth'
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

  const handleLogout = () => {
    removeToken()
    navigate('/login', { replace: true })
  }

  return (
    <div className="search-container">
      <div className="search-card">
        <div className="search-header">
          <div>
            <h1>Medly</h1>
            <p>Medical Record Service</p>
          </div>
          <button className="logout-btn" onClick={handleLogout}>Log out</button>
        </div>
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