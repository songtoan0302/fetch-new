package com.vnpt.tnvn.send_email;

import com.vnpt.tnvn.send_email.model.OracleQuestion;
import com.vnpt.tnvn.send_email.model.OracleUser;
import com.vnpt.tnvn.send_email.model.OracleUserCategory;
import com.vnpt.tnvn.send_email.repo.OracleCategoryRepository;
import com.vnpt.tnvn.send_email.repo.OracleQuestionRepository;
import com.vnpt.tnvn.send_email.repo.OracleUserCategoryRepository;
import com.vnpt.tnvn.send_email.repo.OracleUserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Service
public class SendEmailService {

    private static final String CONTENT_TYPE = "text/html; charset=utf-8";

    @Autowired
    private SendEmailConfiguration configuration;
    @Autowired
    private OracleUserRepository oracleUserRepository;
    @Autowired
    private OracleQuestionRepository oracleQuestionRepository;
    @Autowired
    private OracleCategoryRepository oracleCategoryRepository;
    @Autowired
    private OracleUserCategoryRepository oracleUserCategoryRepository;

    public void sendEmail() {
        List<OracleQuestion> questions = oracleQuestionRepository.findAllByIsAnswerIsNotLikeAndIsNotifiedEquals(1, 0);
        if (questions != null && !questions.isEmpty()) {
            for (OracleQuestion question : questions) {
                List<OracleUserCategory> userCategories = oracleUserCategoryRepository.findOracleUserCategoriesByEmbedded_CategoryId(question.categoryId);
                if (userCategories != null && !userCategories.isEmpty()) {
                    StringBuilder addressesString = new StringBuilder();
                    for (OracleUserCategory userCategory : userCategories) {
                        OracleUser user = oracleUserRepository.findOracleUserById(userCategory.embedded.userId);
                        if (user != null && StringUtils.hasLength(user.email)) {
                            if (!addressesString.toString().isEmpty()) {
                                addressesString.append(",");
                            }
                            addressesString.append(user.email);
                        }
                    }
                    if (!StringUtils.isEmpty(addressesString.toString())) {
                        try {
                            send(InternetAddress.parse(addressesString.toString()), question);
                        } catch (AddressException e) {
                            Logger.getLogger(getClass()).error(e.getMessage());
                        }
                    }
                }
            }
        }
    }

    private void send(Address[] addresses, OracleQuestion question) {
        new Thread(() -> {
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.host", configuration.getSmtpHost());
            properties.put("mail.smtp.port", configuration.getSmtpPort());
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.starttls.required", "true");
            properties.put("mail.smtp.startssl.enable", "true");
            properties.put("mail.smtp.startssl.required", "true");

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(configuration.getSenderEmail(), configuration.getSenderPassword());
                }
            });

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(configuration.getSenderEmail(), false));
                message.setRecipients(Message.RecipientType.TO, addresses);
                message.setSubject("[TNVN][Question] Bạn có câu hỏi: " + question.title);
                message.setContent(
                        "Xin chào,"
                                + "<br></br>Nhờ bạn trả lời câu hỏi bên dưới nhé:"
                                + "<br></br><b>Chuyên mục: </b>" + oracleCategoryRepository.findOracleCategoryById(question.categoryId).name + "<br></br>"
                                + "<b>Tiêu đề: </b>" + question.title + "<br></br>"
                                + "<b>Nội dung: </b>" + question.content + "<br></br>"
                                + (StringUtils.isEmpty(question.filePath1) ? "" : "Link 1: " + question.filePath1 + "<br></br>")
                                + (StringUtils.isEmpty(question.filePath2) ? "" : "Link 2: " + question.filePath2 + "<br></br>")
                                + (StringUtils.isEmpty(question.filePath3) ? "" : "Link 3: " + question.filePath3 + "<br></br>")
                                + "Thân ái,<br></br>Thanh niên Việt Nam",
                        CONTENT_TYPE
                );
                message.setSentDate(new Date());
                Transport.send(message);
                question.isNotified = 1;
                oracleQuestionRepository.save(question);
            } catch (MessagingException e) {
                Logger.getLogger(getClass()).error(e.getMessage());
            }
        }).start();
    }
}
