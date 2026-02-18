import { formatEnum, factionTone } from '../lib/format';

function PlayerRoster({ players }) {
  if (!players.length) {
    return (
      <div className="card empty-card">
        <h3>Salón de héroes</h3>
        <p className="muted">Aún no hay héroes registrados. Forja el primero para comenzar.</p>
      </div>
    );
  }

  return (
    <div className="card roster-card">
      <h3>Salón de héroes</h3>
      <div className="roster-grid">
        {players.map((player) => (
          <article key={player.id} className={`roster-item ${factionTone(player.faccion)}`}>
            <div>
              <h4>{player.nombre}</h4>
              <p className="muted">
                {formatEnum(player.raza)} · {formatEnum(player.clase)}
              </p>
            </div>
            <div className="roster-meta">
              <span>Nivel {player.nivel}</span>
              <span>{formatEnum(player.faccion)}</span>
              <span>{player.hermandad || 'Sin hermandad'}</span>
            </div>
          </article>
        ))}
      </div>
    </div>
  );
}

export default PlayerRoster;
