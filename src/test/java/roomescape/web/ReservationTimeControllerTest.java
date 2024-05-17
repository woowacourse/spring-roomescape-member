package roomescape.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.web.api.resolver.AdminAuthValidateInterceptor;
import roomescape.web.api.resolver.MemberAuthValidateInterceptor;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReservationTimeControllerTest extends ExcludeInterceptorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberAuthValidateInterceptor memberAuthValidateInterceptor;
    @MockBean
    private AdminAuthValidateInterceptor adminAuthValidateInterceptor;

    @DisplayName("예약 시간을 추가한다")
    @Test
    void when_postReservationTime_then_created() throws Exception {
        // given
        String requestReservationTime = "{\"startAt\": \"10:00\"}";

        // when, then
        mockMvc.perform(post("/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestReservationTime))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @DisplayName("동일한 예약 시간을 두번 추가하면, 예외가 발생한다")
    @Test
    void when_postDuplicateReservationTime_then_exception() throws Exception {
        // given
        String requestReservationTime = "{\"startAt\": \"10:00\"}";

        // when, then
        mockMvc.perform(post("/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestReservationTime))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestReservationTime))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("모든 예약 시간을 조회한다")
    @Sql("/test-data/reservation-times.sql")
    @Test
    void when_getReservationTimes_then_returnReservationTimes() throws Exception {
        // when, then
        mockMvc.perform(get("/times"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationTimes", hasSize(12)));
    }

    @DisplayName("예약 시간을 삭제한다")
    @Sql("/test-data/reservation-times.sql")
    @Test
    void when_deleteReservationTime_then_noContent() throws Exception {
        // when, then
        mockMvc.perform(delete("/times/1"))
                .andExpect(status().isNoContent());
    }

    @DisplayName("예약 가능한 시간을 조회한다")
    @Sql(value = {"/test-data/reservation-times.sql", "/test-data/themes.sql", "/test-data/members.sql",
            "/test-data/reservations.sql"})
    @Test
    void when_getAvailableTimes_then_returnAvailableTimes() throws Exception {
        // when, then
        mockMvc.perform(get("/times/available")
                        .param("date", "2099-07-01")
                        .param("themeId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationTimes[?(@.alreadyBooked == false)]", hasSize(7)))
                .andExpect(jsonPath("$.reservationTimes[?(@.alreadyBooked == true)]", hasSize(5)));
    }
}
