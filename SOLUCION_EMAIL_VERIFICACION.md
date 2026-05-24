# Solución: Email de Verificación No Funciona

## ✅ Problema Identificado

**Error**: `SocketTimeoutException: Connect timed out - Couldn't connect to host, port: smtp.gmail.com, 587`

**Causa**: Railway bloquea los puertos SMTP tradicionales (587, 25) para prevenir spam.

---

## 🔧 Cambios Realizados en el Código

### 1. EmailService.java ✅
- Agregado logging con SLF4J
- Logs detallados de inicio y éxito/error
- Mejor manejo de excepciones

### 2. AuthController.java ✅
- Try-catch en `reenviarVerificacion`
- Retorna error 500 si falla
- Logs para debugging

### 3. application.properties ✅
- **Puerto cambiado a 465 (SSL)**
- SSL habilitado
- Timeouts aumentados a 10 segundos
- Logging nivel DEBUG

---

## 🚀 Soluciones Disponibles

### Opción 1: Puerto 465 con Gmail (PROBAR PRIMERO)

**Ya está configurado en el código**. Solo necesitas:

1. **Actualizar en Railway**:
   - Variables → `MAIL_PORT=465`

2. **Desplegar**:
   ```bash
   cd c:\Users\nicol\Documents\ProjectoTFG\Tfg-backend
   git add src/
   git commit -m "Fix: Puerto 465 SSL para Gmail"
   git push
   ```

3. **Probar y revisar logs**

⚠️ **Probabilidad**: 20% (Railway puede bloquear este puerto también)

---

### Opción 2: Brevo (ex-Sendinblue) - RECOMENDADO

**Plan gratuito**: 300 emails/día

1. Crear cuenta: https://www.brevo.com/
2. Settings → SMTP & API → Generar SMTP Key
3. Configurar en Railway:
   ```
   MAIL_HOST=smtp-relay.brevo.com
   MAIL_PORT=587
   MAIL_USERNAME=tu_email@ejemplo.com
   MAIL_PASSWORD=tu_smtp_key
   ```

✅ **Funciona en Railway**: Sí

---

### Opción 3: Mailgun

**Plan gratuito**: 5,000 emails/mes (3 meses)

1. Crear cuenta: https://signup.mailgun.com/
2. Dashboard → Domain settings → Copiar credenciales SMTP
3. Configurar en Railway:
   ```
   MAIL_HOST=smtp.mailgun.org
   MAIL_PORT=587
   MAIL_USERNAME=postmaster@sandboxXXX.mailgun.org
   MAIL_PASSWORD=tu_password
   ```

✅ **Funciona en Railway**: Sí

---

### Opción 4: Amazon SES

**Costo**: 62,000 emails/mes gratis, luego $0.10/1,000

1. Crear cuenta AWS: https://aws.amazon.com/ses/
2. Verificar email de remitente
3. SES → SMTP Settings → Crear credenciales
4. Configurar en Railway:
   ```
   MAIL_HOST=email-smtp.us-east-1.amazonaws.com
   MAIL_PORT=587
   MAIL_USERNAME=tu_smtp_username
   MAIL_PASSWORD=tu_smtp_password
   ```

✅ **Funciona en Railway**: Sí

---

## 📊 Comparación

| Opción | Emails Gratis | Dificultad | Tiempo | Funciona |
|--------|---------------|------------|--------|----------|
| Puerto 465 Gmail | Ilimitado | Fácil | 5 min | ⚠️ Probablemente no |
| Brevo | 300/día | Fácil | 10 min | ✅ Sí |
| Mailgun | 5,000/mes | Fácil | 10 min | ✅ Sí |
| Amazon SES | 62,000/mes | Media | 15 min | ✅ Sí |

---

## 🎯 Recomendación

1. **Primero**: Prueba puerto 465 (ya configurado)
2. **Si falla**: Usa **Brevo** (300 emails/día gratis, fácil)
3. **Alternativa**: Mailgun o Amazon SES

Ver **ALTERNATIVAS_SIN_SENDGRID.md** para instrucciones detalladas.

---

## 📝 Nota Importante

**No necesitas cambiar código** para cambiar de proveedor. Solo actualiza las variables de entorno en Railway:

- `MAIL_HOST`
- `MAIL_PORT`
- `MAIL_USERNAME`
- `MAIL_PASSWORD`

El código actual funciona con cualquier proveedor SMTP.
