# Solución: Email de Verificación No Funciona

## ✅ Problema Identificado

**Error Real**: `SocketTimeoutException: Connect timed out - Couldn't connect to host, port: smtp.gmail.com, 587`

**Causa**: Railway (y muchas plataformas de hosting) **bloquean los puertos SMTP salientes** (587, 25) para prevenir spam. Esto es una limitación de la infraestructura, no de tu código.

Los logs muestran claramente:
```
Error inesperado al enviar email de verificación a nicolas.adones839@gmail.com: 
Mail server connection failed. Failed messages: 
org.eclipse.angus.mail.util.MailConnectException: Couldn't connect to host, port: smtp.gmail.com, 587; timeout 5000
```

## Soluciones Disponibles

### ✅ Solución 1: Usar SendGrid (RECOMENDADO) 🚀

SendGrid es un servicio profesional de emails que funciona perfectamente en Railway:

- **Plan gratuito**: 100 emails/día (3,000/mes)
- **No hay bloqueos**: Usa APIs en lugar de SMTP tradicional
- **Mejor deliverability**: Menos probabilidad de ir a spam
- **Fácil configuración**: Solo necesitas una API key
- **Estadísticas**: Puedes ver qué emails se enviaron y abrieron

**Ver instrucciones completas en**: `CONFIGURAR_SENDGRID.md`

**Configuración rápida**:
1. Crear cuenta en https://signup.sendgrid.com/
2. Crear API Key en Settings → API Keys
3. Verificar email en Settings → Sender Authentication → Verify a Single Sender
4. Configurar en Railway:
   ```
   MAIL_HOST=smtp.sendgrid.net
   MAIL_PORT=587
   MAIL_USERNAME=apikey
   MAIL_PASSWORD=SG.tu_api_key_aqui
   ```
5. Desplegar y probar

### ⚠️ Solución 2: Intentar Puerto 465 con Gmail

He actualizado la configuración para usar el puerto 465 (SSL) en lugar de 587 (TLS). Algunos proveedores permiten este puerto.

**Cambios realizados en application.properties**:
- Puerto cambiado de 587 a 465
- STARTTLS deshabilitado
- SSL habilitado
- Timeouts aumentados a 10 segundos

**Para probar**:
1. Actualiza la variable en Railway: `MAIL_PORT=465`
2. Despliega los cambios (ver comandos abajo)
3. Intenta reenviar un email
4. Revisa los logs

**Probabilidad de éxito**: Baja (Railway probablemente también bloquea este puerto)

## Cambios Realizados en el Código

### 1. EmailService.java ✅
- Agregado logging con SLF4J para rastrear el envío de emails
- Logs informativos: "Iniciando envío..." y "Email enviado exitosamente..."
- Logs de error detallados con stack traces
- Separación de excepciones de mensajería vs. generales

### 2. AuthController.java ✅
- Agregado try-catch en el método `reenviarVerificacion`
- Retorna error 500 si falla el envío del email
- Logs de error para debugging en Railway

### 3. application.properties ✅
- Configuración de logging nivel DEBUG
- **Puerto cambiado a 465 (SSL)** para intentar evitar bloqueos
- SSL habilitado en lugar de STARTTLS
- Timeouts aumentados a 10 segundos

### 4. Documentación ✅
- `SOLUCION_EMAIL_VERIFICACION.md` - Este archivo
- `CONFIGURAR_SENDGRID.md` - Guía completa para configurar SendGrid

## Próximos Pasos

### Opción A: SendGrid (Recomendado) 🚀

1. Sigue las instrucciones en `CONFIGURAR_SENDGRID.md`
2. Configura las variables en Railway
3. Despliega y prueba
4. ✅ Problema resuelto

### Opción B: Intentar Puerto 465 con Gmail ⚠️

1. Actualiza en Railway: `MAIL_PORT=465`
2. Commit y push los cambios:
   ```bash
   cd Tfg-backend
   git add .
   git commit -m "Fix: Cambiar a puerto 465 SSL y mejorar logging de emails"
   git push
   ```
3. Revisa los logs después de intentar reenviar
4. Si sigue fallando con timeout, usa SendGrid

## Ver Logs en Railway

1. Ve a tu proyecto en Railway
2. Haz clic en el servicio del backend
3. Ve a "Deployments" → deployment activo → "View Logs"

Busca:
```
✅ Iniciando envío de email de verificación a: [email]
✅ Email de verificación enviado exitosamente a: [email]

❌ Error inesperado al enviar email de verificación a [email]: ...
```

## Recomendación Final

**Usa SendGrid**. Es la solución más confiable para producción:

1. ✅ Funciona en Railway sin problemas de puertos
2. ✅ Plan gratuito generoso (100 emails/día)
3. ✅ Mejor deliverability que Gmail
4. ✅ Estadísticas y monitoreo de emails
5. ✅ Configuración en 10 minutos
6. ✅ Escalable cuando tu app crezca

Ver `CONFIGURAR_SENDGRID.md` para instrucciones paso a paso.
