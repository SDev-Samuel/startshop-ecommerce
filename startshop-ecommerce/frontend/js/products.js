/**
 * Pinta el catalogo de productos en index.html.
 */

async function loadProducts() {
  const grid = document.getElementById("products-grid");
  grid.innerHTML = "<p>Cargando productos...</p>";

  try {
    const products = await apiFetch("/products");

    if (products.length === 0) {
      grid.innerHTML = '<div class="empty-state">Aun no hay productos disponibles.</div>';
      return;
    }

    grid.innerHTML = products.map(renderProductCard).join("");

    // Enganchamos el evento de cada boton "Agregar al carrito"
    document.querySelectorAll(".add-to-cart-btn").forEach((btn) => {
      btn.addEventListener("click", () => addToCart(btn.dataset.productId, btn));
    });
  } catch (err) {
    grid.innerHTML = `<div class="empty-state">No se pudieron cargar los productos: ${err.message}</div>`;
  }
}

function renderProductCard(product) {
  const sinStock = product.stock <= 0;
  return `
    <article class="card product-card">
      <img src="${product.imageUrl || "img/placeholder.svg"}" alt="${product.name}"
           onerror="this.src='img/placeholder.svg'">
      <span class="category-tag">${product.category || "General"}</span>
      <h3>${product.name}</h3>
      <p>${product.description || ""}</p>
      <span class="price">$${Number(product.price).toFixed(2)}</span>
      <button class="btn btn-primary add-to-cart-btn"
              data-product-id="${product.id}"
              ${sinStock ? "disabled" : ""}>
        ${sinStock ? "Sin stock" : "Agregar al carrito"}
      </button>
    </article>
  `;
}

async function addToCart(productId, button) {
  if (!isLoggedIn()) {
    window.location.href = "login.html";
    return;
  }

  const originalText = button.textContent;
  button.disabled = true;
  button.textContent = "Agregando...";

  try {
    await apiFetch("/cart/items", {
      method: "POST",
      body: JSON.stringify({ productId: Number(productId), quantity: 1 }),
    });
    button.textContent = "Agregado ✓";
    setTimeout(() => {
      button.textContent = originalText;
      button.disabled = false;
    }, 1200);
  } catch (err) {
    alert("No se pudo agregar el producto: " + err.message);
    button.textContent = originalText;
    button.disabled = false;
  }
}

document.addEventListener("DOMContentLoaded", loadProducts);
