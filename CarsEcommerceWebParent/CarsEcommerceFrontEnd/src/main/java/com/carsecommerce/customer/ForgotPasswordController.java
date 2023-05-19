package com.carsecommerce.customer;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.carsecommerce.Utility;
import com.carsecommerce.common.entity.Customer;
import com.carsecommerce.setting.EmailSettingBag;
import com.carsecommerce.setting.SettingService;

@Controller
public class ForgotPasswordController {
	@Autowired private CustomerService customerService;
	@Autowired private SettingService settingService;

	@GetMapping("/forgot_password")
	public String showRequestForm() {
		return "customer/forgot_password_form";
	}

	@PostMapping("/forgot_password")
	public String processRequestForm(HttpServletRequest request, Model model) {
		String email = request.getParameter("email");
		try {
			String token = customerService.updateResetPasswordToken(email);
			String link = Utility.getSiteURL(request) + "/reset_password?token=" + token;
			sendEmail(link, email);

			model.addAttribute("message", "Ne kemi dërguar një lidhje të rivendosjes së fjalëkalimit në emailin tuaj."
					+ " Ju lutem hapeni.");
		} catch (CustomerNotFoundException e) {
			model.addAttribute("error", e.getMessage());
		} catch (UnsupportedEncodingException | MessagingException e) {
			model.addAttribute("error", "Nuk mund të dërgohej email.");
		}

		return "customer/forgot_password_form";
	}

	private void sendEmail(String link, String email) 
			throws UnsupportedEncodingException, MessagingException {
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);

		String toAddress = email;
		String subject = "Këtu është lidhja për të rivendosur fjalëkalimin tuaj.";

		String content = "<p>Përshëndetje,</p>"
				+ "<p>Ju keni kërkuar të rivendosni fjalëkalimin tuaj.</p>"
				+ "Klikoni në lidhjen e mëposhtme për të ndryshuar fjalëkalimin tuaj:</p>"
				+ "<p><a href=\"" + link + "\">Ndrysho Fjalkalimin</a></p>"
				+ "<br>"
				+ "<p>Injorojeni këtë email nëse e mbani mend fjalëkalimin tuaj, "
				+ "ose nëse nuk e keni bërë kërkesën.</p>";

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);		

		helper.setText(content, true);
		mailSender.send(message);
	}

	@GetMapping("/reset_password")
	public String showResetForm(@Param("token") String token, Model model) {
		Customer customer = customerService.getByResetPasswordToken(token);
		if (customer != null) {
			model.addAttribute("token", token);
		} else {
			model.addAttribute("pageTitle", "Token i pavlefshëm");
			model.addAttribute("message", "Token i pavlefshëm");
			return "message";
		}

		return "customer/reset_password_form";
	}

	@PostMapping("/reset_password")
	public String processResetForm(HttpServletRequest request, Model model) {
		String token = request.getParameter("token");
		String password = request.getParameter("password");

		try {
			customerService.updatePassword(token, password);

			model.addAttribute("pageTitle", "Fjalkalimi u ndryshua");
			model.addAttribute("title", "Fjalkalimi u ndryshua");
			model.addAttribute("message", "Ju keni ndryshuar me sukses fjalëkalimin tuaj.");

		} catch (CustomerNotFoundException e) {
			model.addAttribute("pageTitle", "Token i pavlefshëm");
			model.addAttribute("message", e.getMessage());
		}	

		return "message";		
	}
}
