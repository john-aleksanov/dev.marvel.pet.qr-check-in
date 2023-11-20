package dev.marvel.qrcheckin.common.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenSettingFilterTest {
	@Mock
	private TokenService tokenService;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private FilterChain filterChain;

	@Captor
	private ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);

	@InjectMocks
	private TokenSettingFilter uut;

	@Test
	void doFilterInternal_addsCookie() throws Exception {
		// GIVEN
		var token = UUID.randomUUID();
		when(tokenService.generateToken()).thenReturn(token);
		when(response.getStatus()).thenReturn(200);
		var expectedCookie = new Cookie("token", token.toString());
		expectedCookie.setHttpOnly(true);
		expectedCookie.setPath("/qr-check-in/v1");
		expectedCookie.setMaxAge(10);

		// WHEN
		uut.doFilterInternal(request, response, filterChain);

		// THEN
		verify(filterChain).doFilter(request, response);
		verify(response).addCookie(cookieCaptor.capture());
		var actualCookie = cookieCaptor.getValue();
		assertThat(actualCookie).usingRecursiveComparison().isEqualTo(expectedCookie);
	}

	@ParameterizedTest
	@MethodSource("errorStatusCodesProvider")
	void doFilterInternal_expiresCookieForErrorResponses(HttpStatus httpStatus) throws Exception {
		// GIVEN
		var token = UUID.randomUUID();
		when(tokenService.generateToken()).thenReturn(token);
		when(response.getStatus()).thenReturn(httpStatus.value());

		// WHEN
		uut.doFilterInternal(request, response, filterChain);

		// THEN
		verify(filterChain).doFilter(request, response);
		verify(tokenService).validateAndExpire(token);
	}

	private static List<HttpStatus> errorStatusCodesProvider() {
		return Arrays.stream(HttpStatus.values())
				.filter(status -> status.is4xxClientError() || status.is5xxServerError())
				.collect(Collectors.toList());
	}

	@Test
	void shouldNotFilter_filtersCorrectServletPath() {
		// GIVEN
		when(request.getServletPath()).thenReturn("/v1/scheduled-activities");

		// WHEN
		var actual = uut.shouldNotFilter(request);

		// THEN
		assertThat(actual).isFalse();
	}

	@Test
	void shouldNotFilter_doesNotFilterIncorrectServletPath() {
		// GIVEN
		when(request.getServletPath()).thenReturn("/v1/check-in");

		// WHEN
		var actual = uut.shouldNotFilter(request);

		// THEN
		assertThat(actual).isTrue();
	}
}