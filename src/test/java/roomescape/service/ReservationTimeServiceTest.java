package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservationTime.ReservationTime;
import roomescape.domain.reservationTime.ReservationTimeCondition;
import roomescape.domain.reservationTime.ReservationTimeWithAvailable;
import roomescape.dto.reservationTime.AddReservationTimeRequest;
import roomescape.exception.DataReferencedException;
import roomescape.exception.DuplicatedResourceException;
import roomescape.exception.ErrorCode;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationTime.ReservationTimeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.exception.ErrorCode.DUPLICATED_RESERVATION_TIME;

public class ReservationTimeServiceTest {
    private ReservationRepository createReservationRepository(boolean isExistTime) {
        return new ReservationRepository() {

            @Override
            public Reservation addReservation(Reservation reservation) {
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
            public List<Reservation> getAllReservation() {
                return List.of();
            }

            @Override
            public List<Reservation> getAllReservationByName(String name) {
                return List.of();
            }
        };
    }

    private ReservationTimeRepository createReservationTimeRepository(Runnable runnable, boolean isExist) {
        return new ReservationTimeRepository() {
            @Override
            public ReservationTime addReservationTime(ReservationTime reservationTime) {
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

            @Override
            public boolean existsByStartAt(LocalTime localTime) {
                return isExist;
            }
        };
    }

    @Test
    @DisplayName("동일한 시간이 존재하는 경우 예약 시간 생성 시 예외 테스트")
    void addReservationTimeFailedWhenDuplicatedTest() {
        ReservationTimeService reservationTimeService = new ReservationTimeService(
                createReservationTimeRepository(() -> {}, true),
                createReservationRepository(false)
        );

        assertThatThrownBy(() -> reservationTimeService.addReservationTime(
                new AddReservationTimeRequest(LocalTime.parse("10:00"))
        ))
                .isExactlyInstanceOf(DuplicatedResourceException.class)
                .hasMessage(DUPLICATED_RESERVATION_TIME.getMessage());
    }

    @Test
    @DisplayName("정상 삭제 테스트")
    void deleteReservationTimeTest() {
        ReservationTimeService reservationTimeService = new ReservationTimeService(createReservationTimeRepository(() -> {}, false), createReservationRepository(false));
        assertThatCode(() -> reservationTimeService.deleteReservationTime(1)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("외부에서 사용이 되었을 때 삭제 시 예외 테스트")
    void deleteFailedWhenInUseTest() {
        ReservationTimeService reservationTimeService = new ReservationTimeService(createReservationTimeRepository(() -> {}, false), createReservationRepository(true));

        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(1))
                .isExactlyInstanceOf(DataReferencedException.class)
                .hasMessage(ErrorCode.CANNOT_DELETE_RESERVATION_TIME_IN_USE.getMessage());
    }

    @Test
    @DisplayName("조회 시점엔 없었으나 삭제 시점에 제약조건 위반이 발생한 경우 예외 테스트")
    void deleteFailedByIntegrityTest() {
        ReservationTimeService reservationTimeService = new ReservationTimeService(createReservationTimeRepository(() -> {
            throw new DataIntegrityViolationException("정합성 오류");
        }, false), createReservationRepository(false));

        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(1))
                .isExactlyInstanceOf(DataReferencedException.class)
                .hasMessage(ErrorCode.INTEGRITY_VIOLATION_ON_DELETE.getMessage());
    }

}
