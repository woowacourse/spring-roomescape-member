package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Email;
import roomescape.domain.Name;
import roomescape.dto.request.ReservationAddMemberRequest;
import roomescape.dto.request.ReservationAddRequest;
import roomescape.dto.response.AuthResponse;
import roomescape.dto.response.MemberResponse;
import roomescape.dto.response.ReservationResponse;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static roomescape.InitialDataFixture.*;
import static roomescape.InitialDataFixture.MEMBER_1;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("/initial_test_data.sql")
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("예약을 추가하고 id값을 붙여서 응답 DTO를 생성한다.")
    void addReservation() {
        ReservationAddRequest reservationAddRequest = new ReservationAddRequest(LocalDate.now().plusDays(1), 1L, 1L);
        ReservationAddMemberRequest reservationAddMemberRequest = new ReservationAddMemberRequest(new MemberResponse(MEMBER_1));
        ReservationResponse reservationResponse = reservationService.addReservation(reservationAddRequest, reservationAddMemberRequest);

        assertThat(reservationResponse.id()).isNotNull();
    }

    @Test
    @DisplayName("존재하지 않는 time_id로 예약을 추가하면 예외를 발생시킨다.")
    void addReservationInvalidGetTimeGetId() {
        ReservationAddRequest reservationAddRequest = new ReservationAddRequest(LocalDate.now().plusDays(1), -1L, 1L);
        ReservationAddMemberRequest reservationAddMemberRequest = new ReservationAddMemberRequest(new MemberResponse(MEMBER_1));
        assertThatThrownBy(() -> reservationService.addReservation(reservationAddRequest, reservationAddMemberRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("id에 맞는 예약을 삭제한다.")
    void deleteReservation() {
        reservationService.deleteReservation(1L);

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation", Integer.class);
        assertThat(count).isOne();
    }

    @Test
    @DisplayName("모든 예약들을 조회한다.")
    void getReservations() {
        List<ReservationResponse> reservations = reservationService.findReservations();

        assertThat(reservations).hasSize(2);
    }

    @Test
    @DisplayName("같은 날짜, 시간, 테마에 예약을 하는 경우 예외를 발생시킨다.")
    void saveSameReservation() {
        ReservationAddRequest reservationAddRequest = new ReservationAddRequest(
                RESERVATION_2.getDate(),
                RESERVATION_2.getTime().getId(),
                RESERVATION_2.getTheme().getId());
        assertThatThrownBy(() -> reservationService.addReservation(reservationAddRequest, new ReservationAddMemberRequest(new MemberResponse(MEMBER_1)))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("시간과 날짜만 같고 테마가 다른 경우 예약에 성공한다.")
    void saveOnlySameGetDateGetTime() {
        ReservationAddRequest reservationAddRequest = new ReservationAddRequest(
                RESERVATION_2.getDate(),
                RESERVATION_2.getTime().getId(),
                THEME_2.getId());
        assertThatNoException().isThrownBy(() -> reservationService.addReservation(reservationAddRequest, new ReservationAddMemberRequest(new MemberResponse(MEMBER_1))));
    }

    @Test
    @DisplayName("테마가 같고 날짜가 다른 경우 예약에 성공한다.")
    void saveOnlySameTheme() {
        ReservationAddRequest reservationAddRequest = new ReservationAddRequest(
                RESERVATION_2.getDate().plusDays(1),
                RESERVATION_2.getTime().getId(),
                RESERVATION_2.getTheme().getId());
        assertThatNoException().isThrownBy(() -> reservationService.addReservation(reservationAddRequest, new ReservationAddMemberRequest(new MemberResponse(MEMBER_1))));
    }
}
