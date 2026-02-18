import { useState } from 'react';
import AchievementProgress from './AchievementProgress';
import { formatEnum } from '../lib/format';

function AchievementHall({
  players,
  logros,
  onInitLogros,
  onAdvanceLogro,
  onLoadLogro,
  onCreateLogro,
  onUpdateLogro,
  onDeleteLogro,
  onRefreshLogros,
}) {
  const [logroId, setLogroId] = useState('');
  const [logroDetail, setLogroDetail] = useState(null);
  const [titulo, setTitulo] = useState('');
  const [descripcion, setDescripcion] = useState('');
  const [puntos, setPuntos] = useState('5');
  const [objetivo, setObjetivo] = useState('10');
  const [status, setStatus] = useState({ type: '', message: '' });

  const resetStatus = () => setStatus({ type: '', message: '' });

  const loadLogro = async () => {
    resetStatus();
    if (!logroId) return;
    try {
      const data = await onLoadLogro(Number(logroId));
      setLogroDetail(data);
      setTitulo(data.titulo || '');
      setDescripcion(data.descripcion || '');
      setPuntos(data.puntosDeLogro?.toString() || '');
      setObjetivo(data.valorObjetivo?.toString() || '');
    } catch (error) {
      setStatus({ type: 'error', message: error.message || 'No se pudo cargar el logro.' });
    }
  };

  const handleCreate = async () => {
    resetStatus();
    try {
      await onCreateLogro({
        titulo,
        descripcion,
        puntosDeLogro: Number(puntos),
        valorObjetivo: Number(objetivo),
      });
      setStatus({ type: 'success', message: 'Nuevo logro añadido a las crónicas.' });
      await onRefreshLogros();
    } catch (error) {
      setStatus({ type: 'error', message: error.message || 'No se pudo crear el logro.' });
    }
  };

  const handleUpdate = async () => {
    resetStatus();
    if (!logroId) return;
    try {
      await onUpdateLogro(Number(logroId), {
        titulo,
        descripcion,
        puntosDeLogro: Number(puntos),
        valorObjetivo: Number(objetivo),
      });
      setStatus({ type: 'success', message: 'Logro actualizado.' });
      await onRefreshLogros();
      await loadLogro();
    } catch (error) {
      setStatus({ type: 'error', message: error.message || 'No se pudo actualizar el logro.' });
    }
  };

  const handleDelete = async () => {
    resetStatus();
    if (!logroId) return;
    try {
      await onDeleteLogro(Number(logroId));
      setStatus({ type: 'success', message: 'Logro retirado del tablón.' });
      setLogroId('');
      setLogroDetail(null);
      await onRefreshLogros();
    } catch (error) {
      setStatus({ type: 'error', message: error.message || 'No se pudo eliminar el logro.' });
    }
  };

  return (
    <div className="stack">
      <AchievementProgress
        players={players}
        onInitLogros={onInitLogros}
        onAdvanceLogro={onAdvanceLogro}
      />

      <div className="card logros-library">
        <h3>Tablón de logros</h3>
        <p className="muted">Consulta y registra nuevas hazañas para Azeroth.</p>

        <div className="form-grid">
          <label className="form-field">
            <span>Logro</span>
            <select value={logroId} onChange={(event) => setLogroId(event.target.value)}>
              <option value="">Selecciona un logro</option>
              {logros.map((logro) => (
                <option key={logro.id} value={logro.id}>
                  {logro.titulo}
                </option>
              ))}
            </select>
          </label>
          <button type="button" onClick={loadLogro} disabled={!logroId}>
            Consultar
          </button>
        </div>

        {status.message ? (
          <div className={`${status.type}-message mt-2`}>{status.message}</div>
        ) : null}

        {logroDetail ? (
          <div className="logro-detail mt-2">
            <h4>{logroDetail.titulo}</h4>
            <p className="muted">{logroDetail.descripcion}</p>
            <p className="muted">
              Estado: {formatEnum(logroDetail.estado)} · Objetivo {logroDetail.valorObjetivo}
            </p>
          </div>
        ) : null}

        <div className="form-grid mt-2">
          <label className="form-field">
            <span>Título</span>
            <input value={titulo} onChange={(event) => setTitulo(event.target.value)} />
          </label>
          <label className="form-field">
            <span>Descripción</span>
            <input value={descripcion} onChange={(event) => setDescripcion(event.target.value)} />
          </label>
          <label className="form-field">
            <span>Puntos</span>
            <input type="number" value={puntos} onChange={(event) => setPuntos(event.target.value)} />
          </label>
          <label className="form-field">
            <span>Valor objetivo</span>
            <input type="number" value={objetivo} onChange={(event) => setObjetivo(event.target.value)} />
          </label>
        </div>

        <div className="button-row">
          <button type="button" onClick={handleCreate}>Registrar logro</button>
          <button type="button" onClick={handleUpdate} disabled={!logroId}>Actualizar</button>
          <button type="button" className="danger" onClick={handleDelete} disabled={!logroId}>
            Eliminar
          </button>
        </div>
      </div>
    </div>
  );
}

export default AchievementHall;
