package com.reviewmeter.tfg.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Value("${resend.api-key}")
    private String resendApiKey;

    @Value("${resend.from-email}")
    private String fromEmail;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Async
    public void enviarEmailVerificacion(String destinatario, String nombre, String token) {
        try {
            logger.info("Iniciando envío de email de verificación a: {}", destinatario);

            String enlace = frontendUrl + "/verificar?token=" + token;

            String html = """
                    <!DOCTYPE html>
                    <html lang="es">
                    <head><meta charset="UTF-8"></head>
                    <body style="margin:0;padding:0;background:#f3f4f6;font-family:Arial,sans-serif;">
                      <table width="100%%" cellpadding="0" cellspacing="0" style="background:#f3f4f6;padding:40px 0;">
                        <tr><td align="center">
                          <table width="480" cellpadding="0" cellspacing="0"
                                 style="background:#ffffff;border-radius:16px;overflow:hidden;box-shadow:0 4px 24px rgba(0,0,0,0.08);">
                            <tr>
                              <td style="background:linear-gradient(135deg,#2563eb,#7c3aed);padding:32px;text-align:center;">
                                <span style="background:#fff;color:#2563eb;font-size:22px;font-weight:900;
                                             padding:8px 16px;border-radius:8px;">R</span>
                                <span style="color:#fff;font-size:20px;font-weight:700;margin-left:10px;
                                             vertical-align:middle;">Reviewmeter</span>
                              </td>
                            </tr>
                            <tr>
                              <td style="padding:40px 36px;">
                                <h2 style="margin:0 0 12px;color:#111827;font-size:22px;">
                                  ¡Hola, %s! 👋
                                </h2>
                                <p style="margin:0 0 24px;color:#6b7280;font-size:15px;line-height:1.6;">
                                  Gracias por registrarte en <strong>Reviewmeter</strong>.
                                  Para activar tu cuenta y empezar a escribir reseñas, confirma tu dirección de email.
                                </p>
                                <table cellpadding="0" cellspacing="0" width="100%%">
                                  <tr>
                                    <td align="center" style="padding:8px 0 32px;">
                                      <a href="%s"
                                         style="display:inline-block;background:#2563eb;color:#ffffff;
                                                font-size:15px;font-weight:700;text-decoration:none;
                                                padding:14px 36px;border-radius:50px;">
                                        Verificar mi cuenta
                                      </a>
                                    </td>
                                  </tr>
                                </table>
                                <p style="margin:0 0 8px;color:#9ca3af;font-size:13px;">
                                  Si el botón no funciona, copia y pega este enlace en tu navegador:
                                </p>
                                <p style="margin:0 0 24px;word-break:break-all;">
                                  <a href="%s" style="color:#2563eb;font-size:13px;">%s</a>
                                </p>
                                <p style="margin:0;color:#d1d5db;font-size:12px;">
                                  Este enlace expira en <strong>24 horas</strong>.
                                  Si no creaste esta cuenta, puedes ignorar este mensaje.
                                </p>
                              </td>
                            </tr>
                            <tr>
                              <td style="background:#f9fafb;padding:20px 36px;text-align:center;
                                         border-top:1px solid #e5e7eb;">
                                <p style="margin:0;color:#9ca3af;font-size:12px;">
                                  © 2025 Reviewmeter · Todos los derechos reservados
                                </p>
                              </td>
                            </tr>
                          </table>
                        </td></tr>
                      </table>
                    </body>
                    </html>
                    """.formatted(nombre, enlace, enlace, enlace);

            Resend resend = new Resend(resendApiKey);

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from("onboarding@resend.dev")
                    .to(destinatario)
                    .subject("Verifica tu cuenta en Reviewmeter")
                    .html(html)
                    .build();

            CreateEmailResponse data = resend.emails().send(params);
            logger.info("Email enviado exitosamente a: {} (ID: {})", destinatario, data.getId());

        } catch (ResendException e) {
            logger.error("Error de Resend al enviar email a {}: {}", destinatario, e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error inesperado al enviar email a {}: {}", destinatario, e.getMessage(), e);
        }
    }
}
