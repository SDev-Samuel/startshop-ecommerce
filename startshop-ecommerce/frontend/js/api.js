/**
 * apiFetch: un "envoltorio" (wrapper) sobre fetch() que:
 * 1. Agrega automaticamente el token JWT guardado (si existe) en el header Authorization.
 * 2. Convierte el body a JSON automaticamente.
 * 3. Si la respuesta no es exitosa (status >= 400), lanza un error con el mensaje del backend.
 *
 * Asi, en products.js / cart.js / orders.js no repetimos este codigo una y otra vez.
 */
async function apiFetch(path, options = {}) {
  const token = localStorage.getItem("startshop_token");

  const headers = {
    "Content-Type": "application/json",
    ...(options.headers || {}),
  };

  if (token) {
    headers["Authorization"] = "Bearer " + token;
  }

  try {
    const response = await fetch(API_BASE + path, {
      ...options,
      headers,
    });

    // 204 No Content: no hay body que parsear
    if (response.status === 204) {
      return null;
    }

    const data = await response.json().catch(() => null);

    if (!response.ok) {
      const mensaje = (data && data.message) || "Ocurrio un error inesperado";
      throw new Error(mensaje);
    }

    return data;
  } catch (err) {
    if (err instanceof Error && err.message) {
      throw err;
    }
    throw new Error("No se pudo conectar con la API. Verifica que el backend esté disponible.");
  }

  return data;
}
