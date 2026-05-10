# Requirements Document

## Introduction

Este documento describe los requisitos para dos nuevas funcionalidades de la plataforma **Reviewmeter**:

1. **Sistema de integridad de reseñas (anti-fraude)**: Mecanismos para detectar y prevenir el abuso de multicuentas, garantizando que las reseñas publicadas sean verídicas y representativas de la experiencia real de los usuarios con los productos.

2. **Panel de estadísticas**: Visualización de métricas de rendimiento tanto para el perfil de usuario (actividad del revisor) como para los productos (rendimiento comercial), orientado especialmente a las empresas que gestionan productos en la plataforma.

---

## Glossary

- **Reviewmeter**: La plataforma de reseñas de productos objeto de este documento.
- **Sistema**: El backend de Reviewmeter implementado en Spring Boot.
- **Frontend**: La aplicación Angular que consume la API del Sistema.
- **Usuario**: Persona registrada en Reviewmeter con entidad `Usuario` (idUsuario, nombre, email, password, estado, fechaRegistro, rol).
- **Resena**: Valoración publicada por un Usuario sobre un Producto, con campos: idResena, titulo, comentario, puntuacion (1–5), fecha, usuario, producto.
- **Producto**: Artículo registrado en la plataforma con campos: idProducto, nombre, descripcion, marca, fechaLanzamiento, valoracion, imageUrl, categoria.
- **Empresa**: Usuario con rol de empresa que gestiona uno o más Productos en la plataforma.
- **Detector_Fraude**: Componente del Sistema responsable de analizar señales de riesgo en las reseñas entrantes.
- **Gestor_Sanciones**: Componente del Sistema responsable de aplicar y registrar las acciones sobre reseñas o cuentas sospechosas.
- **Servicio_Estadisticas**: Componente del Sistema que agrega y expone métricas de reseñas y productos.
- **Panel_Estadisticas**: Sección del Frontend que visualiza las métricas devueltas por el Servicio_Estadisticas.
- **Señal_Riesgo**: Indicador calculado por el Detector_Fraude que refleja la probabilidad de que una reseña sea fraudulenta (valores: "ninguno", "bajo", "medio", "alto").
- **Puntuacion_Confianza**: Valor numérico entre 0 y 100 asignado a cada Resena que refleja su nivel de fiabilidad.
- **Reseña_Sospechosa**: Resena cuya Puntuacion_Confianza es inferior a 40.
- **Reseña_Verificada**: Resena cuya Puntuacion_Confianza es igual o superior a 70 y que no ha sido marcada como Reseña_Sospechosa.
- **Ventana_Temporal**: Período de 60 minutos rodante usado como referencia para detectar patrones de abuso por velocidad.
- **JWT**: Token de autenticación JSON Web Token ya implementado en el Sistema.

---

## Requirements

### Requirement 1: Una reseña por usuario por producto

**User Story:** As a usuario de Reviewmeter, I want que el sistema impida publicar más de una reseña por producto, so that las valoraciones reflejen experiencias únicas y no se inflen artificialmente.

#### Acceptance Criteria

1. WHEN un Usuario autenticado intenta crear una Resena sobre un Producto, THE Sistema SHALL verificar si ese Usuario ya tiene una Resena publicada para ese Producto.
2. IF el Usuario ya tiene una Resena publicada para ese Producto, THEN THE Sistema SHALL rechazar la solicitud e informar al usuario con un mensaje de error indicando que ya existe una reseña duplicada para ese producto.
3. THE Sistema SHALL aplicar esta restricción independientemente del estado de la cuenta del Usuario.
4. WHEN un Usuario elimina definitivamente su Resena de un Producto, THE Sistema SHALL permitir que ese Usuario publique una nueva Resena para ese mismo Producto.
5. IF una Resena de un Usuario para un Producto está en estado suspendido, oculto o rechazado (pero no eliminada definitivamente), THEN THE Sistema SHALL seguir considerando esa Resena como "publicada" a efectos de la restricción de unicidad.

---

### Requirement 2: Detección de patrones de abuso por velocidad

**User Story:** As a administrador de Reviewmeter, I want que el sistema detecte cuando un usuario publica reseñas a una velocidad anormalmente alta, so that se puedan identificar comportamientos automatizados o coordinados.

#### Acceptance Criteria

1. WHEN un Usuario autenticado intenta crear una Resena, THE Detector_Fraude SHALL contar el número de Resenas publicadas por ese Usuario en la Ventana_Temporal anterior al momento de la solicitud, excluyendo la Resena que se está evaluando.
2. IF el número de Resenas publicadas por el Usuario en la Ventana_Temporal es igual o superior a 5, THEN THE Detector_Fraude SHALL asignar a la nueva Resena una Señal_Riesgo de nivel "alto".
3. IF el número de Resenas publicadas por el Usuario en la Ventana_Temporal está entre 3 y 4 inclusive, THEN THE Detector_Fraude SHALL asignar a la nueva Resena una Señal_Riesgo de nivel "medio".
4. IF el número de Resenas publicadas por el Usuario en la Ventana_Temporal es igual a 1 o 2, THEN THE Detector_Fraude SHALL asignar a la nueva Resena una Señal_Riesgo de nivel "bajo".
5. IF el Usuario no tiene ninguna Resena publicada en la Ventana_Temporal (contador igual a 0), THEN THE Detector_Fraude SHALL asignar a la nueva Resena una Señal_Riesgo de nivel "ninguno", sin modificar la Puntuacion_Confianza por este factor.
6. WHEN el Detector_Fraude calcula la Señal_Riesgo, THE Sistema SHALL registrar la Señal_Riesgo calculada junto con la Resena en la base de datos de forma atómica.
7. IF la persistencia de la Señal_Riesgo falla, THEN THE Sistema SHALL revertir la creación de la Resena y notificar al usuario con un mensaje de error genérico.

---

### Requirement 3: Cálculo de puntuación de confianza

**User Story:** As a plataforma Reviewmeter, I want asignar una puntuación de confianza a cada reseña basada en múltiples señales, so that los usuarios puedan distinguir reseñas fiables de sospechosas.

#### Acceptance Criteria

1. WHEN se crea una nueva Resena, THE Detector_Fraude SHALL calcular la Puntuacion_Confianza de esa Resena combinando las siguientes señales: antigüedad de la cuenta del Usuario (cuentas con menos de 7 días desde fechaRegistro reducen la puntuación en 30 puntos), nivel de Señal_Riesgo por velocidad (nivel "alto" reduce 40 puntos, nivel "medio" reduce 20 puntos, nivel "bajo" o "ninguno" no reduce puntos), y longitud del comentario (comentarios con menos de 20 caracteres reducen la puntuación en 15 puntos).
2. THE Detector_Fraude SHALL inicializar la Puntuacion_Confianza en 100 antes de aplicar las reducciones.
3. THE Detector_Fraude SHALL establecer la Puntuacion_Confianza mínima en 0 y máxima en 100, sin valores fuera de ese rango.
4. WHEN el Detector_Fraude completa el cálculo de la Puntuacion_Confianza, THE Sistema SHALL almacenar el valor calculado en la entidad Resena.
5. IF la Puntuacion_Confianza de una Resena es inferior a 40, THEN THE Sistema SHALL marcar esa Resena como Reseña_Sospechosa y no incluirla en las consultas públicas de reseñas. Una Resena marcada como Reseña_Sospechosa no podrá alcanzar el estado de Reseña_Verificada de forma automática.
6. IF la Puntuacion_Confianza de una Resena es igual o superior a 70 y la Resena no está marcada como Reseña_Sospechosa, THEN THE Sistema SHALL marcar esa Resena como Reseña_Verificada y THE Frontend SHALL mostrar un indicador visual de verificación visible para cualquier Usuario que visualice esa Resena.
7. WHEN una Resena alcanza el estado de Reseña_Verificada, THE Sistema SHALL mantener ese estado aunque la Puntuacion_Confianza sea recalculada posteriormente con un valor inferior a 70.
8. WHEN la Señal_Riesgo de una Resena es actualizada después de su creación, THE Sistema SHALL recalcular la Puntuacion_Confianza de esa Resena aplicando las mismas reglas del criterio 1.
9. IF la Puntuacion_Confianza recalculada de una Reseña_Sospechosa es igual o superior a 40, THEN THE Sistema SHALL desmarcar esa Resena como Reseña_Sospechosa y hacerla visible en las consultas públicas.

---

### Requirement 4: Moderación manual de reseñas sospechosas

**User Story:** As a administrador de Reviewmeter, I want revisar manualmente las reseñas marcadas como sospechosas, so that pueda decidir si deben publicarse, eliminarse o mantenerse ocultas.

#### Acceptance Criteria

1. THE Sistema SHALL exponer un endpoint REST `GET /api/admin/resenas/sospechosas` que devuelva todas las Resenas con Puntuacion_Confianza inferior a 40, accesible únicamente para Usuarios con rol "ADMIN". WHEN no existen Resenas con Puntuacion_Confianza inferior a 40, THE Sistema SHALL responder con HTTP 200 y una lista vacía.
2. WHEN un administrador aprueba una Reseña_Sospechosa mediante `PUT /api/admin/resenas/{id}/aprobar`, THE Gestor_Sanciones SHALL actualizar la Puntuacion_Confianza de esa Resena a 70, establecer su estado a "aprobada" y hacerla visible en las listas públicas.
3. WHEN un administrador rechaza una Reseña_Sospechosa mediante `PUT /api/admin/resenas/{id}/rechazar`, THE Gestor_Sanciones SHALL marcar la Resena con estado "rechazada" y excluirla permanentemente de las listas públicas. Este cambio de estado es irreversible a través de la API de moderación.
4. IF un Usuario sin rol "ADMIN" intenta acceder a los endpoints de moderación, THEN THE Sistema SHALL responder con el código HTTP 403.
5. THE Sistema SHALL registrar en un log de auditoría la acción de aprobación o rechazo, incluyendo el identificador del administrador, el identificador de la Resena y la marca de tiempo de la acción en formato ISO 8601 UTC.
6. IF el `{id}` de la Resena no corresponde a ninguna Resena existente, THEN THE Sistema SHALL responder con el código HTTP 404.
7. IF un administrador intenta aprobar o rechazar una Resena que no está en estado "sospechosa", THEN THE Sistema SHALL responder con el código HTTP 409 indicando que la acción no es aplicable al estado actual de la Resena.

---

### Requirement 5: Suspensión automática de cuentas con abuso reiterado

**User Story:** As a administrador de Reviewmeter, I want que el sistema suspenda automáticamente las cuentas que acumulen múltiples reseñas rechazadas, so that se reduzca la carga de moderación manual.

#### Acceptance Criteria

1. WHEN el número acumulado de Resenas con estado "rechazada" de un Usuario alcanza 3 y el estado actual del Usuario no es `false`, THE Gestor_Sanciones SHALL establecer el campo `estado` del Usuario a `false` (suspendido).
2. WHEN un Usuario suspendido intenta autenticarse, THE Sistema SHALL rechazar la solicitud y devolver un mensaje de error indicando suspensión por incumplimiento de las normas de la comunidad.
3. THE Gestor_Sanciones SHALL registrar en el log de auditoría cada suspensión automática, incluyendo el identificador del Usuario, el motivo de la suspensión y la marca de tiempo.
4. IF un administrador ejecuta la acción de reactivación sobre un Usuario suspendido, THEN THE Gestor_Sanciones SHALL establecer el campo `estado` del Usuario a `true` y reiniciar el contador de reseñas rechazadas a 0.
5. IF un Usuario reactivado acumula de nuevo 3 Resenas con estado "rechazada", THEN THE Gestor_Sanciones SHALL volver a suspender la cuenta aplicando las mismas reglas del criterio 1.

---

### Requirement 6: Indicadores visuales de fiabilidad en el Frontend

**User Story:** As a visitante de Reviewmeter, I want ver indicadores visuales que me indiquen si una reseña es verificada o sospechosa, so that pueda tomar decisiones de compra más informadas.

#### Acceptance Criteria

1. WHEN el Frontend renderiza una Resena con Puntuacion_Confianza igual o superior a 70, THE Frontend SHALL mostrar un distintivo con la etiqueta "Verificada" junto al nombre del autor.
2. WHEN el Frontend renderiza una Resena con Puntuacion_Confianza entre 40 y 69 inclusive, THE Frontend SHALL mostrar la Resena sin ningún distintivo adicional.
3. IF una Resena tiene estado "rechazada" o Puntuacion_Confianza inferior a 40, THEN esa Resena no será visible para ningún visitante en las listas públicas.
4. WHEN el Usuario autenticado es el autor de una Reseña_Sospechosa propia, THE Frontend SHALL mostrar un aviso "Tu reseña está pendiente de revisión" en su perfil de usuario. WHEN esa Resena deja de cumplir la condición de sospechosa o rechazada, THE Frontend SHALL eliminar el aviso.
5. IF una Resena no tiene valor de Puntuacion_Confianza (campo nulo o ausente), THEN THE Frontend SHALL tratar esa Resena como si tuviera Puntuacion_Confianza igual a 0 a efectos de visibilidad y distintivos.

---

### Requirement 7: Estadísticas de actividad del usuario revisor

**User Story:** As a usuario de Reviewmeter, I want ver estadísticas detalladas de mi actividad como revisor en mi perfil, so that pueda conocer mi contribución a la plataforma y el impacto de mis reseñas.

#### Acceptance Criteria

1. THE Servicio_Estadisticas SHALL exponer un endpoint REST `GET /api/estadisticas/usuario/{id}` que devuelva las siguientes métricas del Usuario: total de Resenas publicadas visibles, puntuacion media (escala 1.0–5.0, redondeada a 1 decimal) de todas sus Resenas con Puntuacion_Confianza ≥ 40, distribución de puntuaciones (número de Resenas con puntuacion 1, 2, 3, 4 y 5), número de Resenas publicadas en los últimos 30 días naturales contados desde el instante de la petición, y nombre de la Categoria con más Resenas del Usuario (en caso de empate, se selecciona la categoría cuya Resena más reciente sea la más reciente).
2. WHEN el Usuario autenticado accede a su propio perfil, THE Frontend SHALL mostrar las métricas del criterio 1 en el Panel_Estadisticas dentro de la pestaña "Estadísticas" del perfil.
3. THE Servicio_Estadisticas SHALL calcular la puntuacion media únicamente sobre Resenas con Puntuacion_Confianza igual o superior a 40. IF ninguna Resena del Usuario cumple ese umbral, THEN THE Servicio_Estadisticas SHALL devolver `puntuacion_media` como `null`.
4. IF el Usuario no tiene ninguna Resena publicada (total de Resenas publicadas igual a 0, incluyendo las ocultas), THEN THE Servicio_Estadisticas SHALL devolver todos los valores numéricos como 0, `puntuacion_media` como `null` y la categoría más reseñada como `null`.
5. IF el `{id}` no corresponde a ningún Usuario registrado, THEN THE Servicio_Estadisticas SHALL responder con el código HTTP 404.
6. IF el Servicio_Estadisticas devuelve un error o no responde, THEN THE Frontend SHALL mostrar un mensaje de error en el Panel_Estadisticas y mantener visible el resto del perfil del Usuario.

---

### Requirement 8: Estadísticas de rendimiento de producto para empresas

**User Story:** As a empresa registrada en Reviewmeter, I want ver estadísticas detalladas del rendimiento de mis productos, so that pueda evaluar la satisfacción de los clientes y tomar decisiones de mejora.

#### Acceptance Criteria

1. THE Servicio_Estadisticas SHALL exponer un endpoint REST `GET /api/estadisticas/producto/{id}` que devuelva las siguientes métricas del Producto que le pertenece: total de Resenas visibles, puntuacion media redondeada a 1 decimal, distribución de puntuaciones (número de Resenas con puntuacion 1, 2, 3, 4 y 5), evolución mensual con número de Resenas y puntuacion media por mes para los 6 meses calendario completos anteriores al mes en curso, y porcentaje de Resenas Verificadas sobre el total de Resenas visibles redondeado a 1 decimal.
2. WHEN una Empresa autenticada accede a la página de detalle de un Producto que gestiona, THE Frontend SHALL mostrar las métricas del criterio 1 en el Panel_Estadisticas de ese Producto.
3. THE Servicio_Estadisticas SHALL incluir únicamente Resenas con Puntuacion_Confianza igual o superior a 40 en el cálculo de la puntuacion media, la distribución de puntuaciones, la evolución mensual y el porcentaje de Resenas Verificadas.
4. IF el Producto no tiene Resenas visibles con Puntuacion_Confianza ≥ 40, THEN THE Servicio_Estadisticas SHALL devolver todos los valores numéricos como 0, el porcentaje de Verificadas como 0.0 y la lista de evolución mensual como una lista vacía.
5. THE Servicio_Estadisticas SHALL devolver los datos de evolución mensual ordenados cronológicamente de más antiguo a más reciente.
6. IF el `{id}` del Producto no existe o el Usuario autenticado no tiene acceso a ese Producto, THEN THE Servicio_Estadisticas SHALL responder con el código HTTP 404 sin revelar si el recurso existe o no.

---

### Requirement 9: Resumen de estadísticas en el perfil de usuario

**User Story:** As a usuario de Reviewmeter, I want que las tarjetas de resumen en la cabecera de mi perfil muestren datos actualizados y enriquecidos, so that pueda tener una visión rápida de mi actividad.

#### Acceptance Criteria

1. THE Frontend SHALL mostrar en la sección de estadísticas del perfil de usuario las siguientes tarjetas: total de Resenas publicadas, valoración media (escala 1.0–5.0, 1 decimal), número de Resenas en los últimos 30 días y categoría más reseñada (en caso de empate, se selecciona la categoría cuya Resena más reciente sea la más reciente).
2. WHEN el Servicio_Estadisticas devuelve los datos del Usuario, THE Frontend SHALL actualizar las tarjetas de resumen en un tiempo máximo de 2 segundos desde la carga de la página.
3. IF el Servicio_Estadisticas devuelve 0 reseñas publicadas para ese Usuario, THEN THE Frontend SHALL mostrar el valor "—" en las tarjetas de valoración media, número de reseñas en los últimos 30 días y categoría más reseñada.
4. WHEN el Usuario publica o elimina una Resena, THE Frontend SHALL refrescar automáticamente las tarjetas de resumen del perfil en un tiempo máximo de 2 segundos desde la confirmación del Servicio_Estadisticas, sin recargar la página completa.
5. IF el Servicio_Estadisticas no está disponible o supera el tiempo de espera, THEN THE Frontend SHALL mostrar un mensaje de error en la sección de estadísticas y mostrar "—" en todas las tarjetas.

---

### Requirement 10: Control de acceso a estadísticas según rol

**User Story:** As a plataforma Reviewmeter, I want controlar el acceso a las estadísticas según el rol del usuario, so that se protejan los datos sensibles de rendimiento comercial.

#### Acceptance Criteria

1. IF el identificador numérico en el token JWT del Usuario autenticado coincide con el parámetro `{id}` de la ruta `GET /api/estadisticas/usuario/{id}`, THEN THE Sistema SHALL permitir el acceso y devolver las estadísticas de ese Usuario.
2. IF el identificador numérico en el token JWT del Usuario autenticado no coincide con el parámetro `{id}` de la ruta `GET /api/estadisticas/usuario/{id}`, THEN THE Sistema SHALL responder con el código HTTP 403.
3. IF el Usuario autenticado tiene rol "ADMIN" y el recurso solicitado existe, THEN THE Sistema SHALL permitir el acceso a las estadísticas de cualquier Usuario o Producto.
4. IF el Usuario autenticado tiene rol "ADMIN" y el recurso solicitado no existe, THEN THE Sistema SHALL responder con el código HTTP 404.
5. IF un visitante no autenticado accede al endpoint `GET /api/estadisticas/producto/{id}`, THEN THE Sistema SHALL permitir el acceso únicamente a los campos marcados como públicos, excluyendo las métricas internas de rendimiento comercial.
6. IF un Usuario no autenticado intenta acceder a `GET /api/estadisticas/usuario/{id}`, THEN THE Sistema SHALL responder con el código HTTP 401.
