package dev.marvel.qrcheckin.scheduledactivity;

import dev.marvel.qrcheckin.scheduledactivity.adapters.persistence.ScheduledActivityJpaEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduledActivity {
	private UUID classroomId;
	private String classroomName;
	private UUID activityId;
	private String activityName;
	private LocalDateTime startTime;
	private LocalDateTime endTime;

	public ScheduledActivity(ScheduledActivityJpaEntity saje) {
		this.classroomId = saje.getClassroom().getId();
		this.classroomName = saje.getClassroom().getName();
		this.activityId = saje.getActivity().getId();
		this.activityName = saje.getActivity().getName();
		this.startTime = saje.getStartTime();
		this.endTime = saje.getEndTime();
	}
}
