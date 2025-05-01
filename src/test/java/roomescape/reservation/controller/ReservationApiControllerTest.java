package roomescape.reservation.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.reservation.service.ReservationService;

@WebMvcTest(ReservationApiController.class)
class ReservationApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;

    private static final String URI = "/reservations";

    @DisplayName("예약 내역을 모두 조회한다")
    @Test
    void findAll() throws Exception {
        mockMvc.perform(get(URI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("추가하려는 예약 내역 속성에 빈 값이 있는 경우 예외가 발생한다")
    @Test
    void add() throws Exception {
        String requestBody = """
                {
                    "date": "",
                    "name": "neo",
                    "timeId": 1
                }
                """;

        mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("존재하지 않는 예약 내역 아이디를 삭제하는 경우 예외가 발생한다")
    @Test
    void deleteById() throws Exception {
        mockMvc.perform(delete(URI + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
