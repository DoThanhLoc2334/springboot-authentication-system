// package com.dtl.springboot_auth_system.service;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.mail.SimpleMailMessage;
// import org.springframework.mail.javamail.JavaMailSender;
// import org.springframework.stereotype.Service;

// @Service
// public class MailSender {

//     @Autowired
//     private JavaMailSender javaMailSender;

//     public void sendOtpEmail(String toEmail, String otp) {
//         try {
//             SimpleMailMessage message = new SimpleMailMessage();
//             message.setFrom("noreply@authsystem.com");
//             message.setTo(toEmail);
//             message.setSubject("Password Reset OTP");
//             message.setText("Your OTP for password reset is: " + otp + "\n\nThis OTP is valid for 10 minutes.\n\n"
//                     + "If you did not request this, please ignore this email.");

//             javaMailSender.send(message);
//         } catch (Exception e) {
//             throw new RuntimeException("Failed to send OTP email", e);
//         }
//     }

//     public void sendPasswordResetSuccessEmail(String toEmail, String username) {
//         try {
//             SimpleMailMessage message = new SimpleMailMessage();
//             message.setFrom("noreply@authsystem.com");
//             message.setTo(toEmail);
//             message.setSubject("Password Reset Successfully");
//             message.setText("Hello " + username + ",\n\n"
//                     + "Your password has been reset successfully. You can now log in with your new password.\n\n"
//                     + "If you did not perform this action, please contact support immediately.");

//             javaMailSender.send(message);
//         } catch (Exception e) {
//             throw new RuntimeException("Failed to send password reset success email", e);
//         }
//     }
// }
