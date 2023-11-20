package dev.marvel.qrcheckin.checkin.adapters.web;

import dev.marvel.qrcheckin.checkin.CheckIn;
import dev.marvel.qrcheckin.checkin.CheckInService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing classroom check-ins.
 * Provides endpoints for handling check-in operations.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/check-in")
public class CheckInController {

	private final CheckInService checkInService;

	/**
	 * Handles a POST request to perform a check-in. The check-in details are provided in the request body.
	 *
	 * @param request A CheckIn object containing the details of the check-in.
	 */
	@PostMapping
	public void performCheckIn(@RequestBody CheckIn request) {
		checkInService.performCheckIn(request);
	}
}
