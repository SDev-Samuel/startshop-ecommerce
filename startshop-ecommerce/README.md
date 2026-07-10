# StartShop — Ecommerce educativo (reconstrucción)

Este proyecto es una **reconstrucción simplificada** del proyecto original `ecommerce-api`,
pensada para que un estudiante principiante en Spring Boot pueda leerlo, ejecutarlo y
entenderlo de principio a fin. Es una carpeta **completamente nueva e independiente**:
no modifica ni depende del proyecto original.

## 🧠 ¿Qué cambia respecto al proyecto original?

| Aspecto | Proyecto original | Esta reconstrucción | ¿Por qué? |
|---|---|---|---|
| Base de datos | PostgreSQL (requiere Docker) | H2 (un archivo local) | Puedes correr el backend sin instalar ni configurar nada |
| Migraciones | Flyway | Hibernate `ddl-auto=update` | Menos conceptos nuevos para empezar; Flyway es un buen "siguiente paso" |
| Getters/Setters | Lombok (anotaciones que generan código) | Escritos a mano | Puedes leer cada línea sin "magia" oculta |
| Pagos | Stripe real (API keys, webhooks) | Pago simulado | Puedes probar el flujo de compra completo sin crear cuentas externas |
| Frontend | Landing estática sin conexión a la API | HTML + CSS + JS puro **conectado de verdad** | Login, catálogo, carrito y pedidos funcionan end-to-end |
| Docker | Obligatorio para correr la app | Opcional | Puedes desarrollar sin Docker y usarlo solo para "empaquetar" |

Todo lo demás (JWT, Spring Security, arquitectura en capas, endpoints REST) sigue el
mismo espíritu profesional del proyecto original, solo que explicado paso a paso en
comentarios dentro del código.

## 📁 Estructura del proyecto

```
startshop-ecommerce/
├── backend/                        Spring Boot 3 + Java 21
│   ├── pom.xml
│   └── src/main/java/com/startshop/ecommerce/
│       ├── entity/          Las "recetas" de tus datos (User, Product, Order...)
│       ├── repository/      Acceso a la base de datos
│       ├── security/        JWT + configuración de Spring Security
│       ├── dto/             Objetos que viajan entre frontend y backend
│       ├── service/         Lógica de negocio (el "chef")
│       ├── controller/      Endpoints REST (el "mesero")
│       ├── config/          Datos de ejemplo al arrancar
│       └── exception/       Manejo centralizado de errores
├── frontend/                       HTML + CSS + JavaScript puro
│   ├── index.html           Catálogo de productos
│   ├── login.html / register.html
│   ├── cart.html            Carrito de compras
│   ├── orders.html          Historial de pedidos
│   ├── css/styles.css
│   └── js/                  config.js, api.js, auth.js, products.js, cart.js, orders.js
└── docker-compose.yml               Opcional, para "empaquetar" todo junto
```

## ▶️ Cómo ejecutar (sin Docker — recomendado para aprender)

1. **Backend**:
   ```bash
   cd backend
   mvn spring-boot:run
   ```
   Esto levanta la API en `http://localhost:8080`. La base de datos H2 se crea
   automáticamente dentro de `backend/data/`.

2. **Frontend**: abre `frontend/index.html` directamente en tu navegador
   (doble clic, o usa la extensión "Live Server" de VS Code). El JavaScript
   ya está configurado para hablar con `http://localhost:8080/api`.

3. **Consola de la base de datos** (opcional, para ver tus tablas):
   `http://localhost:8080/h2-console` — JDBC URL: `jdbc:h2:file:./data/startshopdb`

## 🐳 Cómo ejecutar con Docker (opcional)

```bash
docker compose up --build
```
- Backend disponible en `http://localhost:8080`
- Frontend disponible en `http://localhost:3000`

## 🔌 Endpoints disponibles

| Método | Endpoint | Protegido | Descripción |
|---|---|---|---|
| POST | `/api/auth/register` | No | Crear cuenta |
| POST | `/api/auth/login` | No | Iniciar sesión (devuelve JWT) |
| GET | `/api/products` | No | Ver catálogo |
| GET | `/api/products/{id}` | No | Ver un producto |
| GET | `/api/cart` | Sí | Ver mi carrito |
| POST | `/api/cart/items` | Sí | Agregar producto al carrito |
| PUT | `/api/cart/items/{id}?quantity=N` | Sí | Cambiar cantidad |
| DELETE | `/api/cart/items/{id}` | Sí | Quitar del carrito |
| POST | `/api/orders/checkout` | Sí | Confirmar compra (pago simulado) |
| GET | `/api/orders` | Sí | Ver mi historial de pedidos |

"Protegido" significa que necesitas enviar el header `Authorization: Bearer <token>`
que obtienes al iniciar sesión (el frontend ya lo hace automáticamente por ti).

## 🚀 Siguientes retos para seguir aprendiendo

Ahora que tienes el proyecto funcionando, estos son buenos siguientes pasos
(pregúntame por cualquiera de ellos y lo desarrollamos juntos, paso a paso):

1. **Panel de administrador**: endpoints para crear/editar/borrar productos (rol `ADMIN`).
2. **Migraciones con Flyway**: reemplazar `ddl-auto=update` por scripts SQL versionados.
3. **Pagos reales con Stripe**: conectar `OrderService.checkout()` con la API de Stripe.
4. **Cambiar a PostgreSQL + Docker**: volver a una base de datos "de producción".
5. **Tests automáticos**: JUnit + Mockito para probar los `Service` sin levantar el servidor.
6. **Paginación y búsqueda** en el catálogo de productos.

## ⚠️ Notas importantes antes de usar en producción

- El JWT se guarda en `localStorage` del navegador — perfecto para aprender, pero
  en un proyecto real se recomienda usar cookies `httpOnly`.
- El CORS está abierto a cualquier origen (`*`) para facilitar las pruebas locales.
  En producción deberías restringirlo al dominio real de tu frontend.
- El `jwt.secret` en `application.properties` es de ejemplo — cámbialo siempre
  en cualquier entorno real.
