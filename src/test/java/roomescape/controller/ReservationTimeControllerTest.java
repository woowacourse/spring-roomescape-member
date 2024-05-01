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
import roomescape.domain.ReservationTime;
import roomescape.dto.app.ReservationTimeAppRequest;
import roomescape.dto.web.ReservationTimeWebRequest;
import roomescape.dto.web.ReservationTimeWebResponse;
import roomescape.service.ReservationTimeService;

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
        LocalTime time = LocalTime.now();
        ReservationTime reservationTime = new ReservationTime(id, time);

        when(reservationTimeService.save(new ReservationTimeAppRequest(time.toString())))
            .thenReturn(reservationTime);

        String requestBody = objectMapper.writeValueAsString(new ReservationTimeWebRequest(id, time.toString()));
        String responseBody = objectMapper.writeValueAsString(new ReservationTimeWebResponse(id, time));

        mvc.perform(post("/times")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(content().json(responseBody));
    }

    @DisplayName("예약 시간을 삭제한다 -> 204")
    @Test
    void deleteBy() throws Exception {
        long id = 1L;
        LocalTime time = LocalTime.now();
        ReservationTime reservationTime = new ReservationTime(id, time);

        when(reservationTimeService.save(new ReservationTimeAppRequest(time.toString())))
            .thenReturn(reservationTime);

        mvc.perform(delete("/times/" + reservationTime.getId()))
            .andExpect(status().isNoContent());
    }

    @DisplayName("예약 시간을 조회한다. -> 200")
    @Test
    void getReservationTimes() throws Exception {
        mvc.perform(get("/times"))
            .andExpect(status().isOk());
    }

    @DisplayName("예약 시간 포맷이 잘못될 경우 -> 400")
    @Test
    void create_IllegalTimeFormat() throws Exception {
        String illegalTime = "24:00";
        String requestBody = objectMapper.writeValueAsString(new ReservationTimeWebRequest(1L, illegalTime));

        when(reservationTimeService.save(new ReservationTimeAppRequest(illegalTime)))
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
        LocalTime time = LocalTime.now();
        ReservationTime reservationTime = new ReservationTime(timeId, time);

        when(reservationTimeService.delete(timeId))
            .thenThrow(IllegalArgumentException.class);

        mvc.perform(delete("/times/" + reservationTime.getId()))
            .andExpect(status().isBadRequest());
    }

    @DisplayName("이미 존재하는 시간을 저장 -> 400")
    @Test
    void create_Duplicate() throws Exception {
        String rawTime = "19:00";
        when(reservationTimeService.save(new ReservationTimeAppRequest(rawTime)))
            .thenThrow(IllegalArgumentException.class);

        String requestBody = objectMapper.writeValueAsString(new ReservationTimeWebRequest(4L, rawTime));

        mvc.perform(post("/times")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }
}

