# Configurar SendGrid para Emails (RECOMENDADO)

## ¿Por qué SendGrid?

Railway y muchas plataformas de hosting bloquean los puertos SMTP tradicionales (587, 25) para prevenir spam. SendGrid es un servicio profesional de emails que:

- ✅ **Plan gratuito**: 100 emails/día (suficiente para tu app)
- ✅ **Funciona en Railway**: No hay bloqueos de puertos
- ✅ **Mejor deliverability**: Menos probabilidad de ir a spam
- ✅ **Estadísticas**: Puedes ver qué emails se enviaron y abrieron
- ✅ **Fácil configuración**: Solo necesitas una API key

## Paso 1: Crear Cuenta en SendGrid

1. Ve a https://signup.sendgrid.com/
2. Regístrate con tu email
3. Verifica tu email
4. Completa el formulario inicial (selecciona "Web App" como tipo de integración)

## Paso 2: Crear API Key

1. Una vez dentro, ve a **Settings** → **API Keys**
2. Haz clic en **Create API Key**
3. Nombre: `Reviewmeter-Production`
4. Permisos: **Full Access** (o al menos "Mail Send")
5. Haz clic en **Create & View**
6. **COPIA LA API KEY** (solo se muestra una vez)
   - Ejemplo: `SG.xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx`

## Paso 3: Verificar Sender Identity

SendGrid requiere que verifiques tu identidad de remitente:

### Opción A: Single Sender Verification (Más Fácil)

1. Ve a **Settings** → **Sender Authentication**
2. Haz clic en **Verify a Single Sender**
3. Completa el formulario:
   - From Name: `Reviewmeter`
   - From Email Address: `nicolas.adones8@gmail.com` (o el que prefieras)
   - Reply To: (mismo email)
   - Company Address: (tu dirección)
4. Haz clic en **Create**
5. **Revisa tu email** y haz clic en el enlace de verificación

### Opción B: Domain Authentication (Más Profesional)

Si tienes un dominio propio (ej: `reviewmeter.com`):

1. Ve a **Settings** → **Sender Authentication**
2. Haz clic en **Authenticate Your Domain**
3. Sigue las instrucciones para agregar registros DNS

## Paso 4: Configurar en Railway

1. Ve a tu proyecto en Railway
2. Selecciona el servicio del backend
3. Ve a **Variables**
4. Agrega/actualiza estas variables:

```
MAIL_HOST=smtp.sendgrid.net
MAIL_PORT=587
MAIL_USERNAME=apikey
MAIL_PASSWORD=SG.tu_api_key_aqui
```

**IMPORTANTE**: 
- El username SIEMPRE es `apikey` (literal)
- El password es tu API Key de SendGrid

## Paso 5: Actualizar application.properties

Ya está configurado para usar variables de entorno, pero asegúrate de que tenga:

```properties
spring.mail.host=${MAIL_HOST:smtp.gmail.com}
spring.mail.port=${MAIL_PORT:587}
spring.mail.username=${MAIL_USERNAME:apikey}
spring.mail.password=${MAIL_PASSWORD:tu_api_key}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
```

## Paso 6: Actualizar EmailService (Opcional)

Si quieres usar el email verificado en SendGrid como remitente:

```java
helper.setFrom("nicolas.adones8@gmail.com", "Reviewmeter");
```

O si verificaste un dominio:

```java
helper.setFrom("noreply@reviewmeter.com", "Reviewmeter");
```

## Paso 7: Desplegar y Probar

1. Commit y push los cambios
2. Espera a que Railway despliegue
3. Prueba el endpoint de reenvío de verificación
4. Revisa los logs en Railway

## Verificar que Funciona

En los logs de Railway deberías ver:

```
Iniciando envío de email de verificación a: [email]
Email de verificación enviado exitosamente a: [email]
```

En SendGrid puedes ver las estadísticas:
1. Ve a **Activity** en el dashboard
2. Verás todos los emails enviados, entregados, abiertos, etc.

## Troubleshooting

### Error: "The from address does not match a verified Sender Identity"

**Solución**: Verifica que el email en `helper.setFrom()` coincida con el email verificado en SendGrid.

### Error: "Authentication failed"

**Solución**: 
- Verifica que el username sea exactamente `apikey`
- Verifica que la API Key esté correcta (sin espacios)
- Regenera la API Key si es necesario

### Error: "Connection timeout"

**Solución**: Asegúrate de usar el puerto 587 (no 465 ni 25)

## Alternativa: Usar Puerto 465 con Gmail

Si prefieres seguir con Gmail, intenta el puerto 465 (SSL):

```
MAIL_HOST=smtp.gmail.com
MAIL_PORT=465
MAIL_USERNAME=nicolas.adones8@gmail.com
MAIL_PASSWORD=dbruzkhyhemznfwe
```

Y en application.properties:

```properties
spring.mail.properties.mail.smtp.ssl.enable=true
spring.mail.properties.mail.smtp.ssl.required=true
```

Pero SendGrid es más confiable para producción.

## Costos

- **Plan Free**: 100 emails/día (3,000/mes) - GRATIS
- **Plan Essentials**: $19.95/mes - 50,000 emails/mes
- **Plan Pro**: $89.95/mes - 100,000 emails/mes

Para tu aplicación, el plan gratuito debería ser suficiente.

## Recursos

- Documentación SendGrid: https://docs.sendgrid.com/
- Integración con Spring Boot: https://docs.sendgrid.com/for-developers/sending-email/spring-boot
- Dashboard SendGrid: https://app.sendgrid.com/
