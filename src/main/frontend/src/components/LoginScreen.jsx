import { useState } from 'react';

const EMPTY_LOGIN_FORM = { username: '', password: '' };
const EMPTY_REGISTER_FORM = {
  username: '',
  email: '',
  password: '',
  confirmPassword: '',
  role: 'USER',
};

function LoginScreen({ onLogin, onRegister }) {
  const [mode, setMode] = useState('login');
  const [loginForm, setLoginForm] = useState(EMPTY_LOGIN_FORM);
  const [registerForm, setRegisterForm] = useState(EMPTY_REGISTER_FORM);
  const [error, setError] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleLoginChange = (event) => {
    const { name, value } = event.target;
    setLoginForm((current) => ({ ...current, [name]: value }));
  };

  const handleRegisterChange = (event) => {
    const { name, value } = event.target;
    setRegisterForm((current) => ({ ...current, [name]: value }));
  };

  const handleSubmitLogin = async (event) => {
    event.preventDefault();
    setError('');
    setIsSubmitting(true);

    try {
      await onLogin(loginForm);
      setLoginForm(EMPTY_LOGIN_FORM);
    } catch (submitError) {
      setError(submitError.message || 'No se pudo iniciar sesión.');
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleSubmitRegister = async (event) => {
    event.preventDefault();
    setError('');

    if (registerForm.password !== registerForm.confirmPassword) {
      setError('Las contraseñas no coinciden.');
      return;
    }

    setIsSubmitting(true);

    try {
      const { confirmPassword, ...payload } = registerForm;
      await onRegister(payload);
      setRegisterForm(EMPTY_REGISTER_FORM);
    } catch (submitError) {
      setError(submitError.message || 'No se pudo completar el registro.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <section className="login-layout">
      <div className="login-card card">
        <header className="login-header">
          <p className="title-eyebrow">Acceso al archivo sagrado</p>
          <h2>{mode === 'login' ? 'Inicia sesión' : 'Crea tu cuenta'}</h2>
          <p className="muted">
            {mode === 'login'
              ? 'Usa tus credenciales para acceder a las gestas de Azeroth y administrar tu hermandad.'
              : 'Registra tu héroe y accede a las crónicas de Azeroth en segundos.'}
          </p>
        </header>

        <div className="login-switch">
          <button
            type="button"
            className={`nav-link ${mode === 'login' ? 'active' : ''}`}
            onClick={() => {
              setMode('login');
              setError('');
            }}
          >
            Entrar
          </button>
          <button
            type="button"
            className={`nav-link ${mode === 'register' ? 'active' : ''}`}
            onClick={() => {
              setMode('register');
              setError('');
            }}
          >
            Registrarse
          </button>
        </div>

        {mode === 'login' ? (
          <form className="login-form" onSubmit={handleSubmitLogin}>
            <label className="form-field" htmlFor="login-username">
              Usuario
              <input
                id="login-username"
                name="username"
                type="text"
                placeholder="Tu nombre de héroe"
                value={loginForm.username}
                onChange={handleLoginChange}
                autoComplete="username"
                required
              />
            </label>

            <label className="form-field" htmlFor="login-password">
              Contraseña
              <input
                id="login-password"
                name="password"
                type="password"
                placeholder="Tu contraseña"
                value={loginForm.password}
                onChange={handleLoginChange}
                autoComplete="current-password"
                required
              />
            </label>

            {error ? <div className="error-message">{error}</div> : null}

            <button type="submit" disabled={isSubmitting}>
              {isSubmitting ? 'Accediendo...' : 'Entrar'}
            </button>
          </form>
        ) : (
          <form className="login-form" onSubmit={handleSubmitRegister}>
            <label className="form-field" htmlFor="register-username">
              Usuario
              <input
                id="register-username"
                name="username"
                type="text"
                placeholder="Nombre de héroe"
                value={registerForm.username}
                onChange={handleRegisterChange}
                autoComplete="username"
                required
              />
            </label>

            <label className="form-field" htmlFor="register-email">
              Email
              <input
                id="register-email"
                name="email"
                type="email"
                placeholder="correo@azeroth.com"
                value={registerForm.email}
                onChange={handleRegisterChange}
                autoComplete="email"
                required
              />
            </label>

            <label className="form-field" htmlFor="register-role">
              Rol
              <select
                id="register-role"
                name="role"
                value={registerForm.role}
                onChange={handleRegisterChange}
                required
              >
                <option value="USER">Aventurero</option>
                <option value="ADMIN">Guardián</option>
              </select>
            </label>

            <label className="form-field" htmlFor="register-password">
              Contraseña
              <input
                id="register-password"
                name="password"
                type="password"
                placeholder="Mínimo 6 caracteres"
                value={registerForm.password}
                onChange={handleRegisterChange}
                autoComplete="new-password"
                required
              />
            </label>

            <label className="form-field" htmlFor="register-confirm-password">
              Confirmar contraseña
              <input
                id="register-confirm-password"
                name="confirmPassword"
                type="password"
                placeholder="Repite tu contraseña"
                value={registerForm.confirmPassword}
                onChange={handleRegisterChange}
                autoComplete="new-password"
                required
              />
            </label>

            {error ? <div className="error-message">{error}</div> : null}

            <button type="submit" disabled={isSubmitting}>
              {isSubmitting ? 'Registrando...' : 'Crear cuenta'}
            </button>
          </form>
        )}
      </div>

      <aside className="login-side card">
        <h3>Guía rápida</h3>
        <ul className="login-tips">
          <li>Accede al registro de héroes y logros.</li>
          <li>Gestiona las hermandades de tu facción.</li>
          <li>Forja nuevas leyendas con cada sesión.</li>
        </ul>
        <p className="muted">
          Puedes crear tu cuenta como aventurero o guardián según la política de tu hermandad.
        </p>
      </aside>
    </section>
  );
}

export default LoginScreen;
