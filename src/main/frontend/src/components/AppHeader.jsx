import { NavLink } from 'react-router-dom';

const NAV_ITEMS = [
  { id: 'inicio', label: 'Inicio', path: '/' },
  { id: 'forja', label: 'Forja de héroes', path: '/forja' },
  { id: 'viaje', label: 'Viaje del héroe', path: '/viaje' },
  { id: 'hermandades', label: 'Hermandades', path: '/hermandades' },
  { id: 'logros', label: 'Logros', path: '/logros' },
  { id: 'razas', label: 'Archivo de razas', path: '/razas', adminOnly: true },
];

function AppHeader({ isAuthenticated, userLabel, onLogout, isAdmin }) {
  const visibleItems = NAV_ITEMS.filter((item) => !item.adminOnly || isAdmin);

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
            {visibleItems.map((item) => (
              <NavLink
                key={item.id}
                to={item.path}
                end={item.path === '/'}
                className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}
              >
                {item.label}
              </NavLink>
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
