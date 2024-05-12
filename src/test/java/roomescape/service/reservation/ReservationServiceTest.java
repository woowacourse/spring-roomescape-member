package roomescape.service.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.restassured.RestAssured;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.member.MemberInfo;
import roomescape.domain.member.Role;
import roomescape.dto.member.SignupRequest;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.dto.reservation.ReservationSearchRequest;
import roomescape.dto.theme.ThemeRequest;
import roomescape.dto.time.TimeRequest;
import roomescape.service.member.MemberService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/test_schema.sql"})
public class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationTimeService timeService;

    @Autowired
    private ReservationThemeService themeService;

    @Autowired
    private MemberService memberService;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        memberService.insertMember(new SignupRequest("email@email.com", "password", "name"));
        timeService.insertTime(new TimeRequest(LocalTime.parse("10:00")));
        themeService.insertTheme(new ThemeRequest("name", "desc", "thumb"));
        RestAssured.port = port;
    }

    @DisplayName("모든 예약을 조회한다.")
    @Test
    void getAllReservations() {
        // given
        reservationService.insertUserReservation(new ReservationRequest(LocalDate.parse("2025-01-01"), 1L, 1L),
                new MemberInfo(1L, "name", Role.USER));

        // when
        List<ReservationResponse> reservations = reservationService.getAllReservations();

        // then
        assertThat(reservations.size()).isEqualTo(1);
    }

    @DisplayName("예약을 추가한다.")
    @Test
    void insertReservation() {
        // given
        ReservationResponse response = reservationService.insertUserReservation(
                new ReservationRequest(LocalDate.parse("2025-01-01"), 1L, 1L), new MemberInfo(1L, "name", Role.USER));

        // when
        Long insertedReservationId = response.id();

        // then
        assertThat(insertedReservationId).isEqualTo(1L);
    }

    @DisplayName("동일한 날짜, 시간, 테마에 대한 예약은 불가능하다.")
    @Test
    void insertDuplicateReservation() {
        // given
        reservationService.insertUserReservation(new ReservationRequest(LocalDate.parse("2025-01-01"), 1L, 1L),
                new MemberInfo(1L, "name", Role.USER));

        // when
        ReservationRequest request = new ReservationRequest(LocalDate.parse("2025-01-01"), 1L, 1L);
        MemberInfo member = new MemberInfo(2L, "name1", Role.USER);

        // then
        assertThatThrownBy(() -> reservationService.insertUserReservation(request, member))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 해당 시간에 예약이 존재합니다.");
    }

    @DisplayName("지난 날짜에 대한 예약은 불가능하다.")
    @Test
    void insertPastDate() {
        ReservationRequest request = new ReservationRequest(LocalDate.now().minusDays(1), 1L, 1L);
        MemberInfo member = new MemberInfo(1L, "name", Role.USER);

        assertThatThrownBy(() -> reservationService.insertUserReservation(request, member))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지난 시간으로는 예약할 수 없습니다.");
    }

    @DisplayName("지난 시간에 대한 예약은 불가능하다.")
    @Test
    void insertPastTime() {
        // given
        timeService.insertTime(new TimeRequest(LocalTime.now().minusHours(1)));

        // when
        ReservationRequest request = new ReservationRequest(LocalDate.now(), 2L, 1L);
        MemberInfo member = new MemberInfo(1L, "name", Role.USER);

        // then
        assertThatThrownBy(
                () -> reservationService.insertUserReservation(request, member))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지난 시간으로는 예약할 수 없습니다.");
    }

    @DisplayName("동일한 날짜, 시간, 테마에 대한 예약은 불가능하다.")
    @Test
    void insertSameReservation() {
        // given
        reservationService.insertUserReservation(new ReservationRequest(LocalDate.parse("2025-01-01"), 1L, 1L),
                new MemberInfo(1L, "name",
                        Role.USER));

        // when
        ReservationRequest request = new ReservationRequest(LocalDate.parse("2025-01-01"), 1L, 1L);
        MemberInfo member = new MemberInfo(1L, "name", Role.USER);

        // then
        assertThatThrownBy(() -> reservationService.insertUserReservation(request, member))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 해당 시간에 예약이 존재합니다.");
    }

    @DisplayName("ID로 예약을 삭제한다.")
    @Test
    void deleteById() {
        // given
        reservationService.insertUserReservation(new ReservationRequest(LocalDate.parse("2025-01-01"), 1L, 1L),
                new MemberInfo(1L, "name",
                        Role.USER));

        // when
        reservationService.deleteReservation(1L);

        // then
        assertThat(reservationService.getAllReservations()).hasSize(0);
    }

    @DisplayName("특정 조건에 대한 모든 예약을 조회한다.")
    @Test
    @Sql(scripts = {"/test_schema.sql", "/test_reservation_search.sql"})
    void searchReservation() {
        ReservationSearchRequest request = new ReservationSearchRequest(
                1L, 1L, LocalDate.now().minusDays(7), LocalDate.now().minusDays(1)
        );
        List<ReservationResponse> reservations = reservationService.searchReservation(request);

        assertThat(reservations).hasSize(7);
        assertThat(reservations).extracting("id").containsExactly(1L, 2L, 3L, 4L, 5L, 6L, 7L);
        assertThat(reservations).extracting("member.name").containsOnly("test_member1");
    }
}
