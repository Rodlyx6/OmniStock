import axios from 'axios'

const transformLargeInteger = (rawData) => {
  if (typeof rawData !== 'string' || rawData.trim() === '') {
    return rawData
  }
  const normalized = rawData.replace(/(:\s*)(-?\d{17,})(\s*[,}])/g, '$1"$2"$3')
  try {
    return JSON.parse(normalized)
  } catch {
    return rawData
  }
}

const http = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  },
  transformResponse: [transformLargeInteger]
})

http.interceptors.response.use(
  (response) => {
    const body = response.data
    if (body && typeof body.code !== 'undefined') {
      if (body.code === 200) {
        return body.data
      }
      return Promise.reject(new Error(body.message || '请求失败'))
    }
    return body
  },
  (error) => {
    const serverMessage = error?.response?.data?.message
    return Promise.reject(new Error(serverMessage || error?.message || '网络异常'))
  }
)

export default http
