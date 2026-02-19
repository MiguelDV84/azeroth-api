import { useCallback, useEffect, useState } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import AppHeader from './components/AppHeader';
import LoginScreen from './components/LoginScreen';
import HomePage from './pages/HomePage';
import ForjaPage from './pages/ForjaPage';
import ViajePage from './pages/ViajePage';
import HermandadesPage from './pages/HermandadesPage';
import LogrosPage from './pages/LogrosPage';
import RazasPage from './pages/RazasPage';
import { api, setAuthToken } from './lib/api';
import './App.css';

const AUTH_STORAGE_KEY = 'auth:v1';

const loadStoredAuth = () => {
  try {
    const stored = localStorage.getItem(AUTH_STORAGE_KEY);
    return stored ? JSON.parse(stored) : null;
  } catch {
    return null;
  }
};

const saveStoredAuth = (authData) => {
  try {
    localStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify(authData));
  } catch {
    // Ignore storage errors
  }
};

const clearStoredAuth = () => {
  try {
    localStorage.removeItem(AUTH_STORAGE_KEY);
  } catch {
    // Ignore storage errors
  }
};

function App() {
  const [players, setPlayers] = useState([]);
  const [guilds, setGuilds] = useState([]);
  const [races, setRaces] = useState([]);
  const [classes, setClasses] = useState([]);
  const [factions, setFactions] = useState([]);
  const [logros, setLogros] = useState([]);
  const [status, setStatus] = useState({ type: '', message: '' });
  const [isLoading, setIsLoading] = useState(false);
  const [auth, setAuth] = useState(() => loadStoredAuth());

  const isAuthenticated = Boolean(auth?.token);
  const isAdmin = auth?.role === 'ADMIN';

  useEffect(() => {
    setAuthToken(auth?.token || null);
  }, [auth?.token]);

  const refreshPlayers = useCallback(async () => {
    const data = await api.getJugadores();
    setPlayers(data);
  }, []);

  const refreshGuilds = useCallback(async () => {
    const data = await api.getHermandades();
    setGuilds(data);
  }, []);

  const refreshRazas = useCallback(async () => {
    const data = await api.getRazas();
    setRaces(data);
  }, []);

  const refreshLogros = useCallback(async () => {
    const data = await api.getLogros();
    setLogros(data);
  }, []);

  useEffect(() => {
    if (!isAuthenticated) {
      setIsLoading(false);
      return;
    }

    const loadData = async () => {
      try {
        setIsLoading(true);
        const [playersData, guildsData, racesData, classesData, factionsData, logrosData] =
          await Promise.all([
            api.getJugadores(),
            api.getHermandades(),
            api.getRazas(),
            api.getClases(),
            api.getFacciones(),
            api.getLogros(),
          ]);
        setPlayers(playersData);
        setGuilds(guildsData);
        setRaces(racesData);
        setClasses(classesData);
        setFactions(factionsData);
        setLogros(logrosData);
      } catch (error) {
        setStatus({ type: 'error', message: error.message || 'No se pudo cargar la crónica.' });
      } finally {
        setIsLoading(false);
      }
    };

    loadData();
  }, [isAuthenticated]);

  const handleLogin = async (credentials) => {
    const response = await api.login(credentials);
    const authData = {
      token: response.token,
      username: response.username,
      email: response.email,
      role: response.role,
    };
    saveStoredAuth(authData);
    setAuth(authData);
    setStatus({ type: 'success', message: 'Has iniciado sesión.' });
  };

  const handleRegister = async (payload) => {
    const response = await api.register(payload);
    const authData = {
      token: response.token,
      username: response.username,
      email: response.email,
      role: response.role,
    };
    saveStoredAuth(authData);
    setAuth(authData);
    setStatus({ type: 'success', message: 'Registro completado.' });
  };

  const handleLogout = () => {
    clearStoredAuth();
    setAuthToken(null);
    setAuth(null);
    setPlayers([]);
    setGuilds([]);
    setRaces([]);
    setClasses([]);
    setFactions([]);
    setLogros([]);
    setStatus({ type: 'success', message: 'Sesión cerrada.' });
  };

  return (
    <BrowserRouter>
      <div className="app">
        <AppHeader
          isAuthenticated={isAuthenticated}
          userLabel={auth?.username}
          onLogout={handleLogout}
          isAdmin={isAdmin}
        />

        <main className="app-content container">
          {status.message ? (
            <div className={`${status.type}-message mb-2`}>{status.message}</div>
          ) : null}

          {!isAuthenticated ? (
            <LoginScreen onLogin={handleLogin} onRegister={handleRegister} />
          ) : isLoading ? (
            <div className="loading" aria-label="Cargando" />
          ) : (
            <Routes>
              <Route path="/" element={<HomePage />} />
              <Route
                path="/forja"
                element={
                  <ForjaPage
                    races={races}
                    classes={classes}
                    factions={factions}
                    players={players}
                    onCreatePlayer={api.createJugador}
                    onRefreshPlayers={refreshPlayers}
                    onLoadRace={api.getRazaById}
                    onLoadClass={api.getClaseById}
                    onLoadFaction={api.getFaccionById}
                  />
                }
              />
              <Route
                path="/viaje"
                element={
                  <ViajePage
                    players={players}
                    onGetPlayer={api.getJugadorById}
                    onUpdatePlayer={api.updateJugador}
                    onDeletePlayer={api.deleteJugador}
                    onGainExp={api.ganarExperiencia}
                    onRemoveGuild={api.removerHermandad}
                    onRefreshPlayers={refreshPlayers}
                  />
                }
              />
              <Route
                path="/hermandades"
                element={
                  <HermandadesPage
                    guilds={guilds}
                    factions={factions}
                    players={players}
                    onCreateGuild={api.createHermandad}
                    onUpdateGuild={api.updateHermandad}
                    onDeleteGuild={api.deleteHermandad}
                    onGetGuild={api.getHermandadById}
                    onGetGuildCount={api.getHermandadCantidad}
                    onJoinGuild={api.asignarHermandad}
                    onRefreshGuilds={refreshGuilds}
                    onRefreshPlayers={refreshPlayers}
                    canDisband={isAdmin}
                  />
                }
              />
              <Route
                path="/logros"
                element={
                  <LogrosPage
                    players={players}
                    logros={logros}
                    onInitLogros={api.inicializarLogros}
                    onLoadLogros={api.getLogrosJugador}
                    onAdvanceLogro={api.actualizarProgreso}
                    onLoadLogro={api.getLogroById}
                    onCreateLogro={api.createLogro}
                    onUpdateLogro={api.updateLogro}
                    onDeleteLogro={api.deleteLogro}
                    onRefreshLogros={refreshLogros}
                    isAdmin={isAdmin}
                  />
                }
              />
              <Route
                path="/razas"
                element={
                  isAdmin ? (
                    <RazasPage
                      races={races}
                      classes={classes}
                      factions={factions}
                      onLoadRaza={api.getRazaById}
                      onCreateRaza={api.createRaza}
                      onUpdateRaza={api.updateRaza}
                      onDeleteRaza={api.deleteRaza}
                      onRefreshRazas={refreshRazas}
                    />
                  ) : (
                    <Navigate to="/" replace />
                  )
                }
              />
              <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
          )}
        </main>
      </div>
    </BrowserRouter>
  );
}

export default App;

