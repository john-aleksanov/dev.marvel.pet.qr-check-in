package dev.marvel.qrcheckin.scheduledactivity.adapters.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.marvel.qrcheckin.common.security.TokenService;
import dev.marvel.qrcheckin.scheduledactivity.ScheduledActivity;
import dev.marvel.qrcheckin.scheduledactivity.ScheduledActivityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Import(TokenService.class)
@WebMvcTest(controllers = ScheduledActivityController.class)
class ScheduledActivityControllerMvcTest {

	private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ScheduledActivityService scheduledActivityService;

	@Test
	void getScheduledActivities_returnsActivities() throws Exception {
		// GIVEN
		var classroomId = UUID.randomUUID();
		var expectedActivity = ScheduledActivity.builder()
				.activityId(UUID.fromString("11111111-1111-1111-1111-11111111111"))
				.activityName("Activity")
				.classroomId(UUID.fromString("22222222-2222-2222-2222-222222222222"))
				.classroomName("ClassroomName")
				.startTime(LocalDateTime.now().plusMinutes(10))
				.endTime(LocalDateTime.now().plusMinutes(50))
				.build();
		var expectedActivities = Collections.singleton(expectedActivity);
		when(scheduledActivityService.getCurrentScheduledActivity(classroomId)).thenReturn(expectedActivities);

		// WHEN
		var responseContent = mockMvc.perform(get("/v1/scheduled-activities/" + classroomId))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();
		var actual = mapper.readValue(responseContent, new TypeReference<Set<ScheduledActivity>>() {
		});

		// THEN
		assertThat(actual).usingRecursiveComparison().isEqualTo(expectedActivities);
	}
}