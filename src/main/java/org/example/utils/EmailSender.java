package org.example.utils;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {
    private String email;
    public EmailSender(String email){
        this.email = email;
    }

    public void SendEmail(){
        // Sender's email configuration
        String senderEmail = "";
        String senderPassword = "";

        // Email properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.example.com");
        properties.put("mail.smtp.port", "587");

        // Create a session with authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Create a new email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Passord resset for Contact Manager!");
            message.setText("This is your new Password: "+ "");

            // Send the email
            Transport.send(message);

            System.out.println("Email sent successfully.");

        } catch (MessagingException e) {
            System.out.println("Error sending email: " + e.getMessage());
        }
    }


}
