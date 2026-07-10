// Dirección base de la API del backend.
// En GitHub Pages, la interfaz es estática y no puede llamar a localhost.
// Si el backend está corriendo localmente, se usará http://localhost:8080/api.
// Si no, la app mostrará un mensaje claro cuando la API no esté disponible.
const API_BASE = (window.location.hostname === "localhost" || window.location.hostname === "127.0.0.1")
  ? "http://localhost:8080/api"
  : "https://startshop-ecommerce.onrender.com/api";
