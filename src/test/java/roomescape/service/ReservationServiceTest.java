package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationRequest;
import roomescape.fake.FakeReservationDao;
import roomescape.fake.FakeReservationTimeDao;
import roomescape.fake.FakeThemeDao;

public class ReservationServiceTest {

    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        ReservationDao reservationDao = new FakeReservationDao();
        ReservationTimeDao reservationTimeDao = new FakeReservationTimeDao();
        ThemeDao themeDao = new FakeThemeDao();
        reservationService = new ReservationService(reservationDao, reservationTimeDao, themeDao);

        reservationTimeDao.addTime(new ReservationTime(1L, LocalTime.of(10, 0)));
        themeDao.addTheme(new Theme(1L, "레벨1", "탈출하기", "http://~"));
    }

    @Test
    @DisplayName("예약 추가를 할 수 있다.")
    void addReservation() {
        ReservationRequest request = new ReservationRequest(LocalDate.of(2024, 4, 26), "사나", 1L,
            1L);
        Reservation actual = reservationService.addReservation(request);

        assertAll(() -> {
            assertThat(actual.getId()).isEqualTo(1L);
            assertThat(actual.getName()).isEqualTo("사나");
            assertThat(actual.getDate()).isEqualTo(LocalDate.of(2024, 4, 26));
            assertThat(actual.getTime().getId()).isEqualTo(1L);
            assertThat(actual.getTheme().getId()).isEqualTo(1L);
        });
    }

    @Test
    @DisplayName("모든 예약 정보를 가져올 수 있다.")
    void findAllReservations() {
        ReservationRequest request1 = new ReservationRequest(LocalDate.of(2024, 4, 26), "사나", 1L,
            1L);
        ReservationRequest request2 = new ReservationRequest(LocalDate.of(2024, 4, 28), "프리", 1L,
            1L);

        reservationService.addReservation(request1);
        reservationService.addReservation(request2);

        assertThat(reservationService.findAllReservations()).hasSize(2);
    }

    @Test
    @DisplayName("예약을 id를 통해 제거할 수 있다.")
    void removeReservation() {
        ReservationRequest request = new ReservationRequest(LocalDate.of(2024, 4, 26), "사나", 1L,
            1L);
        reservationService.addReservation(request);

        reservationService.removeReservation(1L);

        assertThat(reservationService.findAllReservations()).hasSize(0);
    }
}
