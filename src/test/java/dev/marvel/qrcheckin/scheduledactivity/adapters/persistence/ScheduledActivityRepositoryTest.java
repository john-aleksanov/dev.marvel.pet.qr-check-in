package dev.marvel.qrcheckin.scheduledactivity.adapters.persistence;

import dev.marvel.qrcheckin.common.exceptions.NotFoundException;
import dev.marvel.qrcheckin.scheduledactivity.ScheduledActivity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import static dev.marvel.qrcheckin.scheduledactivity.adapters.persistence.ScheduledActivityRepository.GET_BY_ID_QUERY;
import static dev.marvel.qrcheckin.scheduledactivity.adapters.persistence.ScheduledActivityRepository.GET_SCHEDULED_ACTIVITIES_QUERY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScheduledActivityRepositoryTest {

	@Mock
	private EntityManager em;

	@Mock
	private TypedQuery<ScheduledActivityJpaEntity> query;

	@InjectMocks
	private ScheduledActivityRepository uut;

	@Test
	void getScheduledActivity_returnsActivities() {
		// GIVEN
		var activityId = UUID.fromString("11111111-1111-1111-1111-111111111111");
		var classroomId = UUID.fromString("22222222-2222-2222-2222-222222222222");
		var now = LocalDateTime.now();
		var startTime = now.plusMinutes(10);
		var endTime = now.plusMinutes(50);
		var jpaEntity = ScheduledActivityJpaEntity.builder()
				.activity(ActivityJpaEntity.builder()
						.id(activityId)
						.name("Activity")
						.build())
				.classroom(ClassroomJpaEntity.builder()
						.id(classroomId)
						.name("Classroom")
						.build())
				.startTime(startTime)
				.endTime(endTime)
				.build();
		when(em.createQuery(GET_SCHEDULED_ACTIVITIES_QUERY, ScheduledActivityJpaEntity.class)).thenReturn(query);
		when(query.setParameter("classroomId", classroomId)).thenReturn(query);
		when(query.setParameter(eq("startTime"), any(LocalDateTime.class))).thenReturn(query);
		when(query.setParameter(eq("endTime"), any(LocalDateTime.class))).thenReturn(query);
		when(query.getResultList()).thenReturn(Collections.singletonList(jpaEntity));
		var expectedScheduledActivity = ScheduledActivity.builder()
				.activityId(activityId)
				.activityName("Activity")
				.classroomId(classroomId)
				.classroomName("Classroom")
				.startTime(startTime)
				.endTime(endTime)
				.build();

		// WHEN
		var activities = uut.getScheduledActivity(classroomId);

		// THEN
		verify(em).createQuery(GET_SCHEDULED_ACTIVITIES_QUERY, ScheduledActivityJpaEntity.class);
		assertThat(activities).hasSize(1);
		var actualScheduledActivity = new ArrayList<>(activities).get(0);
		assertThat(actualScheduledActivity).usingRecursiveComparison().isEqualTo(expectedScheduledActivity);
	}

	@Test
	void getById_whenNotExistsThrowsNotFoundException() {
		// GIVEN
		var id = UUID.randomUUID();
		when(em.createQuery(GET_BY_ID_QUERY, ScheduledActivityJpaEntity.class)).thenReturn(query);
		when(query.setParameter("id", id)).thenReturn(query);
		when(query.getResultList()).thenReturn(Collections.emptyList());

		// THEN
		assertThatThrownBy(() -> uut.getById(id))
				.isInstanceOf(NotFoundException.class)
				.hasMessageContaining("Scheduled activity " + id + " not found");
	}
}