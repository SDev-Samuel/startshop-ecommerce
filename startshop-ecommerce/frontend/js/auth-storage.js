/**
 * Funciones pequenas para leer/guardar la sesion del usuario en localStorage.
 * IMPORTANTE (nota de seguridad para cuando avances): guardar el JWT en localStorage
 * es sencillo para aprender, pero en un proyecto en produccion se prefieren cookies
 * httpOnly para reducir el riesgo de robo de token via XSS. Es un buen siguiente reto.
 */
function saveSession(authResponse) {
  localStorage.setItem("startshop_token", authResponse.token);
  localStorage.setItem(
    "startshop_user",
    JSON.stringify({
      fullName: authResponse.fullName,
      email: authResponse.email,
      role: authResponse.role,
    })
  );
}

function getCurrentUser() {
  const raw = localStorage.getItem("startshop_user");
  return raw ? JSON.parse(raw) : null;
}

function isLoggedIn() {
  return !!localStorage.getItem("startshop_token");
}

function logout() {
  localStorage.removeItem("startshop_token");
  localStorage.removeItem("startshop_user");
  window.location.href = "index.html";
}
