package com.app.utilityServices;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.app.dto.ContactRequestDTO;
import com.app.dto.FollowUpLeads;
import com.app.modal.User;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	@Autowired
	JavaMailSender javaMailSender;

	public void sendEmail(User user, String subject,List<String> ccRecipients, List<FollowUpLeads> followUpLeadsList)
			throws MessagingException, UnsupportedEncodingException {
		
		String to = user.getEmail();
		
		subject = subject+" Followup leads for "+user.getName();
		String content = "<p>Dear "+user.getName() + "," + ",</p>" + "<p>Kindely take action on below leads.</p>";

		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		String clientBusinessName = "";
		StringBuilder tableContent = new StringBuilder();
		tableContent.append("<table style='width: 100%; border-collapse: collapse; margin-top: 15px;'>");
		tableContent.append("<tr style='background-color: #39dee8; color: white; text-align: left;'>");
		tableContent.append("<th style='padding: 10px; border: 1px solid #ddd;'>SR No.</th>");
		tableContent.append("<th style='padding: 10px; border: 1px solid #ddd;'>Client Name</th>");
		tableContent.append("<th style='padding: 10px; border: 1px solid #ddd;'>Client Email</th>");
		//tableContent.append("<th style='padding: 10px; border: 1px solid #ddd;'>Follow-Up Date</th>");
		tableContent.append("<th style='padding: 10px; border: 1px solid #ddd;'>Source</th>");
		tableContent.append("</tr>");

		
		int srNo = 1;
		for (FollowUpLeads lead : followUpLeadsList) {
			tableContent.append("<tr style='background-color: #f9f9f9; text-align: left;'>");
			tableContent.append("<td style='padding: 8px; border: 1px solid #ddd;'>" + srNo++ + "</td>");
			tableContent.append("<td style='padding: 8px; border: 1px solid #ddd;'>" + lead.getClientName() + "</td>");
			tableContent.append("<td style='padding: 8px; border: 1px solid #ddd;'>" + lead.getClientEmail() + "</td>");
			//tableContent.append("<td style='padding: 8px; border: 1px solid #ddd;'>" + lead.getNextFollowUpDate() + "</td>");
			tableContent.append(
					"<td style='padding: 8px; border: 1px solid #ddd;'>" + lead.getOurContactSource() + "</td>");
			tableContent.append("</tr>");
		}
		tableContent.append("</table>");

		// Integrate the table into the email content
		String emailContent = "<div style='background-color: rgb(240, 247, 255); padding: 20px;'>"
				+ "<div style='max-width: 600px; margin: 0 auto; background-color: rgb(255, 255, 255); border-radius: 5px;'>"
				//+ "<img src='cid:invoicementLogo' alt='Invoice Logo' style='display: block; width: 100%; border-top-left-radius: 5px; border-top-right-radius: 5px;'>"
				+ "<div style='padding: 20px;'>" + "<p style='font-size: 14px;'>" + content + "</p>"
				+ "<p style='font-size: 14px;'>" + tableContent.toString() + "</p>"
				+ "</div>"
				+ "<hr style='border: none; height: 2px; background-color: rgb(240, 247, 255); margin: 20px 0;'>"
				+ "<div style='padding-top: 14px; padding-bottom: 14px; text-align: center; font-size: 14px;'>"
				+ "<p><b>Thank you.</b></p>" 
//				+ "<p style='text-transform: uppercase;'>" + "Assigt To"
//				+ clientBusinessName + "</p>" 
				+ "</div>" + "</div>"
				+ "<div style='text-align: center; padding: 8px; font-size: 14px; '>"
				//+ "<a href='https://www.interestbudsolutions.com/' style='text-decoration: none;'>"
				//+ "<img src='cid:fotter' alt='Footer' height='35' width='100'>" + "</a>" + "</div>"
				+ "<div><center><b>Powered By <a href='https://www.interestbudsolutions.com/' style='text-decoration: none;'>InterestBud Solutions Pvt. Ltd.</a></b></center></div>"
				+ "</div>";

		helper.setText(emailContent, true);
		helper.setFrom("support@interestbudsolutions.com", clientBusinessName + "From Ibs Leads");
		helper.setSubject(subject);
		helper.setTo(to);
		
//		FileSystemResource image = new FileSystemResource(new File("C:\\Users\\Ibs Nilesh\\nilesh_Workspace\\com.invoiceGenerator\\src\\main\\resources\\static\\images\\invoiceEmail.png"));
//		FileSystemResource image = new FileSystemResource(new File("/opt/javaProjects/ibsInvoice/images/invoiceEmail.jpg"));
//		helper.addInline("invoicementLogo", image, "image/png");

//		FileSystemResource image1 = new FileSystemResource(new File("C:\\Users\\Ibs Nilesh\\nilesh_Workspace\\com.invoiceGenerator\\src\\main\\resources\\static\\images\\invoicement.png"));
//		FileSystemResource image1 = new FileSystemResource(new File("/opt/javaProjects/ibsInvoice/images/invoicement.png"));
//		helper.addInline("fotter", image1, "image/png");

		if (ccRecipients == null) {
			ccRecipients = new ArrayList<String>();
			ccRecipients.add("");
		}

		for (String ccRecipient : ccRecipients) {
			System.out.println("ccRecipients : "+ccRecipients);
			if (!ccRecipient.isBlank()) {
				helper.addCc(ccRecipient);
			}
		}
		javaMailSender.send(message);
	}
	
	public void contactUs(ContactRequestDTO contactRequestDTO)
			throws MessagingException, UnsupportedEncodingException {
		List<String> ccRecipients = Arrays.asList("");
		//String to = "mukul@interestbudsolutions.com";
		String to = "ibsnilesh1998@gmail.com";
		String subject = "New contact from "+contactRequestDTO.getFullName();
		String content = "<p>Name : "+contactRequestDTO.getFullName() + ",</p>" 
		+ "<p>Email : "+contactRequestDTO.getEmail() +",</p>" 
		+ "<p>Phone : "+contactRequestDTO.getPhoneNumber()+ ",</p>"
		+ "<p>"+contactRequestDTO.getMessage()  +",</p>";
		emailSend(to, subject, content,ccRecipients);
		to = contactRequestDTO.getEmail();
		content = "<p>Dear <b>" + contactRequestDTO.getFullName() + "</b>,</p>"
                + "<p>Thank you for reaching out to us. We have received your message and appreciate your time and interest in our services."
                + "Our team will review your inquiry and get back to you as soon as possible. If your request is urgent, feel free to reach out to us directly at "
                + "<a  style='text-decoration:none; color:blue;'href='mailto:mukul@interestbudsolutions.com'>mukul@interestbudsolutions.com</a> "
                + "or call us at <a style='text-decoration:none; color:blur;' href='tel:+918076569119'>8076569119</a>.</p>"
                + "<p>In the meantime, you can explore more about us on our website: <a style='text-decoration:none; color:blue;' href='https://www.interestbudsolutions.com/'>InterestBud Solutions</a>."
                + "We appreciate your patience and look forward to assisting you!</p>"
                + "<p>Best Regards,<br>"
                + "<a href='https://www.interestbudsolutions.com/' style='text-decoration:none; color:blue;'>InterestBud Solutions Pvt Ltd.</a><br>"
                + "<a href='mailto:mukul@interestbudsolutions.com' style='text-decoration:none; color:blue;'>mukul@interestbudsolutions.com</a></p>";
		
		emailSend(contactRequestDTO.getEmail(), subject, content,ccRecipients);
		
	}
	
	
	public void emailSend(String to, String subject, String content, List<String> ccRecipients)
			throws MessagingException, UnsupportedEncodingException {

		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		// Integrate the table into the email content
		String emailContent = "<div style='background-color: rgb(240, 247, 255); padding: 20px;'>"
				+ "<div style='max-width: 600px; margin: 0 auto; background-color: rgb(255, 255, 255); border-radius: 5px;'>"
				//+ "<img src='cid:invoicementLogo' alt='Invoice Logo' style='display: block; width: 100%; border-top-left-radius: 5px; border-top-right-radius: 5px;'>"
				+ "<div style='padding: 20px;'>" + "<p style='font-size: 14px;'>" + content + "</p>"
				+ "</div>"
				+ "<hr style='border: none; height: 2px; background-color: rgb(240, 247, 255); margin: 20px 0;'>"
				+ "<div style='padding-top: 14px; padding-bottom: 14px; text-align: center; font-size: 14px;'>"
				//+ "<p><b>Thank you.</b></p>" 
//				+ "<p style='text-transform: uppercase;'>" + "Assigt To"
//				+ clientBusinessName + "</p>" 
				+ "</div>" + "</div>"
				+ "<div style='text-align: center; padding: 8px; font-size: 14px; '>"
				//+ "<a href='https://www.interestbudsolutions.com/' style='text-decoration: none;'>"
				//+ "<img src='cid:fotter' alt='Footer' height='35' width='100'>" + "</a>" + "</div>"
				+ "<div><center><b>Powered By <a href='https://www.interestbudsolutions.com/' style='text-decoration: none;'>InterestBud Solutions Pvt. Ltd.</a></b></center></div>"
				+ "</div>";

		helper.setText(emailContent, true);
		helper.setFrom("support@interestbudsolutions.com","Interestbud Contact Support");
		helper.setSubject(subject);
		helper.setTo(to);
		
//		FileSystemResource image = new FileSystemResource(new File("C:\\Users\\Ibs Nilesh\\nilesh_Workspace\\com.invoiceGenerator\\src\\main\\resources\\static\\images\\invoiceEmail.png"));
//		FileSystemResource image = new FileSystemResource(new File("/opt/javaProjects/ibsInvoice/images/invoiceEmail.jpg"));
//		helper.addInline("invoicementLogo", image, "image/png");

//		FileSystemResource image1 = new FileSystemResource(new File("C:\\Users\\Ibs Nilesh\\nilesh_Workspace\\com.invoiceGenerator\\src\\main\\resources\\static\\images\\invoicement.png"));
//		FileSystemResource image1 = new FileSystemResource(new File("/opt/javaProjects/ibsInvoice/images/invoicement.png"));
//		helper.addInline("fotter", image1, "image/png");

		if (ccRecipients == null) {
			ccRecipients = new ArrayList<String>();
			ccRecipients.add("");
		}

		for (String ccRecipient : ccRecipients) {
			System.out.println("ccRecipients : "+ccRecipients);
			if (!ccRecipient.isBlank()) {
				helper.addCc(ccRecipient);
			}
		}
		javaMailSender.send(message);
	}

}
