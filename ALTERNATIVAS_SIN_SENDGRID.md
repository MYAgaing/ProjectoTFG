# Alternativas para Email sin SendGrid

## Situación

Railway bloquea los puertos SMTP tradicionales (587, 25). Vamos a probar alternativas.

---

## ✅ Opción 1: Puerto 465 con Gmail (PROBAR PRIMERO)

### Cambios Ya Realizados

He actualizado la configuración para usar el puerto 465 (SSL):

```properties
spring.mail.port=465
spring.mail.properties.mail.smtp.ssl.enable=true
spring.mail.properties.mail.smtp.ssl.required=true
```

### Pasos para Desplegar

1. **Actualizar variable en Railway**:
   - Ve a tu proyecto en Railway
   - Selecciona el backend
   - Ve a Variables
   - Actualiza: `MAIL_PORT=465`

2. **Desplegar cambios de código**:
   ```bash
   cd c:\Users\nicol\Documents\ProjectoTFG\Tfg-backend
   git add src/
   git commit -m "Fix: Cambiar a puerto 465 SSL para Gmail"
   git push
   ```

3. **Probar**:
   - Espera el deployment
   - Intenta reenviar email
   - Revisa logs en Railway

### Probabilidad de Éxito

⚠️ **Baja (20%)** - Railway probablemente también bloquea este puerto, pero vale la pena intentar.

---

## ✅ Opción 2: Mailgun (Alternativa Gratuita)

Mailgun tiene un plan gratuito y funciona en Railway.

### Paso 1: Crear Cuenta

1. Ve a: https://signup.mailgun.com/
2. Regístrate (plan gratuito: 5,000 emails/mes durante 3 meses)
3. Verifica tu email

### Paso 2: Obtener Credenciales

1. Ve al Dashboard
2. En "Sending" → "Domain settings"
3. Copia:
   - **SMTP Hostname**: `smtp.mailgun.org`
   - **Port**: `587`
   - **Username**: `postmaster@sandboxXXXXXXXX.mailgun.org`
   - **Password**: (tu password SMTP)

### Paso 3: Configurar en Railway

```
MAIL_HOST=smtp.mailgun.org
MAIL_PORT=587
MAIL_USERNAME=postmaster@sandboxXXXXXXXX.mailgun.org
MAIL_PASSWORD=tu_password_smtp
```

### Paso 4: Verificar Email de Remitente

En Mailgun, agrega tu email como "Authorized Recipients" para el sandbox domain.

---

## ✅ Opción 3: Brevo (ex-Sendinblue)

Brevo tiene un plan gratuito generoso.

### Paso 1: Crear Cuenta

1. Ve a: https://www.brevo.com/
2. Regístrate (plan gratuito: 300 emails/día)
3. Verifica tu email

### Paso 2: Obtener Credenciales SMTP

1. Ve a Settings → SMTP & API
2. Copia:
   - **SMTP Server**: `smtp-relay.brevo.com`
   - **Port**: `587`
   - **Login**: (tu email de registro)
   - **SMTP Key**: (genera una nueva)

### Paso 3: Configurar en Railway

```
MAIL_HOST=smtp-relay.brevo.com
MAIL_PORT=587
MAIL_USERNAME=tu_email@ejemplo.com
MAIL_PASSWORD=tu_smtp_key
```

---

## ✅ Opción 4: Usar un Servidor VPS Propio

Si tienes un VPS o servidor propio con IP dedicada:

### Configurar Postfix en tu VPS

1. Instala Postfix en tu servidor
2. Configura relay SMTP
3. Usa tu servidor como relay

### Configurar en Railway

```
MAIL_HOST=tu-servidor.com
MAIL_PORT=587
MAIL_USERNAME=tu_usuario
MAIL_PASSWORD=tu_password
```

**Ventaja**: Control total
**Desventaja**: Requiere mantenimiento y configuración de DNS/SPF/DKIM

---

## ✅ Opción 5: Amazon SES

Amazon SES es muy económico pero requiere tarjeta de crédito.

### Paso 1: Crear Cuenta AWS

1. Ve a: https://aws.amazon.com/ses/
2. Crea cuenta (incluye capa gratuita)

### Paso 2: Verificar Email

1. En SES Console, verifica tu email de remitente
2. Solicita salir del "sandbox mode" (opcional)

### Paso 3: Crear Credenciales SMTP

1. En SES → SMTP Settings
2. Crea credenciales SMTP
3. Copia username y password

### Paso 4: Configurar en Railway

```
MAIL_HOST=email-smtp.us-east-1.amazonaws.com
MAIL_PORT=587
MAIL_USERNAME=tu_smtp_username
MAIL_PASSWORD=tu_smtp_password
```

**Costo**: Primeros 62,000 emails/mes gratis, luego $0.10 por 1,000 emails

---

## 📊 Comparación de Opciones

| Opción | Costo | Emails Gratis | Dificultad | Tiempo Setup | Funciona en Railway |
|--------|-------|---------------|------------|--------------|---------------------|
| Puerto 465 Gmail | Gratis | Ilimitado | Fácil | 5 min | ⚠️ Probablemente no |
| Mailgun | Gratis 3 meses | 5,000/mes | Fácil | 10 min | ✅ Sí |
| Brevo | Gratis | 300/día | Fácil | 10 min | ✅ Sí |
| VPS Propio | Variable | Ilimitado | Difícil | 2 horas | ✅ Sí |
| Amazon SES | Casi gratis | 62,000/mes | Media | 15 min | ✅ Sí |

---

## 🎯 Recomendación

### Orden de Prueba:

1. **Puerto 465 con Gmail** (5 min) - Ya está configurado, solo despliega
2. **Brevo** (10 min) - Plan gratuito generoso, fácil de configurar
3. **Mailgun** (10 min) - Buena alternativa, 5,000 emails/mes
4. **Amazon SES** (15 min) - Muy económico, requiere tarjeta

---

## 🚀 Acción Inmediata

### Probar Puerto 465 AHORA:

```bash
# 1. Ir al proyecto
cd c:\Users\nicol\Documents\ProjectoTFG\Tfg-backend

# 2. Commit cambios
git add src/
git commit -m "Fix: Cambiar a puerto 465 SSL para Gmail"

# 3. Push
git push
```

### En Railway:

1. Ve a Variables
2. Actualiza: `MAIL_PORT=465`
3. Espera el deployment
4. Prueba reenviar email
5. Revisa logs

### Si Falla:

Elige Brevo o Mailgun y sigue los pasos arriba.

---

## 📝 Notas

- **No necesitas cambiar código** para cambiar de proveedor
- Solo actualiza las variables de entorno en Railway
- Todos estos servicios usan SMTP estándar
- El código actual funciona con cualquiera de estas opciones
