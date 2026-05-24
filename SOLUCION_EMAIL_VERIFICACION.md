# Solución: Email de Verificación No Funciona

## Problema Identificado

El endpoint `/auth/reenviar-verificacion` devuelve 200 OK pero el email no se envía. Esto ocurre porque:

1. **Errores silenciosos**: El método `enviarEmailVerificacion` es asíncrono (`@Async`) y captura excepciones sin propagarlas
2. **Falta de logging**: Los errores solo se imprimían en `System.err` sin logging estructurado
3. **Sin manejo de errores en el controlador**: El controlador no capturaba posibles excepciones

## Cambios Realizados

### 1. EmailService.java
- ✅ Agregado logging con SLF4J para rastrear el envío de emails
- ✅ Mejorado el manejo de excepciones con logs detallados
- ✅ Separación de excepciones de mensajería vs. excepciones generales

### 2. AuthController.java
- ✅ Agregado try-catch en el método `reenviarVerificacion`
- ✅ Retorna error 500 si falla el envío del email
- ✅ Logs de error para debugging

### 3. application.properties
- ✅ Configuración de logging para ver errores en Railway
- ✅ Nivel DEBUG para el paquete de la aplicación y Spring Mail

## Verificación en Railway

### Paso 1: Verificar Variables de Entorno

Asegúrate de que estas variables estén configuradas en Railway:

```
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=nicolas.adones8@gmail.com
MAIL_PASSWORD=dbruzkhyhemznfwe
FRONTEND_URL=https://tu-frontend.vercel.app
```

### Paso 2: Verificar Contraseña de Aplicación de Gmail

La contraseña `dbruzkhyhemznfwe` es una contraseña de aplicación de Gmail. Si no funciona:

1. Ve a https://myaccount.google.com/security
2. Activa la verificación en 2 pasos (si no está activada)
3. Ve a "Contraseñas de aplicaciones"
4. Genera una nueva contraseña para "Correo"
5. Actualiza la variable `MAIL_PASSWORD` en Railway

### Paso 3: Ver Logs en Railway

1. Ve a tu proyecto en Railway
2. Haz clic en el servicio del backend
3. Ve a la pestaña "Deployments"
4. Haz clic en el deployment activo
5. Ve a "View Logs"

Busca líneas como:
```
Iniciando envío de email de verificación a: [email]
Email de verificación enviado exitosamente a: [email]
```

O errores como:
```
Error de mensajería al enviar email de verificación a [email]: ...
```

### Paso 4: Probar el Endpoint

Después de desplegar los cambios:

```bash
curl -X POST https://projectotfg-production.up.railway.app/auth/reenviar-verificacion \
  -H "Content-Type: application/json" \
  -d '{"email":"tu-email@ejemplo.com"}'
```

Ahora debería:
- Retornar error 500 si falla el envío
- Mostrar logs detallados en Railway
- Enviar el email si la configuración es correcta

## Posibles Causas del Problema

### 1. Contraseña de Aplicación Inválida
**Síntoma**: `535 Authentication failed`
**Solución**: Regenerar contraseña de aplicación en Gmail

### 2. Verificación en 2 Pasos Desactivada
**Síntoma**: No puedes generar contraseñas de aplicación
**Solución**: Activar verificación en 2 pasos en tu cuenta de Google

### 3. Cuenta de Gmail Bloqueada
**Síntoma**: `535 Please log in via your web browser`
**Solución**: 
- Iniciar sesión en Gmail desde un navegador
- Verificar si hay alertas de seguridad
- Permitir el acceso desde aplicaciones menos seguras (no recomendado)

### 4. Firewall o Red Bloqueando SMTP
**Síntoma**: `Connection timeout`
**Solución**: Railway debería permitir conexiones SMTP salientes, pero verifica los logs

### 5. Variables de Entorno No Configuradas
**Síntoma**: Email se envía a localhost o con credenciales por defecto
**Solución**: Verificar que todas las variables estén en Railway

## Alternativa: Usar un Servicio de Email Dedicado

Si Gmail sigue dando problemas, considera usar:

### SendGrid (Recomendado)
```properties
spring.mail.host=smtp.sendgrid.net
spring.mail.port=587
spring.mail.username=apikey
spring.mail.password=TU_API_KEY_DE_SENDGRID
```

### Mailgun
```properties
spring.mail.host=smtp.mailgun.org
spring.mail.port=587
spring.mail.username=postmaster@tu-dominio.mailgun.org
spring.mail.password=TU_PASSWORD_DE_MAILGUN
```

### Amazon SES
```properties
spring.mail.host=email-smtp.us-east-1.amazonaws.com
spring.mail.port=587
spring.mail.username=TU_SMTP_USERNAME
spring.mail.password=TU_SMTP_PASSWORD
```

## Próximos Pasos

1. **Desplegar los cambios** en Railway
2. **Verificar las variables de entorno** en Railway
3. **Revisar los logs** después de intentar reenviar un email
4. **Regenerar la contraseña de aplicación** de Gmail si es necesario
5. **Considerar un servicio de email dedicado** si Gmail sigue fallando

## Comandos Útiles

### Compilar y desplegar
```bash
cd Tfg-backend
mvn clean package
git add .
git commit -m "Fix: Mejorar logging y manejo de errores en envío de emails"
git push
```

### Ver logs en tiempo real (si tienes Railway CLI)
```bash
railway logs
```
