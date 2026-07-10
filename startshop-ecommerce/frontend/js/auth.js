/**
 * Logica de los formularios de login y registro.
 * Se incluye solo en login.html y register.html.
 */

function showAlert(elementId, message, type = "error") {
  const el = document.getElementById(elementId);
  el.textContent = message;
  el.className = "alert alert-" + type;
  el.style.display = "block";
}

function hideAlert(elementId) {
  const el = document.getElementById(elementId);
  el.style.display = "none";
}

async function handleLogin(event) {
  event.preventDefault();
  hideAlert("auth-alert");

  const email = document.getElementById("email").value.trim();
  const password = document.getElementById("password").value;
  const button = document.getElementById("submit-btn");

  button.disabled = true;
  button.textContent = "Ingresando...";

  try {
    const response = await apiFetch("/auth/login", {
      method: "POST",
      body: JSON.stringify({ email, password }),
    });
    saveSession(response);
    window.location.href = "index.html";
  } catch (err) {
    showAlert("auth-alert", err.message);
    button.disabled = false;
    button.textContent = "Iniciar sesion";
  }
}

async function handleRegister(event) {
  event.preventDefault();
  hideAlert("auth-alert");

  const fullName = document.getElementById("fullName").value.trim();
  const email = document.getElementById("email").value.trim();
  const password = document.getElementById("password").value;
  const button = document.getElementById("submit-btn");

  button.disabled = true;
  button.textContent = "Creando cuenta...";

  try {
    const response = await apiFetch("/auth/register", {
      method: "POST",
      body: JSON.stringify({ fullName, email, password }),
    });
    saveSession(response);
    window.location.href = "index.html";
  } catch (err) {
    showAlert("auth-alert", err.message);
    button.disabled = false;
    button.textContent = "Crear cuenta";
  }
}
