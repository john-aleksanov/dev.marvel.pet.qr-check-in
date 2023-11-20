package dev.marvel.qrcheckin.scheduledactivity;

import dev.marvel.qrcheckin.scheduledactivity.adapters.persistence.ScheduledActivityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ScheduledActivityServiceTest {

	@Mock
	private ScheduledActivityRepository repository;

	@InjectMocks
	private ScheduledActivityService uut;

	@Test
	void getCurrentScheduledActivity_delegatesToRepoCorrectly() {
		// GIVEN
		var classroomId = UUID.randomUUID();
		var expectedActivity = new ScheduledActivity();
		var expected = Collections.singleton(expectedActivity);
		when(repository.getScheduledActivity(classroomId)).thenReturn(expected);

		// WHEN
		var actual = uut.getCurrentScheduledActivity(classroomId);

		// THEN
		verify(repository).getScheduledActivity(classroomId);
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
	}

	@Test
	void getByUuid_delegatesToRepoCorrectly() {
		// GIVEN
		var id = UUID.randomUUID();
		var expected = new ScheduledActivity();
		when(repository.getById(id)).thenReturn(expected);

		// WHEN
		var actual = uut.getByUuid(id);

		// THEN
		verify(repository).getById(id);
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
	}
}
