package roomescape.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.domain.member.Role;
import roomescape.web.api.dto.ReservationMemberRequest;
import roomescape.web.api.resolver.MemberArgumentResolver;
import roomescape.web.api.resolver.Principal;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReservationControllerTest extends ExcludeInterceptorTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    MemberArgumentResolver memberArgumentResolver;

    @DisplayName("예약을 생성한다")
    @Sql(value = {"/test-data/reservation-times.sql", "/test-data/themes.sql", "/test-data/members.sql"})
    @Test
    void when_createReservation_then_created() throws Exception {
        // given
        Principal principal = new Principal(1L, "pkpkpkpk@woowa.net", Role.ADMIN);

        // setting
        doReturn(principal).when(memberArgumentResolver).resolveArgument(any(), any(), any(), any());

        // given
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        ReservationMemberRequest reservationMemberRequest = new ReservationMemberRequest(tomorrow.toString(), 1L, 1L);

        // when, then
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationMemberRequest)))
                .andExpect(status().isCreated());
    }

    @DisplayName("과거 시간에 대한 예약을 하면, 예외가 발생한다")
    @Sql(value = {"/test-data/reservation-times.sql", "/test-data/themes.sql", "/test-data/members.sql"})
    @Test
    void when_createReservationWithPastTime_then_badRequest() throws Exception {
        // given
        Principal principal = new Principal(1L, "pkpkpkpk@woowa.net", Role.ADMIN);

        // setting
        doReturn(principal).when(memberArgumentResolver).resolveArgument(any(), any(), any(), any());

        // given
        LocalDate yesterday = LocalDate.now().minusDays(1);
        ReservationMemberRequest reservationMemberRequest = new ReservationMemberRequest(yesterday.toString(), 1L, 1L);

        // when, then
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationMemberRequest)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("이미 예약된 테마와 시간을 예약을 하면, 예외가 발생한다")
    @Sql(value = {"/test-data/reservation-times.sql", "/test-data/themes.sql", "/test-data/members.sql"})
    @Test
    void when_createDuplicateReservation_then_badRequest() throws Exception {
        // given
        Principal principal = new Principal(1L, "pkpkpkpk@woowa.net", Role.ADMIN);

        // setting
        doReturn(principal).when(memberArgumentResolver).resolveArgument(any(), any(), any(), any());

        // given
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        String requestReservation = "{\"date\":\"" + tomorrow + "\",\"timeId\":1,\"themeId\":1}";

        // when, then
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestReservation))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestReservation))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("예약을 조회한다")
    @Sql(value = {"/test-data/reservation-times.sql", "/test-data/themes.sql", "/test-data/members.sql", "/test-data/reservations.sql"})
    @Test
    void when_findAllReservations_then_ok() throws Exception {
        // given
        Principal principal = new Principal(1L, "pkpkpkpk@woowa.net", Role.ADMIN);

        // setting
        doReturn(principal).when(memberArgumentResolver).resolveArgument(any(), any(), any(), any());

        // when, then
        mockMvc.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservations", hasSize(5)));
    }

    @DisplayName("조건에 맞는 예약을 조회한다")
    @Sql(value = {"/test-data/reservation-times.sql", "/test-data/themes.sql", "/test-data/members.sql", "/test-data/reservations.sql"})
    @Test
    void when_findByCondition_then_ok() throws Exception {
        // given
        Principal principal = new Principal(1L, "pkpkpkpk@woowa.net", Role.ADMIN);

        // setting
        doReturn(principal).when(memberArgumentResolver).resolveArgument(any(), any(), any(), any());

        // when, then
        mockMvc.perform(get("/admin/reservations?memberId=1&themeId=1&dateFrom=2099-07-01&dateTo=2099-07-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservations", hasSize(2)));
    }

    @DisplayName("예약을 삭제한다")
    @Sql(value = {"/test-data/reservation-times.sql", "/test-data/themes.sql", "/test-data/members.sql", "/test-data/reservations.sql"})
    @Test
    void when_deleteReservation_then_noContent() throws Exception {
        // given
        Principal principal = new Principal(1L, "pkpkpkpk@woowa.net", Role.ADMIN);

        // setting
        doReturn(principal).when(memberArgumentResolver).resolveArgument(any(), any(), any(), any());

        // when, then
        mockMvc.perform(delete("/reservations/1"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservations", hasSize(4)));
    }
}
