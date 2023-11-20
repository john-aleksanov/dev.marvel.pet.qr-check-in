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
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

/**
 * Servlet filter for token verification.
 * This filter checks for the presence and validity of a one-time token in the request cookies.
 */
@Component
@RequiredArgsConstructor
public class TokenVerificationFilter extends OncePerRequestFilter {

	private static final String CHECK_IN_ENDPOINT = "/v1/check-in";
	private final TokenService tokenService;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		var servletPath = request.getServletPath();
		return !servletPath.startsWith(CHECK_IN_ENDPOINT);
	}

	/**
	 * Processes the request by verifying the token.
	 * If the token is valid, the filter chain continues; otherwise, it returns an error response to guard from
	 * tampered-with check-ins
	 *
	 * @param request The HttpServletRequest object.
	 * @param response The HttpServletResponse object.
	 * @param filterChain The FilterChain object.
	 * @throws ServletException If an exception occurs that interferes with the filter's normal operation.
	 * @throws IOException If an I/O related error has occurred during the processing.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		var isTokenValid = Optional.ofNullable(request.getCookies())
				.map(Arrays::stream)
				.flatMap(stream -> stream.filter(cookie -> "token".equals(cookie.getName()))
						.findFirst())
				.map(Cookie::getValue)
				.map(UUID::fromString)
				.map(tokenService::validateAndExpire)
				.orElse(false);
		if (isTokenValid) {
			filterChain.doFilter(request, response);
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.setContentType("application/json");
			response.getWriter().write("{\"message\": \"Invalid or missing token\"}");
		}
	}
}
