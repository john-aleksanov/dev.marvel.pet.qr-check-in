package dev.marvel.qrcheckin.checkin.adapters.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.marvel.qrcheckin.checkin.CheckIn;
import dev.marvel.qrcheckin.checkin.CheckInService;
import dev.marvel.qrcheckin.common.security.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Import(TokenService.class)
@WebMvcTest(controllers = CheckInController.class)
class CheckInControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CheckInService checkInService;

	@Test
	void performCheckIn_callsCheckInService() throws Exception {
		// GIVEN
		var checkInRequest = CheckIn.builder()
				.studentId(UUID.fromString("11111111-1111-1111-1111-111111111111"))
				.scheduledActivityId(UUID.fromString("22222222-2222-2222-2222-222222222222"))
				.build();
		var mapper = new ObjectMapper();
		var requestBody = mapper.writeValueAsString(checkInRequest);

		// WHEN-THEN
		mockMvc.perform(post("/v1/check-in").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isOk());
		verify(checkInService).performCheckIn(checkInRequest);
	}
}