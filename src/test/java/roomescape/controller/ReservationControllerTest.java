package roomescape.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean; // ✅ 최신 API 임포트
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import roomescape.controller.dto.ReservationPatchRequest;
import roomescape.controller.dto.ReservationPutRequest;
import roomescape.controller.dto.ReservationRequest;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.domain.TimeSlot;
import roomescape.service.ReservationService;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // ✅ Deprecated된 @MockBean 대신 Spring 3.4+ 표준인 @MockitoBean 사용
    @MockitoBean
    private ReservationService reservationService;

    @Test
    void createReservation() throws Exception {
        ReservationRequest request = new ReservationRequest("브라운", LocalDate.now(), 1L, 1L);
        given(reservationService.saveReservation(any(), any(), any(), any()))
                .willReturn(createMockReservation());

        performPost("/reservations", request)
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void createReservationWithInvalidData() throws Exception {
        ReservationRequest request = new ReservationRequest("", LocalDate.now(), 1L, 1L);

        performPost("/reservations", request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateReservation() throws Exception {
        ReservationPutRequest request = new ReservationPutRequest("브라운", LocalDate.now(), 1L, 1L);
        given(reservationService.findReservationById(anyLong()))
                .willReturn(createMockReservation());

        performPut("/reservations/1", request)
                .andExpect(status().isOk());
    }

    @Test
    void patchReservation() throws Exception {
        ReservationPatchRequest request = new ReservationPatchRequest("네오", null, null, null);
        given(reservationService.findReservationById(anyLong()))
                .willReturn(createMockReservation());

        performPatch("/reservations/1", request)
                .andExpect(status().isOk());
    }

    @Test
    void deleteReservation() throws Exception {
        performDelete("/reservations/1")
                .andExpect(status().isNoContent());
    }

    private ResultActions performPost(String url, Object request) throws Exception {
        return mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    }

    private ResultActions performPut(String url, Object request) throws Exception {
        return mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    }

    private ResultActions performPatch(String url, Object request) throws Exception {
        return mockMvc.perform(patch(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    }

    private ResultActions performDelete(String url) throws Exception {
        return mockMvc.perform(delete(url));
    }

    private Reservation createMockReservation() {
        TimeSlot timeSlot = new TimeSlot(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "테마", "설명", "url");
        return new Reservation(1L, "브라운", LocalDate.now(), timeSlot, theme);
    }
}
