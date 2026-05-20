import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { login, saveToken } from '../api/auth'
import './Login.css'

function Login() {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()

  const handleSubmit = (e) => {
    e.preventDefault()
    setLoading(true)
    setError(null)

    login(email, password)
      .then((res) => {
        saveToken(res.data['jwt-token'])
        navigate('/patients')
      })
      .catch(() => setError('Invalid credentials. Please try again.'))
      .finally(() => setLoading(false))
  }

  return (
    <div className="login-container">
      <div className="login-card">
        <h1>Medly</h1>
        <p>Medical Record Service</p>
        <form onSubmit={handleSubmit} className="login-form">
          <label>Email</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="doctor@medly.com"
            required
          />
          <label>Password</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="••••••••"
            required
          />
          {error && <p className="login-error">{error}</p>}
          <button type="submit" disabled={loading}>
            {loading ? 'Logging in...' : 'Login'}
          </button>
        </form>
      </div>
    </div>
  )
}

export default Login