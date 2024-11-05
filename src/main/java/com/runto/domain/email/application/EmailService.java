package com.runto.domain.email.application;

import com.runto.domain.gathering.type.EventRequestStatus;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.util.Date;

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
        } catch (Exception e) {
            throw new RuntimeException("이메일 전송 중 오류 발생: " + e.getMessage(), e);
        }
    }
}
