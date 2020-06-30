package org.base.webapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {
    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String smtpAuth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String starttlsEnable;

    @Value("${spring.mail.host}")
    private String emailHost;

    @Value("${spring.mail.username}")
    private String emailUsername;

    @Value("${spring.mail.password}")
    private String emailPassword;

    @Value("${spring.mail.port}")
    private int emailPort;

    @Value("${spring.mail.protocol}")
    private String emailProtocol;

    @Value("${email.debug}")
    private String emailDebug;

    @Bean
    public JavaMailSender getMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(emailHost);
        mailSender.setPort(emailPort);
        mailSender.setUsername(emailUsername);
        mailSender.setPassword(emailPassword);

        Properties mailProperties = mailSender.getJavaMailProperties();

        mailProperties.setProperty("mail.transport.protocol",emailProtocol);
        mailProperties.setProperty("mail.debug", emailDebug);
        mailProperties.setProperty("mail.smtp.auth", smtpAuth);
        mailProperties.setProperty("mail.smtp.starttls.enable",starttlsEnable);

        return mailSender;
    }
}
