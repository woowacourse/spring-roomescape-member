package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.fixture.LocalDateFixture.AFTER_ONE_DAYS_DATE;
import static roomescape.fixture.LocalDateFixture.AFTER_TWO_DAYS_DATE;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.reservation.domain.Reservation;
import roomescape.domain.reservation.service.AdminReservationService;
import roomescape.domain.reservationTime.domain.ReservationTime;
import roomescape.domain.theme.domain.Theme;
import roomescape.domain.reservation.dto.ReservationAddRequest;
import roomescape.exception.ClientIllegalArgumentException;
import roomescape.reservationTime.service.FakeReservationTimeRepository;
import roomescape.theme.service.FakeThemeRepository;

class AdminReservationServiceTest {

    AdminReservationService adminReservationService;
    FakeReservationRepository fakeReservationRepository;
    FakeReservationTimeRepository fakeReservationTimeRepository;
    FakeThemeRepository fakeThemeRepository;

    @BeforeEach
    void setUp() {
        fakeReservationRepository = new FakeReservationRepository();
        fakeReservationTimeRepository = new FakeReservationTimeRepository();
        fakeThemeRepository = new FakeThemeRepository();
        adminReservationService = new AdminReservationService(fakeReservationRepository, fakeReservationTimeRepository,
                fakeThemeRepository);
    }

    @DisplayName("없는 id의 예약을 삭제하면 예외를 발생합니다.")
    @Test
    void should_throw_ClientIllegalArgumentException_when_remove_reservation_with_non_exist_id() {
        assertThatThrownBy(() -> adminReservationService.removeReservation(1L))
                .isInstanceOf(ClientIllegalArgumentException.class)
                .hasMessage("해당 id를 가진 예약이 존재하지 않습니다.");
    }

    @DisplayName("존재하지 않는 예약시각으로 예약 시 예외가 발생합니다.")
    @Test
    void should_throw_ClientIllegalArgumentException_when_reserve_non_exist_time() {
        LocalDate reservationDate = AFTER_TWO_DAYS_DATE;
        ReservationAddRequest reservationAddRequest = new ReservationAddRequest(reservationDate, "dodo", 1L, 1L);

        assertThatThrownBy(() -> adminReservationService.addReservation(reservationAddRequest))
                .isInstanceOf(ClientIllegalArgumentException.class)
                .hasMessage("존재 하지 않는 예약시각으로 예약할 수 없습니다.");
    }

    @DisplayName("예약 날짜와 예약시각 그리고 테마 아이디가 같은 경우 예외를 발생합니다.")
    @Test
    void should_throw_ClientIllegalArgumentException_when_reserve_date_and_time_duplicated() {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(12, 0));
        Theme theme = new Theme(1L, "dummy", "description", "url");

        Reservation reservation = new Reservation(null, "lib", AFTER_ONE_DAYS_DATE, reservationTime, theme);
        fakeReservationRepository.insert(reservation);

        ReservationAddRequest conflictRequest = new ReservationAddRequest(AFTER_ONE_DAYS_DATE, "dodo",
                1L, 1L);

        assertThatThrownBy(() -> adminReservationService.addReservation(conflictRequest))
                .isInstanceOf(ClientIllegalArgumentException.class)
                .hasMessage("예약 날짜와 예약시간 그리고 테마가 겹치는 예약은 할 수 없습니다.");
    }
}
