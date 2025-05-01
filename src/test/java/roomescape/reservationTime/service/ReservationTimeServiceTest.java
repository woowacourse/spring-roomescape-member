package roomescape.reservationTime.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.reservation.service.FakeReservationRepository;
import roomescape.reservation.service.FakeReservationTimeRepository;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.domain.ReservationTimeRepository;
import roomescape.reservationTime.dto.TimeConditionRequest;
import roomescape.reservationTime.dto.TimeConditionResponse;
import roomescape.theme.domain.Theme;

class ReservationTimeServiceTest {
    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void beforeEach() {
        ReservationTime reservationTime1 = ReservationTime.createWithoutId(LocalTime.of(10, 0));
        ReservationTime reservationTime2 = ReservationTime.createWithoutId(LocalTime.of(11, 0));
        Theme theme = Theme.createWithId(1L, "테마", "테마", "테마");

        ReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository();
        Long id = reservationTimeRepository.save(reservationTime1);
        reservationTimeRepository.save(reservationTime2);

        reservationTime1 = reservationTimeRepository.findById(id);
        ReservationRepository reservationRepository = new FakeReservationRepository();
        reservationRepository.save(Reservation.createWithoutId("홍길동", LocalDate.of(2024, 10, 6), reservationTime1, theme));

        reservationTimeService = new ReservationTimeService(reservationRepository, reservationTimeRepository);
    }

    @DisplayName("이미 존재하는 예약이 있는 경우 예약 시간을 삭제할 수 없다.")
    @Test
    void can_not_delete_when_reservation_exists() {
        Assertions.assertThatThrownBy(() -> reservationTimeService.deleteReservationTimeById(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예약 가능 시간 조회 테스트")
    @Test
    void time_condition_test() {
        LocalDate localDate = LocalDate.of(2024, 10, 6);
        Long themeId = 1L;

        List<TimeConditionResponse> responses = reservationTimeService.getTimesWithCondition(
                new TimeConditionRequest(localDate, themeId));

        Assertions.assertThat(responses).containsExactlyInAnyOrder(
                new TimeConditionResponse(1L, LocalTime.of(10, 0), true),
                new TimeConditionResponse(2L, LocalTime.of(11, 0), false)
                );
    }
}
