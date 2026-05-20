import { HashRouter, Routes, Route, Navigate } from 'react-router-dom'
import { isAuthenticated } from './api/auth'
import Login from './pages/Login'
import PatientSearch from './pages/PatientSearch'
import MedicalRecordList from './pages/MedicalRecordList'
import MedicalRecordDetail from './pages/MedicalRecordDetail'
import NewMedicalRecord from './pages/NewMedicalRecord'

function PrivateRoute({ children }) {
  return isAuthenticated() ? children : <Navigate to="/login" replace />
}

function App() {
  return (
    <HashRouter>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/" element={<Navigate to="/patients" replace />} />
        <Route path="/patients" element={<PrivateRoute><PatientSearch /></PrivateRoute>} />
        <Route path="/patients/:patientId/records" element={<PrivateRoute><MedicalRecordList /></PrivateRoute>} />
        <Route path="/records/:recordId" element={<PrivateRoute><MedicalRecordDetail /></PrivateRoute>} />
        <Route path="/patients/:patientId/records/new" element={<PrivateRoute><NewMedicalRecord /></PrivateRoute>} />
      </Routes>
    </HashRouter>
  )
}

export default App