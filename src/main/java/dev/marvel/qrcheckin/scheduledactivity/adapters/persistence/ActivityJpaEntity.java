package dev.marvel.qrcheckin.scheduledactivity.adapters.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "activities")
public class ActivityJpaEntity {

	@Id
	private UUID id;

	@Column(name = "activity_name")
	private String name;
}
