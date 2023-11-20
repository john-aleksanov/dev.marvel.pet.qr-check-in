package dev.marvel.qrcheckin.scheduledactivity;

import dev.marvel.qrcheckin.scheduledactivity.adapters.persistence.ScheduledActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

/**
 * Service layer for managing scheduled activities.
 * This service provides methods to interact with scheduled activity data.
 */
@Service
@RequiredArgsConstructor
public class ScheduledActivityService {

	private final ScheduledActivityRepository repository;

	/**
	 * Retrieves the current set of scheduled activities for a specified classroom.
	 * The definition of 'current' activities is those that have started or about to start 15 minutes before / after
	 * the current time.
	 * The code deliberately returns a set (not a single scheduled activity) to leave it to the caller how to handle
	 * multiple current scheduled activities.
	 *
	 * @param classroomId The UUID of the classroom for which to retrieve scheduled activities.
	 * @return A set of ScheduledActivity objects representing the current scheduled activities.
	 */
	public Set<ScheduledActivity> getCurrentScheduledActivity(UUID classroomId) {
		return repository.getScheduledActivity(classroomId);
	}

	/**
	 * Retrieves a specific scheduled activity by its UUID.
	 *
	 * @param id The UUID of the scheduled activity.
	 * @return The ScheduledActivity object corresponding to the specified UUID.
	 * @throws dev.marvel.qrcheckin.common.exceptions.NotFoundException if the activity is not found.
	 */
	public ScheduledActivity getByUuid(UUID id) {
		return repository.getById(id);
	}
}
