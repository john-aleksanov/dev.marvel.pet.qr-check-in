package dev.marvel.qrcheckin.checkin;


import dev.marvel.qrcheckin.scheduledactivity.ScheduledActivity;
import dev.marvel.qrcheckin.scheduledactivity.ScheduledActivityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckInServiceTest {

	@Mock
	private ScheduledActivityService scheduledActivityService;

	@InjectMocks
	private CheckInService uut;

	@Test
	void performCheckIn_AddsCheckIn() {
		// GIVEN
		var scheduledActivityId = UUID.randomUUID();
		var studentId = UUID.randomUUID();
		var checkIn = CheckIn.builder()
				.studentId(studentId)
				.scheduledActivityId(scheduledActivityId)
				.build();

		var scheduledActivity = new ScheduledActivity();
		when(scheduledActivityService.getByUuid(scheduledActivityId)).thenReturn(scheduledActivity);


		// WHEN
		uut.performCheckIn(checkIn);

		// THEN
		verify(scheduledActivityService).getByUuid(scheduledActivityId);
	}
}