package br.com.rmc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import br.com.rmc.BaseException;

@Service
public class EmailService {
	
    @Autowired
    public JavaMailSender emailSender;	
	
	public void send(String to, String tittle, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        
        mailMessage.setFrom("cepf.notificacao@cepf.com");
        mailMessage.setTo(to); 
        mailMessage.setSubject(tittle); 
        mailMessage.setText(message);
        
        try {
        	emailSender.send(mailMessage);
        } catch (MailException e) {
        	throw new BaseException("Falha ao efetuar o envio do email para " + to, e);
		}
	}
}
