package ru.kslacker.cats.microservices.restapi.handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ru.kslacker.cats.microservices.restapi.security.UserDetailsImpl;

// TODO
@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		String redirect = request.getContextPath();
		if (userDetails.isAdmin()) {
			redirect = "admin";
		} else if (userDetails.isUser()) {
			redirect = "personal";
		}

		response.sendRedirect(redirect);
	}
}
