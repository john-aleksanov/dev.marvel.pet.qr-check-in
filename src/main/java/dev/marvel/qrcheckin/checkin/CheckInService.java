package dev.marvel.qrcheckin.checkin;

import dev.marvel.qrcheckin.scheduledactivity.ScheduledActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service layer class for handling check-in operations.
 * This service manages the storage and validation of check-in entities.
 */
@Service
@RequiredArgsConstructor
public class CheckInService {

	private final Set<CheckIn> checkins = Collections.newSetFromMap(new ConcurrentHashMap<>());
	private final ScheduledActivityService scheduledActivityService;

	/**
	 * Performs a check-in operation for a given CheckIn domain object.
	 * This method validates the scheduled activity associated with the check-in
	 * and adds the check-in to the internal set of check-ins (a shortcut for persistence).
	 *
	 * @param checkin The CheckIn object containing the details of the check-in to be performed.
	 */
	public void performCheckIn(CheckIn checkin) {
		var scheduledActivity = scheduledActivityService.getByUuid(checkin.getScheduledActivityId());
		checkins.add(checkin);
	}
}
