package roomescape.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.controller.request.ReservationTimeWebRequest;
import roomescape.controller.response.ReservationTimeWebResponse;
import roomescape.domain.vo.ReservationTime;
import roomescape.exception.ReservationExistsException;
import roomescape.service.ReservationTimeService;
import roomescape.service.request.ReservationTimeAppRequest;
import roomescape.service.response.ReservationTimeAppResponse;

@WebMvcTest(ReservationTimeController.class)
class ReservationTimeControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ReservationTimeService reservationTimeService;

    @DisplayName("예약 시간을 저장한다 -> 201")
    @Test
    void create() throws Exception {
        long id = 1L;
        String time = LocalTime.now().toString();
        ReservationTime reservationTime = new ReservationTime(id, time);
        ReservationTimeAppResponse appResponse = ReservationTimeAppResponse.from(reservationTime);

        when(reservationTimeService.save(new ReservationTimeAppRequest(time)))
            .thenReturn(appResponse);

        String requestBody = objectMapper.writeValueAsString(new ReservationTimeWebRequest(time));
        String responseBody = objectMapper.writeValueAsString(ReservationTimeWebResponse.from(appResponse));

        mvc.perform(post("/times")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(content().json(responseBody));
    }

    @DisplayName("예약 시간을 삭제한다 -> 204")
    @Test
    void deleteBy() throws Exception {
        mvc.perform(delete("/times/" + 1L))
            .andExpect(status().isNoContent());
    }

    @DisplayName("예약 시간을 조회한다. -> 200")
    @Test
    void getReservationTimes() throws Exception {
        mvc.perform(get("/times"))
            .andExpect(status().isOk());
    }

    @DisplayName("예약 시간 포맷이 잘못되거나, 중복 될 경우 -> 400")
    @Test
    void create_IllegalTimeFormat() throws Exception {
        String requestBody = objectMapper.writeValueAsString(new ReservationTimeWebRequest("20:00"));

        when(reservationTimeService.save(new ReservationTimeAppRequest("20:00")))
            .thenThrow(IllegalArgumentException.class);

        mvc.perform(post("/times")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @DisplayName("예약이 존재하는 시간 삭제 -> 400")
    @Test
    void delete_ReservationExists() throws Exception {
        long timeId = 1L;

        when(reservationTimeService.delete(timeId))
            .thenThrow(ReservationExistsException.class);

        mvc.perform(delete("/times/" + timeId))
            .andExpect(status().isBadRequest());
    }

    @DisplayName("요청 포맷이 잘못될 경우 -> 400")
    @Test
    void create_MethodArgNotValid() throws Exception {
        String requestBody = objectMapper.writeValueAsString(new ReservationTimeWebRequest(null));

        mvc.perform(post("/times")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }
}

