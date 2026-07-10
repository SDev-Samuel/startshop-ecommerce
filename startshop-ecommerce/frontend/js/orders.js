/**
 * Historial de pedidos (orders.html).
 */

async function loadOrders() {
  if (!isLoggedIn()) {
    window.location.href = "login.html";
    return;
  }

  const container = document.getElementById("orders-container");
  container.innerHTML = "<p>Cargando tus pedidos...</p>";

  try {
    const orders = await apiFetch("/orders");

    if (orders.length === 0) {
      container.innerHTML = `
        <div class="empty-state">
          Aun no tienes pedidos. <a href="index.html" style="color: var(--accent)">Ir a comprar</a>
        </div>`;
      return;
    }

    container.innerHTML = orders.map(renderOrderCard).join("");
  } catch (err) {
    container.innerHTML = `<div class="empty-state">Error al cargar tus pedidos: ${err.message}</div>`;
  }
}

function renderOrderCard(order) {
  const fecha = new Date(order.createdAt).toLocaleString("es-CO", {
    dateStyle: "medium",
    timeStyle: "short",
  });

  const itemsHtml = order.items
    .map(
      (item) => `
      <div class="order-item-row">
        <span>${item.quantity} x ${item.productName}</span>
        <span>$${item.subtotal.toFixed(2)}</span>
      </div>`
    )
    .join("");

  return `
    <div class="card order-card">
      <div class="order-header">
        <div>
          <strong>Pedido #${order.id}</strong>
          <div style="color: var(--muted); font-size: 0.85rem">${fecha}</div>
        </div>
        <span class="status-badge status-${order.status}">${order.status}</span>
      </div>
      ${itemsHtml}
      <div class="cart-summary" style="font-size: 1rem; margin-top: 0.6rem;">
        <span>Total</span>
        <span>$${Number(order.total).toFixed(2)}</span>
      </div>
    </div>
  `;
}

document.addEventListener("DOMContentLoaded", loadOrders);
