package dev.marvel.qrcheckin.scheduledactivity.adapters.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "scheduled_activities")
public class ScheduledActivityJpaEntity {

	@Id
	private UUID id;

	@ManyToOne
	private ClassroomJpaEntity classroom;

	@ManyToOne
	private ActivityJpaEntity activity;

	private LocalDateTime startTime;
	private LocalDateTime endTime;

}
