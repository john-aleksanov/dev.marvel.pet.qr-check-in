package dev.marvel.qrcheckin.common.security;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TokenServiceTest {

	private final TokenService uut = new TokenService();

	@Test
	void generateToken_shouldGenerateAndStoreToken() {
		// WHEN
		var generatedToken = uut.generateToken();

		// THEN
		assertThat(generatedToken).isNotNull();
		var isValid = uut.validateAndExpire(generatedToken);
		assertThat(isValid).isTrue();
	}

	@Test
	void validateAndExpire_shouldExpireTokenCorrectly() {
		// GIVEN
		var token = uut.generateToken();

		// WHEN
		var firstValidation = uut.validateAndExpire(token);

		// THEN
		assertThat(firstValidation).isTrue();
		boolean secondValidation = uut.validateAndExpire(token);
		assertThat(secondValidation).isFalse();
	}
}