package dev.marvel.qrcheckin.scheduledactivity.adapters.web;

import dev.marvel.qrcheckin.scheduledactivity.ScheduledActivity;
import dev.marvel.qrcheckin.scheduledactivity.ScheduledActivityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ScheduledActivityControllerTest {

	@Mock
	private ScheduledActivityService service;

	@InjectMocks
	private ScheduledActivityController uut;

	@Test
	void getScheduledActivities_delegatesToService() {
		// GIVEN
		var classroomId = UUID.randomUUID();
		var expectedActivity = new ScheduledActivity();
		var expected = Collections.singleton(expectedActivity);
		when(service.getCurrentScheduledActivity(classroomId)).thenReturn(expected);

		// WHEN
		var actual = uut.getScheduledActivities(classroomId);

		// THEN
		verify(service).getCurrentScheduledActivity(classroomId);
	}
}
