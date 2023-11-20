package dev.marvel.qrcheckin.scheduledactivity.adapters.persistence;

import dev.marvel.qrcheckin.common.exceptions.NotFoundException;
import dev.marvel.qrcheckin.scheduledactivity.ScheduledActivity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Repository class for handling operations related to ScheduledActivity entities.
 * This class uses JPA's EntityManager to interact with the underlying datasource.
 */
@Repository
@RequiredArgsConstructor
public class ScheduledActivityRepository {

	static final String GET_SCHEDULED_ACTIVITIES_QUERY = """
			    SELECT sa
			      FROM ScheduledActivityJpaEntity sa
			     WHERE sa.classroom.id = :classroomId
			       AND sa.startTime BETWEEN :startTime AND :endTime
			""";
	static final String GET_BY_ID_QUERY = """
				SELECT sa
				  FROM ScheduledActivityJpaEntity sa
				 WHERE sa.id = :id
			""";

	@PersistenceContext
	private final EntityManager em;

	/**
	 * Retrieves a set of currently scheduled activities for a given classroom.
	 * The time range for currently scheduled activities is 15 minutes before and after the current time.
	 * The code deliberately returns a set (not a single scheduled activity) to leave it to the caller how to handle
	 * multiple current scheduled activities.
	 *
	 * @param classroomId The UUID of the classroom.
	 * @return A set of ScheduledActivity objects.
	 */
	public Set<ScheduledActivity> getScheduledActivity(UUID classroomId) {
		var now = LocalDateTime.now();
		var startTimeAfter = now.minusMinutes(15);
		var startTimeBefore = now.plusMinutes(15);

		return em.createQuery(GET_SCHEDULED_ACTIVITIES_QUERY, ScheduledActivityJpaEntity.class)
				.setParameter("classroomId", classroomId)
				.setParameter("startTime", startTimeAfter)
				.setParameter("endTime", startTimeBefore)
				.getResultList()
				.stream()
				.map(ScheduledActivity::new)
				.collect(Collectors.toSet());
	}

	/**
	 * Retrieves a scheduled activity by its ID.
	 *
	 * @param id The UUID of the scheduled activity.
	 * @return The ScheduledActivity object.
	 * @throws NotFoundException if no scheduled activity is found with the provided ID.
	 */
	public ScheduledActivity getById(UUID id) {
		return em.createQuery(GET_BY_ID_QUERY, ScheduledActivityJpaEntity.class)
				.setParameter("id", id)
				.getResultList()
				.stream()
				.findFirst()
				.map(ScheduledActivity::new)
				.orElseThrow(() -> new NotFoundException("Scheduled activity " + id + " not found"));
	}
}
