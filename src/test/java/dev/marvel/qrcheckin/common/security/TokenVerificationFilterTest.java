package dev.marvel.qrcheckin.common.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenVerificationFilterTest {

	@Mock
	private TokenService tokenService;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private FilterChain filterChain;

	@Mock
	private PrintWriter writer;

	@InjectMocks
	private TokenVerificationFilter uut;

	@Test
	void doFilterInternal_withNoToken() throws ServletException, IOException {
		// GIVEN
		when(request.getCookies()).thenReturn(null);
		when(response.getWriter()).thenReturn(writer);

		// WHEN
		uut.doFilterInternal(request, response, filterChain);

		// THEN
		verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
		verify(writer).write("{\"message\": \"Invalid or missing token\"}");
		verify(filterChain, never()).doFilter(request, response);
	}

	@Test
	void doFilterInternal_withInvalidToken() throws ServletException, IOException {
		// GIVEN
		var token = "11111111-1111-1111-1111-111111111111";
		var cookies = new Cookie[]{new Cookie("token", token)};
		when(tokenService.validateAndExpire(UUID.fromString(token))).thenReturn(false);
		when(response.getWriter()).thenReturn(writer);
		when(request.getCookies()).thenReturn(cookies);

		// WHEN
		uut.doFilterInternal(request, response, filterChain);

		// THEN
		verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
		verify(writer).write("{\"message\": \"Invalid or missing token\"}");
		verify(filterChain, never()).doFilter(request, response);
	}

	@Test
	void doFilterInternal_withValidToken() throws ServletException, IOException {
		// GIVEN
		var cookies = new Cookie[]{new Cookie("token", UUID.randomUUID().toString())};
		when(request.getCookies()).thenReturn(cookies);
		when(tokenService.validateAndExpire(any(UUID.class))).thenReturn(true);

		// WHEN
		uut.doFilterInternal(request, response, filterChain);

		// THEN
		verify(filterChain).doFilter(request, response);
		verify(response, never()).setStatus(HttpServletResponse.SC_BAD_REQUEST);
	}

	@Test
	void shouldNotFilter_filtersCorrectServletPath() {
		// GIVEN
		when(request.getServletPath()).thenReturn("/v1/check-in");

		// WHEN
		var actual = uut.shouldNotFilter(request);

		// THEN
		assertThat(actual).isFalse();
	}

	@Test
	void shouldNotFilter_doesNotFilterIncorrectServletPath() {
		// GIVEN
		when(request.getServletPath()).thenReturn("/v1/scheduled-activities");

		// WHEN
		var actual = uut.shouldNotFilter(request);

		// THEN
		assertThat(actual).isTrue();
	}
}