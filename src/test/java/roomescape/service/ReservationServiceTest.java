package roomescape.service;


import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationName;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.DatabaseCleanupListener;
import roomescape.repository.JdbcReservationRepository;
import roomescape.repository.JdbcReservationTimeRepository;
import roomescape.repository.JdbcThemeRepository;
import roomescape.service.dto.ReservationRequestDto;

@TestExecutionListeners(value = {
        DatabaseCleanupListener.class,
        DependencyInjectionTestExecutionListener.class
})
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ReservationServiceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private JdbcReservationTimeRepository reservationTimeRepository;

    @Autowired
    private JdbcThemeRepository themeRepository;

    @Autowired
    private JdbcReservationRepository reservationRepository;

    @DisplayName("저장되어있지 않은 예약 시간에 예약을 시도하면 에러를 발생시킨다.")
    @Test
    void throw_exception_when_create_reservation_use_unsaved_time() {
        ReservationRequestDto requestDto = new ReservationRequestDto("재즈", 1L, "2024-05-07", 1L);

        assertThatThrownBy(() -> reservationService.createReservation(requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 하려는 시간이 저장되어 있지 않습니다.");
    }

    @DisplayName("이미 지나간 날짜에 예약을 시도하면 에러를 발생시킨다.")
    @Test
    void throw_exception_when_create_reservation_use_before_date() {
        reservationTimeRepository.insertReservationTime(new ReservationTime(1L, "11:00"));

        ReservationRequestDto requestDto = new ReservationRequestDto("재즈", 1L, "2024-01-01", 1L);

        assertThatThrownBy(() -> reservationService.createReservation(requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지나간 날짜와 시간에 대한 예약은 불가능합니다.");
    }

    @DisplayName("같은 테마를 같은 시간에 예약을 시도하면 에러를 발생시킨다.")
    @Test
    void throw_exception_when_create_reservation_use_same_theme_and_date_time() {
        ReservationName name = new ReservationName("러너덕");
        Theme theme = new Theme(1L, "공포", "공포는 무서워", "hi.jpg");
        ReservationDate date = new ReservationDate("2025-11-30");
        ReservationTime time = new ReservationTime(1L, "11:00");
        Reservation reservation = new Reservation(1L, name, theme, date, time);

        reservationTimeRepository.insertReservationTime(time);
        themeRepository.insertTheme(theme);
        reservationRepository.insertReservation(reservation);

        ReservationRequestDto requestDto = new ReservationRequestDto("재즈", 1L, "2025-11-30", 1L);

        assertThatThrownBy(() -> reservationService.createReservation(requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 테마는 같은 시간에 이미 예약이 존재합니다.");
    }

    @DisplayName("예약을 정상적으로 생성한다.")
    @Test
    void success_create_reservation() {
        Theme theme = new Theme(1L, "공포", "공포는 무서워", "hi.jpg");
        ReservationTime time = new ReservationTime(1L, "11:00");
        reservationTimeRepository.insertReservationTime(time);
        themeRepository.insertTheme(theme);

        ReservationRequestDto requestDto = new ReservationRequestDto("재즈", 1L, "2025-11-30", 1L);

        assertThatNoException()
                .isThrownBy(() -> reservationService.createReservation(requestDto));
    }

    @DisplayName("예약 삭제 시 저장되어있지 않은 아이디면 에러를 발생시킨다.")
    @Test
    void throw_exception_when_not_saved_reservation_id() {
        assertThatThrownBy(() -> reservationService.deleteReservation(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 아이디입니다.");
    }

    @DisplayName("예약을 정상적으로 삭제한다.")
    @Test
    void success_delete_reservation() {
        ReservationName name = new ReservationName("러너덕");
        Theme theme = new Theme(1L, "공포", "공포는 무서워", "hi.jpg");
        ReservationDate date = new ReservationDate("2025-11-30");
        ReservationTime time = new ReservationTime(1L, "11:00");
        Reservation reservation = new Reservation(1L, name, theme, date, time);

        reservationTimeRepository.insertReservationTime(time);
        themeRepository.insertTheme(theme);
        reservationRepository.insertReservation(reservation);

        assertThatNoException()
                .isThrownBy(() -> reservationService.deleteReservation(reservation.getId()));
    }
}
