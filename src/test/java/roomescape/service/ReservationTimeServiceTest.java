package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationCommand;
import roomescape.domain.reservationTime.ReservationTime;
import roomescape.domain.reservationTime.ReservationTimeCommand;
import roomescape.domain.reservationTime.ReservationTimeCondition;
import roomescape.domain.reservationTime.ReservationTimeWithAvailable;
import roomescape.domain.theme.Theme;
import roomescape.exception.DataReferencedException;
import roomescape.exception.ErrorMessage;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationTime.ReservationTimeRepository;

public class ReservationTimeServiceTest {
    private ReservationRepository createReservationRepository(boolean isExistTime) {
        return new ReservationRepository() {

            @Override
            public Reservation addReservation(ReservationCommand reservationCommand, ReservationTime reservationTime, Theme theme) {
                return null;
            }

            @Override
            public void deleteReservation(long id) {

            }

            @Override
            public boolean existsByTimeId(long timeId) {
                return isExistTime;
            }

            @Override
            public boolean existsByThemeId(long themeId) {
                return false;
            }

            @Override
            public boolean existsByTimeIdAndThemeIdAndDate(long timeId, long themeId, LocalDate date) {
                return false;
            }

            @Override
            public List<Reservation> getAllReservation(String name) {
                return List.of();
            }
        };
    }

    private ReservationTimeRepository createReservationTimeRepository(Runnable runnable) {
        return new ReservationTimeRepository() {
            @Override
            public ReservationTime addReservationTime(ReservationTimeCommand reservationTimeCommand) {
                return null;
            }

            @Override
            public Optional<ReservationTime> getReservationTime(long id) {
                return Optional.empty();
            }

            @Override
            public List<ReservationTime> getAllReservationTime() {
                return List.of();
            }

            @Override
            public void deleteReservationTime(long id) {
                runnable.run();
            }

            @Override
            public List<ReservationTimeWithAvailable> getAvailableReservationTimeByDateAndTheme(ReservationTimeCondition reservationTimeCondition) {
                return List.of();
            }
        };
    }

    @Test
    @DisplayName("정상 삭제 테스트")
    void deleteReservationTimeTest() {
        ReservationTimeService reservationTimeService = new ReservationTimeService(createReservationTimeRepository(() -> {}), createReservationRepository(false));
        assertThatCode(() -> reservationTimeService.deleteReservationTime(1)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("외부에서 사용이 되었을 때 삭제 시 예외 테스트")
    void deleteFailedWhenInUseTest() {
        ReservationTimeService reservationTimeService = new ReservationTimeService(createReservationTimeRepository(() -> {}), createReservationRepository(true));

        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(1))
                .isExactlyInstanceOf(DataReferencedException.class)
                .hasMessage(ErrorMessage.CANNOT_DELETE_RESERVATION_TIME_IN_USE.getMessage());
    }

    @Test
    @DisplayName("조회 시점엔 없었으나 삭제 시점에 제약조건 위반이 발생한 경우 예외 테스트")
    void deleteFailedByIntegrityTest() {
        ReservationTimeService reservationTimeService = new ReservationTimeService(createReservationTimeRepository(() -> {
            throw new DataIntegrityViolationException("정합성 오류");
        }), createReservationRepository(false));

        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(1))
                .isExactlyInstanceOf(DataReferencedException.class)
                .hasMessage(ErrorMessage.INTEGRITY_VIOLATION_ON_DELETE.getMessage());
    }

}
