# 🚀 Configurar Resend para Emails

## ✅ ¿Por qué Resend?

- **100% GRATIS**: 100 emails/día (3,000/mes) sin tarjeta de crédito
- **Funciona en Railway**: Usa API, no puertos SMTP bloqueados
- **Súper fácil**: Solo necesitas una API key
- **Rápido**: 5 minutos de configuración

---

## 📋 Paso 1: Crear Cuenta en Resend (2 min)

1. Ve a: **https://resend.com/signup**
2. Regístrate con tu email (Gmail, GitHub, etc.)
3. Verifica tu email
4. ¡Listo! Ya tienes cuenta

---

## 📋 Paso 2: Obtener API Key (1 min)

1. Una vez dentro, ve a: **API Keys** (en el menú lateral)
2. Haz clic en **Create API Key**
3. Nombre: `Reviewmeter Production`
4. Permisos: **Sending access** (por defecto)
5. Haz clic en **Add**
6. **COPIA LA API KEY** (se muestra solo una vez)
   ```
   re_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
   ```

---

## 📋 Paso 3: Verificar Dominio o Email (Opcional pero Recomendado)

### Opción A: Usar el dominio de prueba (Más Rápido)

Por defecto, Resend te da un dominio de prueba: `onboarding@resend.dev`

**Limitación**: Solo puedes enviar a tu propio email (el que usaste para registrarte)

### Opción B: Verificar tu email personal (Recomendado)

1. En Resend, ve a **Domains**
2. Haz clic en **Add Domain**
3. Ingresa: `resend.dev` (o tu dominio si tienes uno)
4. Sigue las instrucciones para verificar

**Para desarrollo**: Usa `onboarding@resend.dev` y envía solo a tu email

---

## 📋 Paso 4: Configurar en Railway (2 min)

1. Ve a: **https://railway.app/**
2. Selecciona tu proyecto
3. Clic en el servicio del **backend**
4. Ve a **Variables**
5. **Agrega estas variables**:

```
RESEND_API_KEY=re_tu_api_key_completa_aqui
RESEND_FROM_EMAIL=onboarding@resend.dev
```

**Notas**:
- Reemplaza `re_tu_api_key_completa_aqui` con tu API key real
- Si verificaste un dominio propio, usa: `noreply@tudominio.com`

6. Guarda los cambios

---

## 📋 Paso 5: Desplegar Cambios (3 min)

Abre una terminal y ejecuta:

```bash
# Ir al directorio del backend
cd c:\Users\nicol\Documents\ProjectoTFG\Tfg-backend

# Agregar cambios
git add .

# Commit
git commit -m "feat: Integrar Resend para envío de emails"

# Push a Railway
git push
```

Railway detectará el push y desplegará automáticamente (1-2 minutos).

---

## 📋 Paso 6: Probar (1 min)

1. Espera a que Railway termine el deployment
2. Ve a tu frontend
3. Intenta **registrarte** o **reenviar email de verificación**
4. **Revisa los logs en Railway**:
   - Ve a: Deployments → deployment activo → View Logs
   - Busca: `Email de verificación enviado exitosamente`

---

## ✅ Verificar que Funciona

### En los Logs de Railway

✅ **Éxito**:
```
Iniciando envío de email de verificación a: tu@email.com usando Resend
Email de verificación enviado exitosamente a: tu@email.com (ID: abc123...)
```

❌ **Error**:
```
Error de Resend al enviar email de verificación a tu@email.com: ...
```

### En tu Email

Deberías recibir un email con:
- **Asunto**: "Verifica tu cuenta en Reviewmeter"
- **Remitente**: `onboarding@resend.dev` (o tu dominio)
- **Botón**: "Verificar mi cuenta"

### En el Dashboard de Resend

1. Ve a **Emails** en Resend
2. Verás el email enviado con estado "Delivered"
3. Puedes ver estadísticas de apertura, clics, etc.

---

## 🆘 Troubleshooting

### Error: "API key is invalid"

**Causa**: La API key está mal copiada o no está configurada

**Solución**:
1. Verifica que `RESEND_API_KEY` en Railway esté correcta
2. Asegúrate de que empiece con `re_`
3. Regenera la API key en Resend si es necesario

### Error: "You can only send to verified emails"

**Causa**: Estás usando el dominio de prueba y enviando a un email no verificado

**Solución**:
1. Envía solo al email con el que te registraste en Resend
2. O verifica un dominio propio en Resend

### No recibo el email

**Causa**: Puede estar en spam o el email no se envió

**Solución**:
1. Revisa la carpeta de spam
2. Verifica los logs en Railway
3. Verifica en el dashboard de Resend si el email se envió

---

## 📊 Límites del Plan Gratuito

| Característica | Plan Gratuito |
|----------------|---------------|
| Emails por día | 100 |
| Emails por mes | 3,000 |
| Dominios | 1 |
| API Keys | Ilimitadas |
| Costo | $0 |

**Para tu app**: Más que suficiente

---

## 🎯 Resumen

1. ✅ Crear cuenta en Resend
2. ✅ Obtener API Key
3. ✅ Configurar variables en Railway:
   - `RESEND_API_KEY=re_...`
   - `RESEND_FROM_EMAIL=onboarding@resend.dev`
4. ✅ Desplegar: `git add . && git commit -m "feat: Resend" && git push`
5. ✅ Probar y verificar logs

**Tiempo total**: 10 minutos

---

## 🔗 Enlaces Útiles

- **Resend Dashboard**: https://resend.com/
- **Documentación**: https://resend.com/docs
- **API Reference**: https://resend.com/docs/api-reference/emails/send-email
- **Pricing**: https://resend.com/pricing

---

## ✨ Ventajas de Resend

- ✅ No requiere configuración SMTP
- ✅ API moderna y simple
- ✅ Estadísticas en tiempo real
- ✅ Webhooks para eventos (opcional)
- ✅ Excelente deliverability
- ✅ Soporte para attachments
- ✅ Templates HTML

¡Después de esto, tus emails funcionarán perfectamente! 🎉
