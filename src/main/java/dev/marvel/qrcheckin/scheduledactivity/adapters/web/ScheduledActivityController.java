package dev.marvel.qrcheckin.scheduledactivity.adapters.web;

import dev.marvel.qrcheckin.scheduledactivity.ScheduledActivity;
import dev.marvel.qrcheckin.scheduledactivity.ScheduledActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;

/**
 * REST controller for managing scheduled activities.
 * This controller provides endpoints for retrieving scheduled activities.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/scheduled-activities")
public class ScheduledActivityController {

	private final ScheduledActivityService scheduledActivityService;

	/**
	 * Retrieves a set of currently scheduled activities for a given classroom.
	 *
	 * @param classroomId The UUID of the classroom for which to retrieve scheduled activities.
	 * @return A set of ScheduledActivity objects representing the scheduled activities for the specified classroom.
	 */
	@GetMapping("/{classroomId}")
	public Set<ScheduledActivity> getScheduledActivities(@PathVariable UUID classroomId) {
		return scheduledActivityService.getCurrentScheduledActivity(classroomId);
	}
}
