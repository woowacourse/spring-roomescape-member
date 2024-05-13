package roomescape.reservation.service;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.exception.DuplicateReservationException;
import roomescape.exception.InvalidDateException;
import roomescape.exception.InvalidTimeException;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationServiceTest {

    private Member member;
    private Theme theme;

    @LocalServerPort
    private int port;

    @Autowired
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        member = new Member(1L, "김철수", "chulsoo@example.com", "123", Role.MEMBER);
        theme = new Theme(1L, "정글 모험", "열대 정글의 심연을 탐험하세요.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
    }

    @Test
    @DisplayName("지난 날짜에 대한 예약은 할 수 없다.")
    void validateFutureDateReservationTest() {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.MIN);
        Reservation reservation = new Reservation(1L, member, LocalDate.of(2000, 1, 1), reservationTime, theme);

        assertThatThrownBy(() -> reservationService.saveReservation(reservation))
                .isInstanceOf(InvalidDateException.class);
    }

    @Test
    @DisplayName("지난 시간에 대한 예약은 할 수 없다.")
    void validateFutureTimeReservationTest() {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(0, 0));
        Reservation reservation = new Reservation(1L, member, LocalDate.now(), reservationTime, theme);

        assertThatThrownBy(() -> reservationService.saveReservation(reservation))
                .isInstanceOf(InvalidTimeException.class);
    }

    @Test
    @DisplayName("날짜와 시간에 대한 중복 예약은 할 수 없다.")
    void validateUniqueReservationTest() {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        Reservation reservation = new Reservation(1L, member, LocalDate.of(3000, 1, 1), reservationTime, theme);

        reservationService.saveReservation(reservation);

        assertThatThrownBy(() -> reservationService.saveReservation(reservation))
                .isInstanceOf(DuplicateReservationException.class);
    }
}
