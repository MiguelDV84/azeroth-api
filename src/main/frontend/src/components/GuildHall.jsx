import { useState } from 'react';
import { formatEnum, factionTone } from '../lib/format';

const REALMS = ['DUN_MODR', 'SPINNESHATTER', 'ZULJIN'];

function GuildHall({
  guilds,
  factions,
  players,
  onCreateGuild,
  onUpdateGuild,
  onDeleteGuild,
  onGetGuild,
  onGetGuildCount,
  onJoinGuild,
  onRefreshGuilds,
  onRefreshPlayers,
}) {
  const [guildId, setGuildId] = useState('');
  const [guildDetail, setGuildDetail] = useState(null);
  const [guildCount, setGuildCount] = useState(null);
  const [newGuildName, setNewGuildName] = useState('');
  const [newGuildRealm, setNewGuildRealm] = useState('');
  const [newGuildFaction, setNewGuildFaction] = useState('');
  const [joinPlayerId, setJoinPlayerId] = useState('');
  const [status, setStatus] = useState({ type: '', message: '' });

  const resetStatus = () => setStatus({ type: '', message: '' });

  const loadGuild = async () => {
    resetStatus();
    if (!guildId) return;
    try {
      const data = await onGetGuild(Number(guildId));
      setGuildDetail(data);
      setGuildCount(await onGetGuildCount(Number(guildId)));
      setNewGuildName(data.nombre || '');
      setNewGuildRealm(data.reino || '');
      const faction = factions.find((item) => item.nombre === data.faccion);
      setNewGuildFaction(faction?.id ? faction.id.toString() : '');
    } catch (error) {
      setStatus({ type: 'error', message: error.message || 'No se pudo cargar la hermandad.' });
    }
  };

  const handleCreate = async () => {
    resetStatus();
    if (!newGuildName || !newGuildRealm || !newGuildFaction) {
      setStatus({ type: 'error', message: 'Completa los datos para fundar tu hermandad.' });
      return;
    }
    try {
      await onCreateGuild({
        nombre: newGuildName,
        reino: newGuildRealm,
        faccionId: Number(newGuildFaction),
      });
      setStatus({ type: 'success', message: 'Hermandad fundada con honor.' });
      await onRefreshGuilds();
    } catch (error) {
      setStatus({ type: 'error', message: error.message || 'No se pudo crear la hermandad.' });
    }
  };

  const handleUpdate = async () => {
    resetStatus();
    if (!guildId) return;
    try {
      await onUpdateGuild(Number(guildId), {
        nombre: newGuildName,
        reino: newGuildRealm,
        faccionId: Number(newGuildFaction),
      });
      setStatus({ type: 'success', message: 'Hermandad actualizada.' });
      await onRefreshGuilds();
      await loadGuild();
    } catch (error) {
      setStatus({ type: 'error', message: error.message || 'No se pudo actualizar la hermandad.' });
    }
  };

  const handleDelete = async () => {
    resetStatus();
    if (!guildId) return;
    try {
      await onDeleteGuild(Number(guildId));
      setStatus({ type: 'success', message: 'La hermandad ha sido disuelta.' });
      setGuildId('');
      setGuildDetail(null);
      setGuildCount(null);
      await onRefreshGuilds();
    } catch (error) {
      setStatus({ type: 'error', message: error.message || 'No se pudo disolver la hermandad.' });
    }
  };

  const handleJoin = async () => {
    resetStatus();
    if (!joinPlayerId || !guildId) {
      setStatus({ type: 'error', message: 'Selecciona un héroe y una hermandad.' });
      return;
    }
    try {
      await onJoinGuild(Number(joinPlayerId), Number(guildId));
      setStatus({ type: 'success', message: 'El héroe se ha unido a la hermandad.' });
      await onRefreshPlayers();
      await loadGuild();
    } catch (error) {
      setStatus({ type: 'error', message: error.message || 'No se pudo unir a la hermandad.' });
    }
  };

  const factionClass = factionTone(guildDetail?.faccion);

  return (
    <div className="card guild-card">
      <h3>Sala de hermandades</h3>
      <p className="muted">Forja alianzas, edita estandartes y reúne héroes.</p>

      <div className="form-grid">
        <label className="form-field">
          <span>Hermandad</span>
          <select value={guildId} onChange={(event) => setGuildId(event.target.value)}>
            <option value="">Selecciona una hermandad</option>
            {guilds.map((guild) => (
              <option key={guild.idHermandad || guild.nombre} value={guild.idHermandad}>
                {guild.nombre} · {formatEnum(guild.faccion)}
              </option>
            ))}
          </select>
        </label>
        <button type="button" onClick={loadGuild} disabled={!guildId}>
          Consultar
        </button>
      </div>

      {status.message ? (
        <div className={`${status.type}-message mt-2`}>{status.message}</div>
      ) : null}

      <div className="form-grid mt-2">
        <label className="form-field">
          <span>Nombre</span>
          <input value={newGuildName} onChange={(event) => setNewGuildName(event.target.value)} />
        </label>
        <label className="form-field">
          <span>Reino</span>
          <select value={newGuildRealm} onChange={(event) => setNewGuildRealm(event.target.value)}>
            <option value="">Selecciona un reino</option>
            {REALMS.map((realm) => (
              <option key={realm} value={realm}>
                {formatEnum(realm)}
              </option>
            ))}
          </select>
        </label>
        <label className="form-field">
          <span>Facción</span>
          <select value={newGuildFaction} onChange={(event) => setNewGuildFaction(event.target.value)}>
            <option value="">Selecciona facción</option>
            {factions.map((faction) => (
              <option key={faction.id} value={faction.id}>
                {formatEnum(faction.nombre)}
              </option>
            ))}
          </select>
        </label>
      </div>

      <div className="button-row">
        <button type="button" onClick={handleCreate}>Fundar hermandad</button>
        <button type="button" onClick={handleUpdate} disabled={!guildId}>Actualizar</button>
        <button type="button" className="danger" onClick={handleDelete} disabled={!guildId}>
          Disolver
        </button>
      </div>

      {guildDetail ? (
        <div className={`guild-detail ${factionClass}`}>
          <h4>{guildDetail.nombre}</h4>
          <p className="muted">
            {formatEnum(guildDetail.reino)} · {formatEnum(guildDetail.faccion)}
          </p>
          <p className="muted">Héroes en la hermandad: {guildCount ?? '...'}</p>
        </div>
      ) : null}

      <div className="form-grid mt-2">
        <label className="form-field">
          <span>Héroe para unirse</span>
          <select value={joinPlayerId} onChange={(event) => setJoinPlayerId(event.target.value)}>
            <option value="">Selecciona un héroe</option>
            {players.map((player) => (
              <option key={player.id} value={player.id}>
                {player.nombre}
              </option>
            ))}
          </select>
        </label>
        <button type="button" onClick={handleJoin} disabled={!guildId}>
          Unir a hermandad
        </button>
      </div>
    </div>
  );
}

export default GuildHall;
