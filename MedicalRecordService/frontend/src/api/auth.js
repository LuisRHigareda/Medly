import axios from 'axios'

const GATEWAY_URL = 'http://localhost:8080'

export const login = (email, password) =>
  axios.post(`${GATEWAY_URL}/api/login`, { email, password })

export const saveToken = (token) => localStorage.setItem('jwt_token', token)

export const getToken = () => localStorage.getItem('jwt_token')

export const removeToken = () => localStorage.removeItem('jwt_token')

export const isAuthenticated = () => !!getToken()