package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dao.InMemoryReservationDAO;
import roomescape.dao.InMemoryReservationTimeDAO;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class ReservationServiceTest {

    ReservationService reservationService;
    ReservationTimeService reservationTimeService;

    @BeforeEach
    void provideService() {
        ReservationTime time = new ReservationTime(LocalTime.of(10, 10));
        InMemoryReservationTimeDAO reservationTimeDAO = new InMemoryReservationTimeDAO(new ArrayList<>());
        reservationTimeService = new ReservationTimeService(reservationTimeDAO);
        long savedTimeId = reservationTimeDAO.insert(time);
        reservationService = new ReservationService(new InMemoryReservationDAO(new ArrayList<>()), reservationTimeDAO);
        reservationService.addReservation(new ReservationRequest("reservation", LocalDate.of(2025, 1, 1), savedTimeId));
    }

    @Test
    @DisplayName("같은 날짜 및 시간 예약이 존재하지 않을 경우, 예약 정보를 저장한 다음 id를 리턴한다")
    void saveReservation() {
        //given
        LocalDate date = LocalDate.of(2025, 4, 16);

        //when
        ReservationRequest reservationRequest = new ReservationRequest("test", date, 1L);
        Reservation actual = reservationService.addReservation(reservationRequest);

        //then
        assertAll(
                () -> assertThat(reservationService.findAll()).hasSize(2),
                () -> assertThat(actual.getId()).isEqualTo(2L)
        );

    }

    @Test
    @DisplayName("같은 날짜 및 시간 예약이 존재하면 -1을 리턴한다")
    void exceptionWhenSameDateTime() {
        //given
        LocalDate date = LocalDate.of(2025, 1, 1);
        long timeId = 1L;

        //when & then
        ReservationRequest duplicated = new ReservationRequest("test", date, timeId);
        assertThatThrownBy(() -> reservationService.addReservation(duplicated))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 같은 날짜/시간 예약이 존재합니다: date=%s, time=%s"
                        .formatted(date, reservationTimeService.findById(timeId).get().getStartAt()));
    }

    @Test
    @DisplayName("존재하는 예약을 삭제하면 true를 리턴한다")
    void removeReservationById() {
        //given
        LocalDate date = LocalDate.of(2025, 4, 16);
        ReservationTime time = reservationTimeService.findById(1L).get();
        long existedId = 1L;

        //when
        boolean actual = reservationService.removeReservationById(existedId);

        //then
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("존재하지 않는 예약을 삭제하려는 경우 false를 리턴한다")
    void removeNotExistReservationById() {
        //given
        long notExistId = 100L;

        //when
        boolean actual = reservationService.removeReservationById(notExistId);

        //then
        assertThat(actual).isFalse();
    }

    @Test
    @DisplayName("존재하지 않는 timeId인 경우 예외를 발생한다")
    void throwExceptionWhenNotExistTimeId() {
        //given
        long notExistTimeId = 100L;
        ReservationRequest request = new ReservationRequest("test", LocalDate.of(2025, 1, 1), notExistTimeId);

        //when & then
        assertThatThrownBy(() -> reservationService.addReservation(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 존재하지 않는 예약 가능 시간입니다: timeId=%d".formatted(notExistTimeId));
    }
}
