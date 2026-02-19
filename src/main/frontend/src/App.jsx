import { useCallback, useEffect, useState } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import AppHeader from './components/AppHeader';
import Section from './components/Section';
import CharacterCreation from './components/CharacterCreation';
import PlayerRoster from './components/PlayerRoster';
import PlayerJourney from './components/PlayerJourney';
import GuildHall from './components/GuildHall';
import AchievementHall from './components/AchievementHall';
import RazaArchive from './components/RazaArchive';
import LoginScreen from './components/LoginScreen';
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

  const HomePage = () => (
    <Section
      title="Bienvenido a Azeroth"
      subtitle="Recorre las vistas para crear héroes, levantar hermandades y alcanzar logros épicos."
    >
      <div className="card intro-card">
        <h3>Tu viaje comienza aquí</h3>
        <p className="muted">
          Desde este refugio puedes forjar personajes, explorar su progreso y registrar nuevas hazañas.
        </p>
      </div>
    </Section>
  );

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
                  <Section
                    title="Forja de héroes"
                    subtitle="Elige tu raza, clase y facción como en las crónicas de Azeroth."
                  >
                    <CharacterCreation
                      races={races}
                      classes={classes}
                      factions={factions}
                      onCreatePlayer={api.createJugador}
                      onRefreshPlayers={refreshPlayers}
                      onLoadRace={api.getRazaById}
                      onLoadClass={api.getClaseById}
                      onLoadFaction={api.getFaccionById}
                    />
                    <PlayerRoster players={players} />
                  </Section>
                }
              />
              <Route
                path="/viaje"
                element={
                  <Section
                    title="Viaje del héroe"
                    subtitle="Profundiza en el progreso y las decisiones de cada campeón."
                  >
                    <PlayerJourney
                      players={players}
                      onGetPlayer={api.getJugadorById}
                      onUpdatePlayer={api.updateJugador}
                      onDeletePlayer={api.deleteJugador}
                      onGainExp={api.ganarExperiencia}
                      onRemoveGuild={api.removerHermandad}
                      onRefreshPlayers={refreshPlayers}
                    />
                  </Section>
                }
              />
              <Route
                path="/hermandades"
                element={
                  <Section
                    title="Hermandades"
                    subtitle="Fundar, consultar y reforzar lazos entre héroes."
                  >
                    <GuildHall
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
                  </Section>
                }
              />
              <Route
                path="/logros"
                element={
                  <Section
                    title="Salón de logros"
                    subtitle="Avanza en el progreso y registra nuevas hazañas."
                  >
                    <AchievementHall
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
                  </Section>
                }
              />
              <Route
                path="/razas"
                element={
                  isAdmin ? (
                    <Section
                      title="Archivo de razas"
                      subtitle="Registra las razas y sus afinidades dentro de Azeroth."
                    >
                      <RazaArchive
                        races={races}
                        classes={classes}
                        factions={factions}
                        onLoadRaza={api.getRazaById}
                        onCreateRaza={api.createRaza}
                        onUpdateRaza={api.updateRaza}
                        onDeleteRaza={api.deleteRaza}
                        onRefreshRazas={refreshRazas}
                      />
                    </Section>
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

