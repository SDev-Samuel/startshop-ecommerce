/**
 * Pinta la barra de navegacion segun si el usuario esta logueado o no.
 * Se llama en TODAS las paginas (index, login, register, cart, orders).
 */
function renderNav() {
  const nav = document.getElementById("nav-links");
  if (!nav) return;

  if (isLoggedIn()) {
    const user = getCurrentUser();
    nav.innerHTML = `
      <a href="index.html">Productos</a>
      <a href="cart.html">Carrito</a>
      <a href="orders.html">Mis pedidos</a>
      <span style="color: var(--muted)">Hola, ${user.fullName.split(" ")[0]}</span>
      <a href="#" id="logout-link">Cerrar sesion</a>
    `;
    document.getElementById("logout-link").addEventListener("click", (e) => {
      e.preventDefault();
      logout();
    });
  } else {
    nav.innerHTML = `
      <a href="index.html">Productos</a>
      <a href="login.html">Iniciar sesion</a>
      <a href="register.html">Crear cuenta</a>
    `;
  }
}

document.addEventListener("DOMContentLoaded", renderNav);
