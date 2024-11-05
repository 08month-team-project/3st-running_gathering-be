package com.runto.domain.email.application;

import com.runto.domain.email.exception.EmailException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;

import static com.runto.global.exception.ErrorCode.GENERIC_EMAIL_ERROR;
import static com.runto.global.exception.ErrorCode.INVALID_RECIPIENT;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public void sendApprovalStatusEmail(String to, String eventTitle, String status, String reportReason) {

        Context context = new Context();
        context.setVariable("eventTitle", eventTitle);
        context.setVariable("status", status);

        if (reportReason != null) {
            context.setVariable("reportReason", reportReason);
        } else {
            context.setVariable("reportReason", "");
        }

        String emailContent = templateEngine.process("email", context);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("이벤트 승인 상태 변경");
            helper.setText(emailContent, true);
            helper.setSentDate(new Date());

            mailSender.send(message);
        } catch (MailSendException e) {
            throw new EmailException(INVALID_RECIPIENT);
        } catch (Exception e) {
            throw new EmailException(GENERIC_EMAIL_ERROR);
        }
    }
}
