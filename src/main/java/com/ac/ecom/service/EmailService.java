package com.ac.ecom.service;

import com.ac.ecom.model.Order;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendOrderConfirmation(Order order){

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(order.getEmail());
            helper.setSubject("Order Confirmation – Thank You for Your Purchase");
            helper.setFrom("noreply@ecommerce.com");

            String emailContent = buildEmailContent(order);
            helper.setText(emailContent, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String buildEmailContent (Order order){
        return """
                            <html>
                              <body style="font-family:Arial,sans-serif;">
                                <h2>Thank you for your order!</h2>
                        
                                <p>Your order has been placed successfully. Below are the details:</p>
                        
                                <table border="1" cellpadding="10" cellspacing="0">
                                  <tr>
                                    <th>Product</th>
                                    <th>Price</th>
                                    <th>Quantity</th>
                                    <th>Total</th>
                                  </tr>
                                  <tr>
                                    <td>%s</td>
                                    <td>₹%.2f</td>
                                    <td>%d</td>
                                    <td>₹%.2f</td>
                                  </tr>
                                </table>
                        
                                <p><b>Order Total:</b> ₹%.2f</p>
                        
                                <p>
                                  We appreciate your business and hope you enjoy your purchase.
                                </p>
                        
                                <p>
                                  Regards,<br/>
                                  <b>E-Commerce Team</b>
                                </p>
                              </body>
                            </html>
                        """.formatted(
                order.getOrderId(),
                order.getCustomerName(),
                order.getStatus()
                );
    }
}
