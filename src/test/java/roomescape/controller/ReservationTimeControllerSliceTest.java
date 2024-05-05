package roomescape.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.domain.ReservationTime;
import roomescape.dto.SaveReservationTimeRequest;
import roomescape.service.ReservationTimeService;

import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationTimeController.class)
class ReservationTimeControllerSliceTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReservationTimeService reservationTimeService;

    @DisplayName("전체 예약 시간 정보를 조회한다.")
    @Test
    void getReservationTimesTest() throws Exception {
        // Given
        final List<ReservationTime> reservationTimes = List.of(
                new ReservationTime(1L, LocalTime.now().plusHours(3)),
                new ReservationTime(2L, LocalTime.now().plusHours(4)),
                new ReservationTime(3L, LocalTime.now().plusHours(5))
        );
        given(reservationTimeService.getReservationTimes()).willReturn(reservationTimes);

        // When & Then
        mockMvc.perform(get("/times"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @DisplayName("예약 시간 정보를 저장한다.")
    @Test
    void saveReservationTimeTest() throws Exception {
        final SaveReservationTimeRequest saveReservationTimeRequest = new SaveReservationTimeRequest(LocalTime.now().plusHours(3));
        final ReservationTime savedReservationTime = new ReservationTime(1L, LocalTime.now().plusHours(3));
        given(reservationTimeService.saveReservationTime(saveReservationTimeRequest)).willReturn(savedReservationTime);

        // When & Then
        mockMvc.perform(post("/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(saveReservationTimeRequest))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }


    @DisplayName("예약 시간 정보를 삭제한다.")
    @Test
    void deleteReservationTimeTest() throws Exception {
        // Given
        final long reservationTimeId = 1;
        willDoNothing().given(reservationTimeService).deleteReservationTime(reservationTimeId);

        // When & Then
        mockMvc.perform(delete("/times/" + reservationTimeId))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @DisplayName("존재하지 않는 예약 시간 정보를 삭제하려고 하면 400코드가 응답된다.")
    @Test
    void deleteNoExistReservationTimeTest() throws Exception {
        // Given
        final long reservationTimeId = 1;
        doThrow(new NoSuchElementException("해당 id의 예약 시간이 존재하지 않습니다."))
                .when(reservationTimeService)
                .deleteReservationTime(reservationTimeId);

        // When & Then
        mockMvc.perform(delete("/times/" + reservationTimeId))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
