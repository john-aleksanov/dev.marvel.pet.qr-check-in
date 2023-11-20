package dev.marvel.qrcheckin.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for managing security tokens.
 * This service handles the generation, validation, and expiration of tokens.
 */
@Component
@RequiredArgsConstructor
public class TokenService {

	private final Set<UUID> tokens = Collections.newSetFromMap(new ConcurrentHashMap<>());

	/**
	 * Generates a new unique token and stores it.
	 * This method is synchronized to ensure thread-safe operation.
	 *
	 * @return UUID The generated unique token.
	 */
	public synchronized UUID generateToken() {
		var token = UUID.randomUUID();
		tokens.add(token);
		return token;
	}

	/**
	 * Validates and expires a given token.
	 * This method checks if the token exists and removes it from storage, effectively expiring it.
	 * This method is synchronized to ensure thread-safe operation.
	 *
	 * @param token The UUID of the token to validate and expire.
	 * @return boolean True if the token was valid and has been expired, false otherwise.
	 */
	public synchronized boolean validateAndExpire(UUID token) {
		return tokens.remove(token);
	}
}
