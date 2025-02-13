package com.example.demo.service;


import com.example.demo.model.EmailDetail;
import com.example.demo.repository.AuthenticationRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
public class EmailService {

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    AuthenticationRepository authenticationRepository;

    public void sendMailTemplate(EmailDetail emailDetail) {
        try {

            Context context = new Context();
            context.setVariable("username", emailDetail.getName());
            context.setVariable("buttonValue", emailDetail.getButtonValue() != null ? emailDetail.getButtonValue() : "Verify Email");
            context.setVariable("link", emailDetail.getLink());
            context.setVariable("email", emailDetail.getRecipient());

            String text = templateEngine.process("emailtemplate", context);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom("swpproject2024@gmail.com");
            mimeMessageHelper.setTo(emailDetail.getRecipient());
            mimeMessageHelper.setText(text, true);
            mimeMessageHelper.setSubject(emailDetail.getSubject());

            if (emailDetail.getAttachment() != null) {
                mimeMessageHelper.addAttachment(emailDetail.getAttachment().getFilename(), emailDetail.getAttachment());
            }

            javaMailSender.send(mimeMessage);
        } catch (MessagingException messagingException) {
            messagingException.printStackTrace();
        }
    }


    public void sendMailTemplateOwner(EmailDetail emailDetail) {
        try {
            Context context = new Context();
            context.setVariable("username", emailDetail.getName());
            context.setVariable("buttonValue", emailDetail.getButtonValue());
            context.setVariable("link", emailDetail.getLink());
            context.setVariable("email", emailDetail.getRecipient());

            String text = templateEngine.process("emailtemplateowner", context);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom("swpproject2024@gmail.com");
            mimeMessageHelper.setTo(emailDetail.getRecipient());
            mimeMessageHelper.setText(text, true);
            mimeMessageHelper.setSubject(emailDetail.getSubject());

            if (emailDetail.getAttachment() != null) {
                mimeMessageHelper.addAttachment(emailDetail.getAttachment().getFilename(), emailDetail.getAttachment());
            }
            javaMailSender.send(mimeMessage);
        } catch (MessagingException messagingException) {
            messagingException.printStackTrace();
        }
    }

    public void sendMailTemplateForgot(EmailDetail emailDetail) {
        try {
            // Log recipient email address for debugging
            System.out.println("Sending email to: " + emailDetail.getRecipient());

            // Validate email address
            if (emailDetail.getRecipient() == null || emailDetail.getRecipient().isEmpty()) {
                throw new IllegalArgumentException("Recipient email address is invalid or empty");
            }

            Context context = new Context();
            context.setVariable("username", emailDetail.getName());
            context.setVariable("buttonValue", emailDetail.getButtonValue());
            context.setVariable("link", emailDetail.getLink());
            context.setVariable("email", emailDetail.getRecipient());
            context.setVariable("registrationDate", java.time.Clock.systemUTC().instant());

            String text = templateEngine.process("forgotpasswordemailtemplate", context);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom("swpproject2024@gmail.com");
            mimeMessageHelper.setTo(emailDetail.getRecipient());
            mimeMessageHelper.setText(text, true);
            mimeMessageHelper.setSubject(emailDetail.getSubject());

            if (emailDetail.getAttachment() != null) {
                mimeMessageHelper.addAttachment(emailDetail.getAttachment().getFilename(), emailDetail.getAttachment());
            }

            javaMailSender.send(mimeMessage);

            // Log success
            System.out.println("Email successfully sent to: " + emailDetail.getRecipient());

        } catch (MessagingException messagingException) {
            messagingException.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
//    public void sendBookingConfirmationEmail(Booking booking) throws MessagingException {
//        MimeMessage message = javaMailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//        helper.setTo(booking.getCustomer().getEmail());
//        helper.setSubject("Booking Confirmation");
//
//        Map<String, Object> model = new HashMap<>();
//        model.put("customerName", booking.getCustomer().getName());
//        model.put("bookingCode", booking.getCodebooking());
//        model.put("bookingDate", booking.getBookingDate());
//        model.put("locationName", booking.getLocation().getName());
//        model.put("bookingType", booking.getBookingType().toString());
//        model.put("totalPrice", booking.getTotalPrice());
//
//        List<Map<String, Object>> bookingDetails = new ArrayList<>();
//        for (BookingDetail detail : booking.getBookingDetails()) {
//            Map<String, Object> detailModel = new HashMap<>();
//            detailModel.put("courtName", detail.getCourtSlot().getCourt().getName());
//            detailModel.put("date", detail.getCourtSlot().getDate());
//            detailModel.put("slotTime", detail.getCourtSlot().getSlot().getTime());
//            detailModel.put("status", detail.getCourtSlot().getStatus().toString());
//            detailModel.put("price", detail.getPrice());
//            detailModel.put("bookingDetail", detail.getBooking().getCodebooking());
//            detailModel.put("account", detail.getCourtSlot().getAccount().getName());
//            detailModel.put("codeBooking", detail.getCourtSlot().getCodebooking());
//            bookingDetails.add(detailModel);
//        }
//        model.put("bookingDetails", bookingDetails);
//
//        if (booking.getPromotion() != null) {
//            model.put("promotion", true);
//            model.put("promotionCode", booking.getPromotion().getCode());
//            model.put("promotionDiscount", booking.getPromotion().getDiscount());
//        } else {
//            model.put("promotion", false);
//        }
//
//        Context context = new Context();
//        context.setVariables(model);
//
//        String html = templateEngine.process("bookingTemplate", context);
//        helper.setText(html, true);
//
//        javaMailSender.send(message);
//    }
}
