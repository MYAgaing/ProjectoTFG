# 📦 Gestión de Productos con Imágenes - Panel de Administrador

## ✅ Funcionalidades Implementadas

Se ha añadido un sistema completo para que los administradores puedan crear productos con imágenes desde el panel de administrador.

### Backend (Spring Boot)

1. **FileStorageService** - Servicio para almacenar y gestionar archivos
   - Guarda imágenes con nombres únicos (UUID)
   - Elimina imágenes antiguas al actualizar/borrar productos
   - Crea automáticamente el directorio de uploads

2. **adminProductoController** - Controlador REST para gestión de productos
   - `GET /admin/productos/categorias` - Obtiene todas las categorías
   - `POST /admin/productos` - Crea producto con imagen (multipart/form-data)
   - `PUT /admin/productos/{id}` - Actualiza producto
   - `DELETE /admin/productos/{id}` - Elimina producto e imagen

3. **FileUploadConfig** - Configuración para servir imágenes estáticamente
   - Las imágenes son accesibles en `/uploads/productos/{filename}`

4. **Configuración de seguridad actualizada**
   - Ruta `/uploads/productos/**` es pública (permitAll)
   - Rutas `/admin/**` requieren rol ADMIN

### Frontend (Angular)

1. **AdminProductoService** - Servicio para comunicación con API
   - Métodos para crear, actualizar y eliminar productos
   - Manejo de FormData para envío de archivos

2. **Componente Admin actualizado**
   - Nueva pestaña "Productos" en el panel
   - Formulario modal para crear productos
   - Preview de imagen antes de subir
   - Validaciones de campos obligatorios
   - Mensajes de éxito/error

## 🚀 Cómo Usar

### 1. Acceder al Panel de Administrador

1. Inicia sesión con una cuenta de administrador
2. Ve a `/admin` en tu navegador
3. Haz clic en la pestaña **"Productos"**

### 2. Crear un Nuevo Producto

1. Haz clic en el botón **"Crear nuevo producto"**
2. Rellena el formulario:
   - **Nombre** (obligatorio): Ej. "iPhone 15 Pro"
   - **Marca** (obligatorio): Ej. "Apple"
   - **Descripción** (opcional): Características del producto
   - **Fecha de lanzamiento** (obligatorio): Selecciona la fecha
   - **Categoría** (obligatorio): Selecciona de la lista
   - **Imagen** (opcional): Haz clic en el área de carga para seleccionar
3. Verás un preview de la imagen seleccionada
4. Haz clic en **"Crear producto"**
5. El producto aparecerá automáticamente en el catálogo público

### 3. Gestión de Imágenes

- **Formatos soportados**: PNG, JPG, JPEG, WEBP
- **Tamaño máximo**: 10MB
- **Almacenamiento**: Las imágenes se guardan en `Tfg-backend/uploads/productos/`
- **Sin imagen**: Si no subes imagen, se usa un placeholder por defecto

## 📁 Estructura de Archivos Creados

### Backend
```
Tfg-backend/
├── src/main/java/com/reviewmeter/tfg/
│   ├── controller/
│   │   └── adminProductoController.java          ← Nuevo controlador
│   ├── service/
│   │   └── FileStorageService.java               ← Nuevo servicio
│   └── config/
│       └── FileUploadConfig.java                 ← Nueva configuración
├── src/main/resources/
│   └── application.properties                    ← Actualizado
└── uploads/productos/                            ← Nueva carpeta
    └── .gitkeep
```

### Frontend
```
Tfg-frontend/
└── src/app/
    ├── Services/
    │   └── admin-producto-service.ts             ← Nuevo servicio
    └── admin/
        ├── admin.ts                              ← Actualizado
        └── admin.html                            ← Actualizado
```

## ⚙️ Configuración

### Variables de Entorno (application.properties)

```properties
# URL del backend (para construir URLs de imágenes)
app.backend-url=${BACKEND_URL:http://localhost:8080}

# Directorio de almacenamiento
file.upload-dir=${UPLOAD_DIR:uploads/productos}

# Tamaño máximo de archivos
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

### Para Producción (Railway)

Añade estas variables de entorno en Railway:
- `BACKEND_URL`: URL de tu backend en producción
- `UPLOAD_DIR`: (opcional) Ruta personalizada para uploads

**⚠️ IMPORTANTE**: En Railway, considera usar un servicio de almacenamiento externo como:
- AWS S3
- Cloudinary
- Google Cloud Storage

Porque el sistema de archivos de Railway es efímero (se reinicia con cada deploy).

## 🔒 Seguridad

- Solo usuarios con rol **ADMIN** pueden crear/editar/eliminar productos
- Las imágenes se validan en el backend
- Los nombres de archivo se generan con UUID para evitar colisiones
- Las rutas de admin están protegidas por JWT

## 🐛 Solución de Problemas

### Error: "No se pudo crear el directorio"
- Verifica que la aplicación tenga permisos de escritura
- La carpeta `uploads/productos/` se crea automáticamente

### Error: "Archivo demasiado grande"
- El límite es 10MB por archivo
- Ajusta `spring.servlet.multipart.max-file-size` si necesitas más

### Las imágenes no se muestran
- Verifica que `app.backend-url` esté configurado correctamente
- Comprueba que la ruta `/uploads/productos/**` sea pública en SecurityConfig

### Error 403 al crear producto
- Verifica que el usuario tenga rol ADMIN
- Comprueba que el token JWT sea válido

## 📝 Próximas Mejoras Sugeridas

1. **Edición de productos existentes** desde el panel
2. **Listado de productos** en la pestaña de productos
3. **Compresión automática** de imágenes
4. **Múltiples imágenes** por producto
5. **Integración con Cloudinary** para producción
6. **Validación de dimensiones** de imagen
7. **Crop/resize** de imágenes en el frontend

## 🎉 ¡Listo!

Ahora los administradores pueden crear productos con imágenes directamente desde el panel de administrador. Las imágenes se almacenan localmente y son accesibles públicamente para mostrarlas en el catálogo.
