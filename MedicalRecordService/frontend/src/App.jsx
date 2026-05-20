import { HashRouter, Routes, Route, Navigate } from 'react-router-dom'
import PatientSearch from './pages/PatientSearch'
import MedicalRecordList from './pages/MedicalRecordList'
import MedicalRecordDetail from './pages/MedicalRecordDetail'
import NewMedicalRecord from './pages/NewMedicalRecord'

function App() {
  return (
    <HashRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/patients" replace />} />
        <Route path="/patients" element={<PatientSearch />} />
        <Route path="/patients/:patientId/records" element={<MedicalRecordList />} />
        <Route path="/records/:recordId" element={<MedicalRecordDetail />} />
        <Route path="/patients/:patientId/records/new" element={<NewMedicalRecord />} />
      </Routes>
    </HashRouter>
  )
}

export default App
