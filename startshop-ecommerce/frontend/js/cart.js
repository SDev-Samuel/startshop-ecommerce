/**
 * Logica del carrito de compras (cart.html).
 */

async function loadCart() {
  if (!isLoggedIn()) {
    window.location.href = "login.html";
    return;
  }

  const container = document.getElementById("cart-container");
  container.innerHTML = "<p>Cargando tu carrito...</p>";

  try {
    const items = await apiFetch("/cart");

    if (items.length === 0) {
      container.innerHTML = `
        <div class="empty-state">
          Tu carrito esta vacio. <a href="index.html" style="color: var(--accent)">Ver productos</a>
        </div>`;
      return;
    }

    const total = items.reduce((sum, item) => sum + item.subtotal, 0);

    container.innerHTML = `
      <div class="card">
        ${items.map(renderCartItem).join("")}
        <div class="cart-summary">
          <span>Total</span>
          <span>$${total.toFixed(2)}</span>
        </div>
        <button id="checkout-btn" class="btn btn-primary" style="width:100%; margin-top: 1rem;">
          Confirmar compra
        </button>
      </div>
    `;

    document.querySelectorAll(".qty-increase").forEach((btn) =>
      btn.addEventListener("click", () => changeQuantity(btn.dataset.id, 1))
    );
    document.querySelectorAll(".qty-decrease").forEach((btn) =>
      btn.addEventListener("click", () => changeQuantity(btn.dataset.id, -1))
    );
    document.querySelectorAll(".remove-item").forEach((btn) =>
      btn.addEventListener("click", () => removeItem(btn.dataset.id))
    );
    document.getElementById("checkout-btn").addEventListener("click", checkout);
  } catch (err) {
    container.innerHTML = `<div class="empty-state">Error al cargar el carrito: ${err.message}</div>`;
  }
}

function renderCartItem(item) {
  return `
    <div class="cart-item">
      <img src="${item.imageUrl || "img/placeholder.svg"}" onerror="this.src='img/placeholder.svg'">
      <div class="info">
        <h4>${item.productName}</h4>
        <span style="color: var(--muted)">$${Number(item.price).toFixed(2)} c/u</span>
      </div>
      <div class="qty-controls">
        <button class="qty-decrease" data-id="${item.id}" data-qty="${item.quantity}">-</button>
        <span>${item.quantity}</span>
        <button class="qty-increase" data-id="${item.id}" data-qty="${item.quantity}">+</button>
      </div>
      <strong>$${item.subtotal.toFixed(2)}</strong>
      <button class="btn btn-danger remove-item" data-id="${item.id}">Quitar</button>
    </div>
  `;
}

async function changeQuantity(itemId, delta) {
  const button = document.querySelector(`.qty-${delta > 0 ? "increase" : "decrease"}[data-id="${itemId}"]`);
  const currentQty = Number(button.dataset.qty);
  const newQty = currentQty + delta;

  if (newQty < 1) {
    return removeItem(itemId);
  }

  try {
    await apiFetch(`/cart/items/${itemId}?quantity=${newQty}`, { method: "PUT" });
    loadCart();
  } catch (err) {
    alert("No se pudo actualizar la cantidad: " + err.message);
  }
}

async function removeItem(itemId) {
  try {
    await apiFetch(`/cart/items/${itemId}`, { method: "DELETE" });
    loadCart();
  } catch (err) {
    alert("No se pudo quitar el producto: " + err.message);
  }
}

async function checkout() {
  const btn = document.getElementById("checkout-btn");
  btn.disabled = true;
  btn.textContent = "Procesando pago...";

  try {
    await apiFetch("/orders/checkout", { method: "POST" });
    window.location.href = "orders.html";
  } catch (err) {
    alert("No se pudo completar la compra: " + err.message);
    btn.disabled = false;
    btn.textContent = "Confirmar compra";
  }
}

document.addEventListener("DOMContentLoaded", loadCart);
