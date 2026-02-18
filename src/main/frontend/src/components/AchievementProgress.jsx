import { useState } from 'react';
import { formatEnum } from '../lib/format';

const isCompleted = (estado) => estado === 'COMPLETADO';

function AchievementProgress({ players, onInitLogros, onAdvanceLogro }) {
  const [playerId, setPlayerId] = useState('');
  const [logros, setLogros] = useState([]);
  const [status, setStatus] = useState({ type: '', message: '' });
  const [isLoading, setIsLoading] = useState(false);

  const resetStatus = () => setStatus({ type: '', message: '' });

  const handleLoadLogros = async () => {
    resetStatus();
    if (!playerId) {
      setStatus({ type: 'error', message: 'Selecciona un héroe para cargar sus logros.' });
      return;
    }

    try {
      setIsLoading(true);
      const data = await onInitLogros(Number(playerId));
      setLogros(data.logros || []);
      setStatus({ type: 'success', message: 'Logros cargados correctamente.' });
    } catch (error) {
      setStatus({ type: 'error', message: error.message || 'No se pudieron cargar los logros.' });
    } finally {
      setIsLoading(false);
    }
  };

  const handleAdvance = async (logroId) => {
    resetStatus();
    try {
      const data = await onAdvanceLogro(Number(playerId), logroId);
      setLogros((current) =>
        current.map((logro) =>
          logro.id === data.logroId
            ? {
                ...logro,
                valorActual: data.valorActual,
                estado: data.estado,
                fechaCompletado: data.fechaCompletado,
              }
            : logro
        )
      );
    } catch (error) {
      setStatus({ type: 'error', message: error.message || 'No se pudo avanzar el logro.' });
    }
  };

  return (
    <div className="card logros-card">
      <div className="logros-header">
        <div>
          <h3>Salón de logros</h3>
          <p className="muted">Avanza en tu progreso y reclama la gloria.</p>
        </div>
        <div className="logros-controls">
          <select value={playerId} onChange={(event) => setPlayerId(event.target.value)}>
            <option value="">Selecciona un héroe</option>
            {players.map((player) => (
              <option key={player.id} value={player.id}>
                {player.nombre}
              </option>
            ))}
          </select>
          <button type="button" onClick={handleLoadLogros} disabled={isLoading}>
            {isLoading ? 'Cargando...' : 'Cargar logros'}
          </button>
        </div>
      </div>

      {status.message ? (
        <div className={`${status.type}-message mt-2`}>{status.message}</div>
      ) : null}

      {!logros.length ? (
        <p className="muted mt-2">Selecciona un héroe para ver sus objetivos.</p>
      ) : (
        <div className="logros-list">
          {logros.map((logro) => {
            const progress = logro.valorObjetivo
              ? Math.min((logro.valorActual / logro.valorObjetivo) * 100, 100)
              : 0;

            return (
              <div key={logro.id} className={`logro-item ${isCompleted(logro.estado) ? 'completed' : ''}`}>
                <div className="logro-info">
                  <h4>{logro.titulo}</h4>
                  <p className="muted">{logro.descripcion}</p>
                  <div className="logro-meta">
                    <span>{formatEnum(logro.estado)}</span>
                    <span>
                      {logro.valorActual}/{logro.valorObjetivo}
                    </span>
                    <span>{logro.puntosDeLogro} pts</span>
                  </div>
                </div>
                <div className="logro-actions">
                  <div className="progress-bar">
                    <div className="progress-fill" style={{ width: `${progress}%` }} />
                  </div>
                  <button
                    type="button"
                    disabled={isCompleted(logro.estado) || !playerId}
                    onClick={() => handleAdvance(logro.id)}
                  >
                    {isCompleted(logro.estado) ? 'Completado' : 'Avanzar'}
                  </button>
                </div>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
}

export default AchievementProgress;
