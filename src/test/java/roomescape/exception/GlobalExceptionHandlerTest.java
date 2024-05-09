package roomescape.exception;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
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
import roomescape.controller.ReservationController;
import roomescape.service.AuthService;
import roomescape.service.LoginMemberService;
import roomescape.service.ReservationService;
import roomescape.service.dto.ReservationRequest;

@WebMvcTest(ReservationController.class)
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
    public void testInvalidReservationException() throws Exception {
        //given
        ObjectMapper objectMapper = new ObjectMapper();
        ReservationRequest reservationRequest = new ReservationRequest(
                "lily", "2222-01-12", 1, 1
        );
        String jsonContent = objectMapper.writeValueAsString(reservationRequest);

        doThrow(new InvalidReservationException()).when(reservationService).create(any());

        //when&then
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(
                        result -> assertInstanceOf(InvalidReservationException.class, result.getResolvedException()));
    }

    @Test
    public void testMethodArgumentNotValidException() throws Exception {
        //given
        ObjectMapper objectMapper = new ObjectMapper();
        ReservationRequest reservationRequest = new ReservationRequest(
                "", "2222-01-12", 1, 1
        );
        String jsonContent = objectMapper.writeValueAsString(reservationRequest);

        //when&then
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class,
                        result.getResolvedException()));
    }

    @Test
    public void testNullPointerException() throws Exception {
        //given
        ObjectMapper objectMapper = new ObjectMapper();
        ReservationRequest reservationRequest = new ReservationRequest(
                "lily", "2222-01-12", 1, 1
        );
        String jsonContent = objectMapper.writeValueAsString(reservationRequest);

        doThrow(new NullPointerException()).when(reservationService).create(any());

        //when&then
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(NullPointerException.class,
                        result.getResolvedException()));
    }
}
