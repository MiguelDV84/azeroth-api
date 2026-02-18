const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';
const AUTH_STORAGE_KEY = 'auth:v1';

let authToken = null;

export const setAuthToken = (token) => {
  authToken = token || null;
};

const getAuthToken = () => {
  if (authToken) return authToken;
  try {
    const stored = localStorage.getItem(AUTH_STORAGE_KEY);
    if (!stored) return null;
    const auth = JSON.parse(stored);
    authToken = auth?.token || null;
    return authToken;
  } catch {
    return null;
  }
};

const parseResponse = async (response) => {
  if (response.status === 204) return null;
  const text = await response.text();
  if (!text) return null;
  return JSON.parse(text);
};

const request = async (path, options = {}) => {
  const token = getAuthToken();
  const response = await fetch(`${API_BASE_URL}${path}`, {
    headers: {
      'Content-Type': 'application/json',
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...options.headers,
    },
    ...options,
  });

  const data = await parseResponse(response);
  if (!response.ok) {
    const message = data?.message || data?.error || response.statusText;
    throw new Error(message || 'Error de servidor');
  }
  return data;
};

const getPageContent = (page) => {
  if (!page) return [];
  if (Array.isArray(page)) return page;
  return page.content || [];
};

export const api = {
  login(payload) {
    return request('/api/auth/login', {
      method: 'POST',
      body: JSON.stringify(payload),
    });
  },
  register(payload) {
    return request('/api/auth/register', {
      method: 'POST',
      body: JSON.stringify(payload),
    });
  },
  async getJugadores() {
    const data = await request('/api/jugadores/list?size=100');
    return getPageContent(data);
  },
  getJugadorById(id) {
    return request(`/api/jugadores/${id}`);
  },
  createJugador(payload) {
    return request('/api/jugadores', {
      method: 'POST',
      body: JSON.stringify(payload),
    });
  },
  updateJugador(id, payload) {
    return request(`/api/jugadores/${id}`, {
      method: 'PUT',
      body: JSON.stringify(payload),
    });
  },
  deleteJugador(id) {
    return request(`/api/jugadores/${id}`, {
      method: 'DELETE',
    });
  },
  ganarExperiencia(id, experiencia) {
    return request(`/api/jugadores/experiencia/${id}?experiencia=${experiencia}`, {
      method: 'PUT',
    });
  },
  asignarHermandad(jugadorId, hermandadId) {
    return request(`/api/jugadores/hermandad/${jugadorId}`, {
      method: 'PUT',
      body: JSON.stringify({ hermandadId }),
    });
  },
  removerHermandad(jugadorId) {
    return request(`/api/jugadores/remover-hermandad/${jugadorId}`, {
      method: 'PUT',
    });
  },
  inicializarLogros(jugadorId) {
    return request(`/api/jugadores/inicializar-logros/${jugadorId}`, {
      method: 'PUT',
    });
  },
  actualizarProgreso(jugadorId, logroId) {
    return request(`/api/progreso/actualizar/${jugadorId}/${logroId}`, {
      method: 'PUT',
    });
  },
  async getHermandades() {
    const data = await request('/api/hermandades/list?size=100');
    return getPageContent(data);
  },
  getHermandadById(id) {
    return request(`/api/hermandades/${id}`);
  },
  getHermandadCantidad(id) {
    return request(`/api/hermandades/${id}/cantidad-jugadores`);
  },
  createHermandad(payload) {
    return request('/api/hermandades', {
      method: 'POST',
      body: JSON.stringify(payload),
    });
  },
  updateHermandad(id, payload) {
    return request(`/api/hermandades/${id}`, {
      method: 'PUT',
      body: JSON.stringify(payload),
    });
  },
  deleteHermandad(id) {
    return request(`/api/hermandades/${id}`, {
      method: 'DELETE',
    });
  },
  async getLogros() {
    const data = await request('/api/logros/list?size=100');
    return getPageContent(data);
  },
  getLogroById(id) {
    return request(`/api/logros/${id}`);
  },
  createLogro(payload) {
    return request('/api/logros', {
      method: 'POST',
      body: JSON.stringify(payload),
    });
  },
  updateLogro(id, payload) {
    return request(`/api/logros/${id}`, {
      method: 'PUT',
      body: JSON.stringify(payload),
    });
  },
  deleteLogro(id) {
    return request(`/api/logros/${id}`, {
      method: 'DELETE',
    });
  },
  async getRazas() {
    const data = await request('/api/razas/list?size=100');
    return getPageContent(data);
  },
  getRazaById(id) {
    return request(`/api/razas/${id}`);
  },
  createRaza(payload) {
    return request('/api/razas', {
      method: 'POST',
      body: JSON.stringify(payload),
    });
  },
  updateRaza(id, payload) {
    return request(`/api/razas/${id}`, {
      method: 'PUT',
      body: JSON.stringify(payload),
    });
  },
  deleteRaza(id) {
    return request(`/api/razas/${id}`, {
      method: 'DELETE',
    });
  },
  async getClases() {
    const data = await request('/api/clases/list?size=100');
    return getPageContent(data);
  },
  getClaseById(id) {
    return request(`/api/clases/${id}`);
  },
  async getFacciones() {
    const data = await request('/api/facciones/list?size=20');
    return getPageContent(data);
  },
  getFaccionById(id) {
    return request(`/api/facciones/${id}`);
  },
};
