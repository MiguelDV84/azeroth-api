import { useState } from 'react';
import { formatEnum } from '../lib/format';

const RAZAS_ENUM = [
  'HUMANO',
  'ORCO',
  'ELFO_NOCHE',
  'ENANO',
  'TAUREN',
  'GNOMO',
  'NO_MUERTO',
  'TROLL',
  'DRAENEI',
  'ELFO_SANGRE',
  'HUARGEN',
];

function RazaArchive({
  races,
  classes,
  factions,
  onLoadRaza,
  onCreateRaza,
  onUpdateRaza,
  onDeleteRaza,
  onRefreshRazas,
}) {
  const [razaId, setRazaId] = useState('');
  const [nombre, setNombre] = useState('');
  const [faccionId, setFaccionId] = useState('');
  const [clasesIds, setClasesIds] = useState([]);
  const [razaDetail, setRazaDetail] = useState(null);
  const [status, setStatus] = useState({ type: '', message: '' });

  const resetStatus = () => setStatus({ type: '', message: '' });

  const loadRaza = async () => {
    resetStatus();
    if (!razaId) return;
    try {
      const data = await onLoadRaza(Number(razaId));
      setRazaDetail(data);
      setNombre(data.nombre || '');
      const faction = factions.find((item) => item.nombre === data.faccion);
      setFaccionId(faction?.id ? faction.id.toString() : '');
      setClasesIds(
        classes
          .filter((clase) => data.clasesDisponibles?.includes(clase.nombre))
          .map((clase) => clase.id.toString())
      );
    } catch (error) {
      setStatus({ type: 'error', message: error.message || 'No se pudo cargar la raza.' });
    }
  };

  const handleCreate = async () => {
    resetStatus();
    if (!nombre || !faccionId || !clasesIds.length) {
      setStatus({ type: 'error', message: 'Completa nombre, facción y clases.' });
      return;
    }
    try {
      await onCreateRaza({
        nombre,
        faccionId: Number(faccionId),
        clasesDisponiblesIds: clasesIds.map(Number),
      });
      setStatus({ type: 'success', message: 'Raza registrada.' });
      await onRefreshRazas();
    } catch (error) {
      setStatus({ type: 'error', message: error.message || 'No se pudo crear la raza.' });
    }
  };

  const handleUpdate = async () => {
    resetStatus();
    if (!razaId) return;
    try {
      await onUpdateRaza(Number(razaId), {
        nombre,
        faccionId: Number(faccionId),
        clasesDisponiblesIds: clasesIds.map(Number),
      });
      setStatus({ type: 'success', message: 'Raza actualizada.' });
      await onRefreshRazas();
      await loadRaza();
    } catch (error) {
      setStatus({ type: 'error', message: error.message || 'No se pudo actualizar la raza.' });
    }
  };

  const handleDelete = async () => {
    resetStatus();
    if (!razaId) return;
    try {
      await onDeleteRaza(Number(razaId));
      setStatus({ type: 'success', message: 'Raza eliminada.' });
      setRazaId('');
      setRazaDetail(null);
      await onRefreshRazas();
    } catch (error) {
      setStatus({ type: 'error', message: error.message || 'No se pudo eliminar la raza.' });
    }
  };

  const toggleClase = (id) => {
    setClasesIds((current) =>
      current.includes(id) ? current.filter((item) => item !== id) : [...current, id]
    );
  };

  return (
    <div className="card raza-card">
      <h3>Archivo de razas</h3>
      <p className="muted">Registra linajes y define sus clases permitidas.</p>

      <div className="form-grid">
        <label className="form-field">
          <span>Raza existente</span>
          <select value={razaId} onChange={(event) => setRazaId(event.target.value)}>
            <option value="">Selecciona una raza</option>
            {races.map((race) => (
              <option key={race.id} value={race.id}>
                {formatEnum(race.nombre)}
              </option>
            ))}
          </select>
        </label>
        <button type="button" onClick={loadRaza} disabled={!razaId}>
          Consultar
        </button>
      </div>

      {status.message ? (
        <div className={`${status.type}-message mt-2`}>{status.message}</div>
      ) : null}

      {razaDetail ? (
        <div className="raza-detail mt-2">
          <h4>{formatEnum(razaDetail.nombre)}</h4>
          <p className="muted">Facción: {formatEnum(razaDetail.faccion)}</p>
        </div>
      ) : null}

      <div className="form-grid mt-2">
        <label className="form-field">
          <span>Nombre (enum)</span>
          <select value={nombre} onChange={(event) => setNombre(event.target.value)}>
            <option value="">Selecciona nombre</option>
            {RAZAS_ENUM.map((raza) => (
              <option key={raza} value={raza}>
                {formatEnum(raza)}
              </option>
            ))}
          </select>
        </label>
        <label className="form-field">
          <span>Facción</span>
          <select value={faccionId} onChange={(event) => setFaccionId(event.target.value)}>
            <option value="">Selecciona facción</option>
            {factions.map((faction) => (
              <option key={faction.id} value={faction.id}>
                {formatEnum(faction.nombre)}
              </option>
            ))}
          </select>
        </label>
      </div>

      <div className="checkbox-grid mt-2">
        {classes.map((clase) => (
          <label key={clase.id} className="checkbox-item">
            <input
              type="checkbox"
              checked={clasesIds.includes(clase.id.toString())}
              onChange={() => toggleClase(clase.id.toString())}
            />
            {formatEnum(clase.nombre)}
          </label>
        ))}
      </div>

      <div className="button-row mt-2">
        <button type="button" onClick={handleCreate}>Registrar raza</button>
        <button type="button" onClick={handleUpdate} disabled={!razaId}>Actualizar</button>
        <button type="button" className="danger" onClick={handleDelete} disabled={!razaId}>
          Eliminar
        </button>
      </div>
    </div>
  );
}

export default RazaArchive;
