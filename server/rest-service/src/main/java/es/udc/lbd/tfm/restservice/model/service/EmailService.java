package es.udc.lbd.tfm.restservice.model.service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.tfm.restservice.model.domain.User;
import es.udc.lbd.tfm.restservice.model.exception.NotFoundException;
import es.udc.lbd.tfm.restservice.model.repository.UserDao;
import es.udc.lbd.tfm.restservice.model.service.dto.UserDTOPublic;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class EmailService {
	
	@Autowired
	private UserDao userDAO;
	
	@Transactional(readOnly = false)
	public void send(String email_login) throws NotFoundException {  
		//Email para recueprar contraseña
	     	      
	      User bdUser = userDAO.findByEmail(email_login);
	      if(bdUser == null) {
	    	  bdUser = userDAO.findByLogin(email_login);
	    	  if(bdUser == null) {
	    		  throw new NotFoundException("User with email or login "+email_login+" not found!");
	    	  }
	      }
//	      String to = bdUser.getEmail();
	    //Para hacer las pruebas usamos siempre este, después habría que utilizar el del usuario
	      String to = "masterTFMUDC@gmail.com";
	      // Sender's email ID needs to be mentioned
	      String from = "masterTFMUDC@gmail.com";

	      // Get system properties
	      Properties properties = System.getProperties();

	      // Setup mail server	      
	      properties.put("mail.smtp.host", "smtp.gmail.com");  //Servidor SMTP de Google
	      properties.put("mail.smtp.auth", "true"); //Usar autenticación mediante usuario y clave
	      properties.put("mail.smtp.port", "587"); //Puerto SMTP seguro de Google
	      properties.put("mail.smtp.starttls.enable", "true"); //Conexión de forma segura al servidor SMTP
	      properties.put("mail.smtp.ssl.trust", "*");
	      
	      // Get the Session object.// and pass username and password
	      Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(from, "vxjpdirdogqvfeag");
	            }
	      });
	
	      // Used to debug SMTP issues
	      session.setDebug(true);


	      try {
	         // Create a default MimeMessage object.
	         MimeMessage message = new MimeMessage(session);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(from));

	         // Set To: header field of the header.
	         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

	         // Set Subject: header field
	         message.setSubject("Reset password!");
	         String hashString = getSHA256(bdUser.getId()+bdUser.getLogin());
	         bdUser.setValidationString(hashString);
	         // Now set the actual message
	         String hyperlink = "http://localhost:1234/#/reset-password/"+bdUser.getId()+"/"+hashString;
	          String body = "<b> Hi "+bdUser.getName()+",</b><br>";
	          body += "<b>Someone recently requested a password change for your account.</b><br> If this was you, you can set a new password here:</b><br>";
	          body += "<a target='_blank' href="+hyperlink+"><button style='background-color: #A3E4D7;'>Reset your password</button></a></b><br>";
	          body += "<b>If you don't want to change your password or didn't request this, just ignore and delete this message</b><br>";
	          body += "<b>To keep your account secure, please don't forward this email to anyone!</b><br>";
	          body += "<b>Thanks!</b><br>";
	          
	          message.setContent(body,  "text/html; charset=utf-8");

	         // Send message
	         Transport.send(message);
	         bdUser.setEmailExpirationDate(LocalDate.now().plusDays(2));
	      } catch (MessagingException mex) {
	         mex.printStackTrace();
	      }
	}
	
	@Transactional(readOnly = false)
	public void sendToCreateLogin(UserDTOPublic user) {
		//Para los editores dados de alta registrar su login y password
	      
	      User bdUser = userDAO.findByEmail(user.getEmail());

	      //Para hacer las pruebas usamos siempre este, después habría que utilizar el del usuario
//	      String to = bdUser.getEmail();
	      String to = "masterTFMUDC@gmail.com";
	      // Sender's email ID needs to be mentioned
	      String from = "masterTFMUDC@gmail.com";

	      // Get system properties
	      Properties properties = System.getProperties();
	      
	      properties.put("mail.smtp.host", "smtp.gmail.com"); 
	      properties.put("mail.smtp.auth", "true"); 
	      properties.put("mail.smtp.port", "587"); 
	      properties.put("mail.smtp.starttls.enable", "true"); 
	      properties.put("mail.smtp.ssl.trust", "*");
	      // Get the Session object.// and pass username and password
	      Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(from, "vxjpdirdogqvfeag");
	            }
	      });
	
	      // Used to debug SMTP issues
	      session.setDebug(true);


	      try {
	         // Create a default MimeMessage object.
	         MimeMessage message = new MimeMessage(session);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(from));

	         // Set To: header field of the header.
	         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

	         // Set Subject: header field
	         message.setSubject("Set login and password!");
	         String hashString = getSHA256(bdUser.getId()+bdUser.getEmail());
	         bdUser.setValidationString(hashString);
	         // Now set the actual message
	          String hyperlink = "http://localhost:1234/#/new-login-password/"+bdUser.getId()+"/"+hashString;
	          String body = "<b> Hi "+bdUser.getName()+",</b><br>";
	          body += "<b>You have been registered in the digital content application.</b><br> Please click here to set your login and password.:</b><br>";
	          body += "<a target='_blank' href="+hyperlink+"><button style='background-color: #A3E4D7;'>Set login & password</button></a></b><br>";
	          body += "<b>If you don't want to register in the application or didn't request this, just ignore and delete this message</b><br>";
	          body += "<b></b><br>";
	          body += "<b>Thanks!</b><br>";
	          
	          message.setContent(body,  "text/html; charset=utf-8");
	          bdUser.setEmailExpirationDate(LocalDate.now().plusDays(2));
	         // Send message
	         Transport.send(message);
	      } catch (MessagingException mex) {
	         mex.printStackTrace();
	      }
	}
	
	private String getSHA256(String input){

		String toReturn = null;
		try {
		    MessageDigest digest = MessageDigest.getInstance("SHA-256");
		    digest.reset();
		    digest.update(input.getBytes("utf8"));
		    toReturn = String.format("%064x", new BigInteger(1, digest.digest()));
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		return toReturn;
	    }

}
