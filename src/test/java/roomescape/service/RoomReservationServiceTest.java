package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation.Reservation;
import roomescape.domain.Reservation.ReservationCommand;
import roomescape.domain.ReservationTime.ReservationTime;
import roomescape.domain.ReservationTime.ReservationTimeCommand;
import roomescape.exception.ErrorMessage;
import roomescape.exception.NotFoundResourceException;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationTime.ReservationTimeRepository;

public class RoomReservationServiceTest {
    private ReservationRepository createReservationRepository() {
        return new ReservationRepository() {
            @Override
            public List<Reservation> getAllReservation() {
                return List.of();
            }

            @Override
            public Reservation addReservation(ReservationCommand reservationCommand, ReservationTime reservationTime) {
                return new Reservation(1, reservationCommand.name(), reservationCommand.date(), reservationTime);
            }

            @Override
            public void deleteReservation(long id) {

            }

            @Override
            public boolean existsByTimeId(long timeId) {
                return false;
            }
        };
    }

    private ReservationTimeRepository createReservationTimeRepository(ReservationTime reservationTime) {
        return new ReservationTimeRepository() {
            @Override
            public ReservationTime addReservationTime(ReservationTimeCommand reservationTimeCommand) {
                return null;
            }

            @Override
            public Optional<ReservationTime> getReservationTime(long id) {
                if(reservationTime == null) {
                   return Optional.empty();
                }
                return Optional.of(reservationTime);
            }

            @Override
            public List<ReservationTime> getAllReservationTime() {
                return List.of();
            }

            @Override
            public void deleteReservationTime(long id) {

            }
        };
    }

    @Test
    @DisplayName("예약 생성 시 유효한 시간 ID인 경우 정상 작동 테스트")
    void addReservationTest() {
        ReservationTime reservationTime = new ReservationTime(1, "10:00");
        RoomReservationService reservationService = new RoomReservationService(createReservationRepository(), createReservationTimeRepository(reservationTime));
        ReservationCommand reservationCommand = new ReservationCommand("브라운", "2023-08-05", 1);

        Reservation reservation = reservationService.addReservation(reservationCommand);

        assertThat(reservation).isEqualTo(new Reservation(1, "브라운", "2023-08-05", reservationTime));
    }

    @Test
    @DisplayName("예약 생성 시 존재하지 않는 시간ID인 경우 예외 테스트")
    void addReservationFailTest() {
        RoomReservationService reservationService = new RoomReservationService(createReservationRepository(), createReservationTimeRepository(null));
        ReservationCommand reservationCommand = new ReservationCommand("브라운", "2023-08-05", 1);

        assertThatThrownBy(() -> reservationService.addReservation(reservationCommand))
                .isExactlyInstanceOf(NotFoundResourceException.class)
                .hasMessage(ErrorMessage.INVALID_RESERVATION_TIME_ID.getMessage());
    }
}
