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
@Table(name = "classrooms")
public class ClassroomJpaEntity {

	@Id
	private UUID id;

	@Column(name = "classroom_name")
	private String name;
}
