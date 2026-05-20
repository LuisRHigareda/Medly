import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import {
  getMedicalRecordById,
  getDiagnosesByRecord,
  getAllergiesByRecord,
  getPrescriptionsByRecord,
  getProceduresByRecord,
  getPrescriptionPdf,
  getMedications,
  createDiagnosis,
  createAllergy,
  createPrescription,
  createProcedure,
} from '../api/medicalRecords'
import './MedicalRecordDetail.css'

function MedicalRecordDetail() {
  const { recordId } = useParams()
  const navigate = useNavigate()

  const [record, setRecord] = useState(null)
  const [diagnoses, setDiagnoses] = useState([])
  const [allergies, setAllergies] = useState([])
  const [prescriptions, setPrescriptions] = useState([])
  const [procedures, setProcedures] = useState([])
  const [medicationList, setMedicationList] = useState([])
  const [medicationMap, setMedicationMap] = useState({})
  const [loading, setLoading] = useState(true)
  const [openForm, setOpenForm] = useState(null)

  const loadAll = () => {
    return Promise.all([
      getMedicalRecordById(recordId),
      getDiagnosesByRecord(recordId),
      getAllergiesByRecord(recordId),
      getPrescriptionsByRecord(recordId),
      getProceduresByRecord(recordId),
      getMedications(),
    ]).then(([rec, diag, allerg, presc, proc, meds]) => {
      setRecord(rec.data)
      setDiagnoses(diag.data)
      setAllergies(allerg.data)
      setPrescriptions(presc.data)
      setProcedures(proc.data)
      setMedicationList(meds.data)
      const map = {}
      meds.data.forEach((m) => { map[m.id] = m.name })
      setMedicationMap(map)
    })
  }

  useEffect(() => {
    loadAll().finally(() => setLoading(false))
  }, [recordId])

  const handleDownloadPdf = () => {
    getPrescriptionPdf(recordId).then((res) => {
      const url = window.URL.createObjectURL(new Blob([res.data], { type: 'application/pdf' }))
      const link = document.createElement('a')
      link.href = url
      link.setAttribute('download', `prescription_${recordId}.pdf`)
      document.body.appendChild(link)
      link.click()
      link.remove()
    })
  }

  const toggleForm = (name) => setOpenForm(openForm === name ? null : name)

  if (loading) return <p className="msg">Loading...</p>
  if (!record) return <p className="error-msg">Record not found.</p>

  return (
    <div className="detail-container">
      <div className="detail-header">
        <button onClick={() => navigate(-1)} className="btn-back">← Back</button>
        <h2>Record #{record.id}</h2>
        <button onClick={handleDownloadPdf} className="btn-pdf">Download PDF</button>
      </div>

      <div className="section">
        <div className="section-header">Patient Information</div>
        <div className="section-body">
          <div className="info-row"><span className="info-label">Patient ID:</span><span>{record.patient_id}</span></div>
          <div className="info-row"><span className="info-label">Doctor ID:</span><span>{record.doctor_id}</span></div>
          <div className="info-row"><span className="info-label">Blood type:</span><span>{record.blood_type_id ?? 'N/A'}</span></div>
          <div className="info-row"><span className="info-label">Weight:</span><span>{record.weight ? `${record.weight} kg` : 'N/A'}</span></div>
          <div className="info-row"><span className="info-label">Height:</span><span>{record.height ? `${record.height} m` : 'N/A'}</span></div>
          <div className="info-row"><span className="info-label">Notes:</span><span>{record.notes ?? 'None'}</span></div>
        </div>
      </div>

      <Section title="Diagnoses" onAdd={() => toggleForm('diagnosis')}>
        {openForm === 'diagnosis' && (
          <DiagnosisForm recordId={recordId} onSave={() => { setOpenForm(null); loadAll() }} onCancel={() => setOpenForm(null)} />
        )}
        {diagnoses.length === 0 && openForm !== 'diagnosis'
          ? <p className="empty-msg">None registered.</p>
          : diagnoses.map((d) => (
            <p key={d.id} className="list-item">- {d.name} — {d.diagnosed_date?.slice(0, 10) ?? 'N/A'}</p>
          ))}
      </Section>

      <Section title="Allergies" onAdd={() => toggleForm('allergy')}>
        {openForm === 'allergy' && (
          <AllergyForm recordId={recordId} onSave={() => { setOpenForm(null); loadAll() }} onCancel={() => setOpenForm(null)} />
        )}
        {allergies.length === 0 && openForm !== 'allergy'
          ? <p className="empty-msg">None registered.</p>
          : allergies.map((a) => (
            <p key={a.id} className="list-item">- {a.allergen} | Reaction: {a.reaction ?? 'N/A'} | Severity: {a.severity ?? 'N/A'}</p>
          ))}
      </Section>

      <Section title="Prescribed Medications" onAdd={() => toggleForm('prescription')}>
        {openForm === 'prescription' && (
          <PrescriptionForm recordId={recordId} medications={medicationList} onSave={() => { setOpenForm(null); loadAll() }} onCancel={() => setOpenForm(null)} />
        )}
        {prescriptions.length === 0 && openForm !== 'prescription'
          ? <p className="empty-msg">None registered.</p>
          : prescriptions.map((p) => (
            <p key={p.id} className="list-item">
              - {medicationMap[p.medication_id] ?? `Medication ID ${p.medication_id}`} | Dose: {p.dose ?? 'N/A'} mg | {p.start_date} to {p.end_date ?? 'Ongoing'}
            </p>
          ))}
      </Section>

      <Section title="Medical Procedures" onAdd={() => toggleForm('procedure')}>
        {openForm === 'procedure' && (
          <ProcedureForm recordId={recordId} onSave={() => { setOpenForm(null); loadAll() }} onCancel={() => setOpenForm(null)} />
        )}
        {procedures.length === 0 && openForm !== 'procedure'
          ? <p className="empty-msg">None registered.</p>
          : procedures.map((pr) => (
            <p key={pr.id} className="list-item">- {pr.name} — {pr.date}{pr.notes ? ` | ${pr.notes}` : ''}</p>
          ))}
      </Section>
    </div>
  )
}

function Section({ title, onAdd, children }) {
  return (
    <div className="section">
      <div className="section-header">
        <span>{title}</span>
        {onAdd && <button onClick={onAdd} className="btn-add">+ Add</button>}
      </div>
      <div className="section-body">{children}</div>
    </div>
  )
}

function DiagnosisForm({ recordId, onSave, onCancel }) {
  const [form, setForm] = useState({ name: '', diagnosed_date: '', status_id: '' })
  const handle = (e) => setForm({ ...form, [e.target.name]: e.target.value })
  const submit = (e) => {
    e.preventDefault()
    createDiagnosis({
      medical_record_id: parseInt(recordId),
      name: form.name,
      diagnosed_date: form.diagnosed_date ? `${form.diagnosed_date}T00:00:00` : undefined,
      status_id: form.status_id ? parseInt(form.status_id) : undefined,
    }).then(onSave)
  }
  return (
    <form onSubmit={submit} className="inline-form">
      <input name="name" placeholder="Diagnosis name *" value={form.name} onChange={handle} required />
      <input name="diagnosed_date" type="date" value={form.diagnosed_date} onChange={handle} />
      <select name="status_id" value={form.status_id} onChange={handle}>
        <option value="">Status (optional)</option>
        <option value="1">ACTIVE</option>
        <option value="2">RESOLVED</option>
        <option value="3">CHRONIC</option>
        <option value="4">IN_TREATMENT</option>
      </select>
      <div className="form-actions">
        <button type="submit" className="btn-save">Save</button>
        <button type="button" onClick={onCancel} className="btn-cancel">Cancel</button>
      </div>
    </form>
  )
}

function AllergyForm({ recordId, onSave, onCancel }) {
  const [form, setForm] = useState({ allergen: '', reaction: '', severity: '' })
  const handle = (e) => setForm({ ...form, [e.target.name]: e.target.value })
  const submit = (e) => {
    e.preventDefault()
    createAllergy({
      medical_record_id: parseInt(recordId),
      allergen: form.allergen,
      reaction: form.reaction || null,
      severity: form.severity || null,
    }).then(onSave)
  }
  return (
    <form onSubmit={submit} className="inline-form">
      <input name="allergen" placeholder="Allergen *" value={form.allergen} onChange={handle} required />
      <input name="reaction" placeholder="Reaction" value={form.reaction} onChange={handle} />
      <input name="severity" placeholder="Severity (mild/moderate/severe)" value={form.severity} onChange={handle} />
      <div className="form-actions">
        <button type="submit" className="btn-save">Save</button>
        <button type="button" onClick={onCancel} className="btn-cancel">Cancel</button>
      </div>
    </form>
  )
}

function PrescriptionForm({ recordId, medications, onSave, onCancel }) {
  const [form, setForm] = useState({ medication_id: '', dose: '', frequency: '', start_date: '', end_date: '' })
  const handle = (e) => setForm({ ...form, [e.target.name]: e.target.value })
  const submit = (e) => {
    e.preventDefault()
    createPrescription({
      medical_record_id: parseInt(recordId),
      medication_id: parseInt(form.medication_id),
      dose: form.dose ? parseFloat(form.dose) : null,
      frequency: form.frequency ? parseInt(form.frequency) : null,
      start_date: form.start_date,
      end_date: form.end_date || null,
    }).then(onSave)
  }
  return (
    <form onSubmit={submit} className="inline-form">
      <select name="medication_id" value={form.medication_id} onChange={handle} required>
        <option value="">Select medication *</option>
        {medications.map((m) => (
          <option key={m.id} value={m.id}>{m.name}</option>
        ))}
      </select>
      <input name="dose" type="number" step="0.1" placeholder="Dose (mg)" value={form.dose} onChange={handle} />
      <input name="frequency" type="number" placeholder="Frequency (x/day)" value={form.frequency} onChange={handle} />
      <input name="start_date" type="date" value={form.start_date} onChange={handle} required />
      <input name="end_date" type="date" value={form.end_date} onChange={handle} />
      <div className="form-actions">
        <button type="submit" className="btn-save">Save</button>
        <button type="button" onClick={onCancel} className="btn-cancel">Cancel</button>
      </div>
    </form>
  )
}

function ProcedureForm({ recordId, onSave, onCancel }) {
  const [form, setForm] = useState({ name: '', date: '', notes: '' })
  const handle = (e) => setForm({ ...form, [e.target.name]: e.target.value })
  const submit = (e) => {
    e.preventDefault()
    createProcedure({
      medical_record_id: parseInt(recordId),
      name: form.name,
      date: form.date,
      notes: form.notes || null,
    }).then(onSave)
  }
  return (
    <form onSubmit={submit} className="inline-form">
      <input name="name" placeholder="Procedure name *" value={form.name} onChange={handle} required />
      <input name="date" type="date" value={form.date} onChange={handle} required />
      <input name="notes" placeholder="Notes" value={form.notes} onChange={handle} />
      <div className="form-actions">
        <button type="submit" className="btn-save">Save</button>
        <button type="button" onClick={onCancel} className="btn-cancel">Cancel</button>
      </div>
    </form>
  )
}

export default MedicalRecordDetail