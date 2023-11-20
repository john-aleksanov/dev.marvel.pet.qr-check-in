package dev.marvel.qrcheckin.common.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Servlet filter for setting a one-time token in the response cookie.
 * This filter generates a token for a get scheduled activities request and adds it as a cookie in the response
 * for future validations.
 */
@Component
@RequiredArgsConstructor
public class TokenSettingFilter extends OncePerRequestFilter {

	private static final String SCHEDULED_ACTIVITIES_ENDPOINT = "/v1/scheduled-activities";
	private static final String COOKIE_PATH = "/qr-check-in/v1";
	private static final int COOKIE_EXPIRATION_TIME_IN_SECONDS = 10;

	private final TokenService tokenService;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		var servletPath = request.getServletPath();
		return !servletPath.startsWith(SCHEDULED_ACTIVITIES_ENDPOINT);
	}

	/**
	 * Processes the request and response by generating a token and setting it as a cookie.
	 * If the response status is not 200, the token is expired and removed.
	 *
	 * @param request     The HttpServletRequest object.
	 * @param response    The HttpServletResponse object.
	 * @param filterChain The FilterChain object.
	 * @throws ServletException If an exception occurs that interferes with the filter's normal operation.
	 * @throws IOException      If an I/O related error has occurred during the processing.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		var token = tokenService.generateToken();
		var cookie = generateCookie(token);
		response.addCookie(cookie);
		filterChain.doFilter(request, response);
		if (response.getStatus() != 200) {
			tokenService.validateAndExpire(token);
		}
	}

	private Cookie generateCookie(UUID token) {
		var cookie = new Cookie("token", token.toString());
		cookie.setHttpOnly(true);
		cookie.setPath(COOKIE_PATH);
		cookie.setMaxAge(COOKIE_EXPIRATION_TIME_IN_SECONDS);
		return cookie;
	}
}
