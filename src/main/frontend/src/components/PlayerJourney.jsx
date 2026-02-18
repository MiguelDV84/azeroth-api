import { useState } from 'react';
import { formatEnum, factionTone } from '../lib/format';

function PlayerJourney({
  players,
  onGetPlayer,
  onUpdatePlayer,
  onDeletePlayer,
  onGainExp,
  onRemoveGuild,
  onRefreshPlayers,
}) {
  const [playerId, setPlayerId] = useState('');
  const [playerDetail, setPlayerDetail] = useState(null);
  const [newName, setNewName] = useState('');
  const [exp, setExp] = useState('100');
  const [status, setStatus] = useState({ type: '', message: '' });
  const [isLoading, setIsLoading] = useState(false);

  const resetStatus = () => setStatus({ type: '', message: '' });

  const loadPlayer = async () => {
    resetStatus();
    if (!playerId) return;
    try {
      setIsLoading(true);
      const data = await onGetPlayer(Number(playerId));
      setPlayerDetail(data);
      setNewName(data.nombre || '');
    } catch (error) {
      setStatus({ type: 'error', message: error.message || 'No se pudo cargar el héroe.' });
    } finally {
      setIsLoading(false);
    }
  };

  const handleRename = async () => {
    resetStatus();
    if (!playerId || !newName) {
      setStatus({ type: 'error', message: 'Introduce un nuevo nombre.' });
      return;
    }
    try {
      await onUpdatePlayer(Number(playerId), { nombre: newName });
      setStatus({ type: 'success', message: 'El héroe ha sido renombrado.' });
      await onRefreshPlayers();
      await loadPlayer();
    } catch (error) {
      setStatus({ type: 'error', message: error.message || 'No se pudo renombrar.' });
    }
  };

  const handleDelete = async () => {
    resetStatus();
    if (!playerId) return;
    try {
      await onDeletePlayer(Number(playerId));
      setStatus({ type: 'success', message: 'El héroe ha partido de las crónicas.' });
      setPlayerId('');
      setPlayerDetail(null);
      await onRefreshPlayers();
    } catch (error) {
      setStatus({ type: 'error', message: error.message || 'No se pudo eliminar.' });
    }
  };

  const handleExp = async () => {
    resetStatus();
    if (!playerId || !exp) return;
    try {
      await onGainExp(Number(playerId), exp);
      setStatus({ type: 'success', message: 'Experiencia otorgada.' });
      await loadPlayer();
      await onRefreshPlayers();
    } catch (error) {
      setStatus({ type: 'error', message: error.message || 'No se pudo otorgar experiencia.' });
    }
  };

  const handleRemoveGuild = async () => {
    resetStatus();
    if (!playerId) return;
    try {
      await onRemoveGuild(Number(playerId));
      setStatus({ type: 'success', message: 'El héroe ha abandonado la hermandad.' });
      await loadPlayer();
      await onRefreshPlayers();
    } catch (error) {
      setStatus({ type: 'error', message: error.message || 'No se pudo abandonar la hermandad.' });
    }
  };

  const factionClass = factionTone(playerDetail?.faccion);

  return (
    <div className="card journey-card">
      <h3>Viaje del héroe</h3>
      <p className="muted">Consulta, renombra o fortalece a tus campeones.</p>

      <div className="form-grid">
        <label className="form-field">
          <span>Héroe</span>
          <select value={playerId} onChange={(event) => setPlayerId(event.target.value)}>
            <option value="">Selecciona un héroe</option>
            {players.map((player) => (
              <option key={player.id} value={player.id}>
                {player.nombre}
              </option>
            ))}
          </select>
        </label>
        <button type="button" onClick={loadPlayer} disabled={!playerId || isLoading}>
          {isLoading ? 'Consultando...' : 'Consultar'}
        </button>
      </div>

      {status.message ? (
        <div className={`${status.type}-message mt-2`}>{status.message}</div>
      ) : null}

      {playerDetail ? (
        <div className={`journey-detail ${factionClass}`}>
          <h4>{playerDetail.nombre}</h4>
          <p className="muted">
            {formatEnum(playerDetail.raza)} · {formatEnum(playerDetail.clase)} ·{' '}
            {formatEnum(playerDetail.faccion)}
          </p>
          <div className="journey-stats">
            <span>Nivel {playerDetail.nivel}</span>
            <span>Experiencia: {playerDetail.experiencia}</span>
            <span>Siguiente nivel: {playerDetail.experienciaParaProximoNivel}</span>
            <span>Hermandad: {playerDetail.hermandad || 'Sin hermandad'}</span>
          </div>

          <div className="journey-actions">
            <label className="form-field">
              <span>Nuevo nombre</span>
              <input value={newName} onChange={(event) => setNewName(event.target.value)} />
            </label>
            <button type="button" onClick={handleRename}>Renombrar</button>

            <label className="form-field">
              <span>Experiencia otorgada</span>
              <input
                type="number"
                value={exp}
                onChange={(event) => setExp(event.target.value)}
              />
            </label>
            <button type="button" onClick={handleExp}>Otorgar experiencia</button>

            <button type="button" onClick={handleRemoveGuild}>
              Abandonar hermandad
            </button>
            <button type="button" className="danger" onClick={handleDelete}>
              Retirar héroe
            </button>
          </div>
        </div>
      ) : null}
    </div>
  );
}

export default PlayerJourney;
