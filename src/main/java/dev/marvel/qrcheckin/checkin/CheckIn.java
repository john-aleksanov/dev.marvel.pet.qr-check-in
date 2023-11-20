package dev.marvel.qrcheckin.checkin;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@EqualsAndHashCode
public class CheckIn {
	private UUID studentId;
	private UUID scheduledActivityId;
}
