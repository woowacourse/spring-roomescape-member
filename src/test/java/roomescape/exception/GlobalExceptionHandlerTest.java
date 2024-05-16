package roomescape.exception;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import roomescape.controller.ReservationsController;
import roomescape.service.AuthService;
import roomescape.service.LoginMemberService;
import roomescape.service.ReservationService;
import roomescape.service.dto.AdminReservationRequest;

@WebMvcTest(ReservationsController.class)
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private AuthService authService;

    @MockBean
    private LoginMemberService loginMemberService;

    @Test
    public void testMethodArgumentNotValidException() throws Exception {
        //given
        ObjectMapper objectMapper = new ObjectMapper();
        AdminReservationRequest adminReservationRequest = new AdminReservationRequest(
                "", 1, 1, 1
        );
        String jsonContent = objectMapper.writeValueAsString(adminReservationRequest);

        //when&then
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class,
                        result.getResolvedException()));
    }
}
