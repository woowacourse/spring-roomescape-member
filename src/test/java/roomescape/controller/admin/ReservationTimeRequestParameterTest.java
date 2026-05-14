package roomescape.controller.admin;

import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomescapeException;
import roomescape.service.ReservationTimeService;

@WebMvcTest(ReservationTimeController.class)
class ReservationTimeRequestParameterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationTimeService reservationTimeService;

    @Test
    @DisplayName("예약 시간이 null인 경우 예약 시간 생성에 실패한다.")
    void failCreate_WhenStartAtIsNull() throws Exception {
        mockMvc.perform(post("/admin/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "startAt": null
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("예약 시간이 HH:mm 형식이 아닌 경우 예약 시간 생성에 실패한다.")
    void failCreate_WhenStartAtFormatIsInvalid() throws Exception {
        mockMvc.perform(post("/admin/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "startAt": "10:00:00"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간 삭제 시 에러 응답을 반환한다.")
    void failDelete_WhenReservationTimeNotFound() throws Exception {
        willThrow(new RoomescapeException(ErrorCode.RESERVATION_TIME_NOT_FOUND))
                .given(reservationTimeService)
                .deleteReservationTime(1L);

        mockMvc.perform(delete("/admin/times/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("예약이 존재하는 예약 시간 삭제 시 에러 응답을 반환한다.")
    void failDelete_WhenReservationTimeInUse() throws Exception {
        willThrow(new RoomescapeException(ErrorCode.RESERVATION_TIME_IN_USE))
                .given(reservationTimeService)
                .deleteReservationTime(1L);

        mockMvc.perform(delete("/admin/times/1"))
                .andExpect(status().isConflict());
    }
}
