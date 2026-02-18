const NAV_ITEMS = [
  { id: 'inicio', label: 'Inicio' },
  { id: 'forja', label: 'Forja de héroes' },
  { id: 'viaje', label: 'Viaje del héroe' },
  { id: 'hermandades', label: 'Hermandades' },
  { id: 'logros', label: 'Logros' },
  { id: 'razas', label: 'Archivo de razas' },
];

function AppHeader({ activeView, onNavigate, isAuthenticated, userLabel, onLogout }) {
  return (
    <header className="app-header">
      <div className="app-header-inner container">
        <div className="title-block">
          <p className="title-eyebrow">Crónicas de Azeroth</p>
          <h1 className="app-title">Forja de Héroes</h1>
          <p className="title-subtitle">
            Un viaje inmersivo por Azeroth: crea tu campeón, únete a una hermandad y avanza en los logros.
          </p>
        </div>
        {isAuthenticated ? (
          <nav className="app-nav" aria-label="Navegación principal">
            {NAV_ITEMS.map((item) => (
              <button
                key={item.id}
                type="button"
                className={`nav-link ${activeView === item.id ? 'active' : ''}`}
                onClick={() => onNavigate(item.id)}
              >
                {item.label}
              </button>
            ))}
          </nav>
        ) : null}

        {isAuthenticated ? (
          <div className="header-actions">
            <span className="user-pill">{userLabel || 'Viajero'}</span>
            <button type="button" className="nav-link" onClick={onLogout}>
              Cerrar sesión
            </button>
          </div>
        ) : null}
      </div>
    </header>
  );
}

export default AppHeader;
