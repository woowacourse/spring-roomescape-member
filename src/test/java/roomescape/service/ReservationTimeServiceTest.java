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
import roomescape.domain.reservation.ReservationInfo;
import roomescape.domain.reservationTime.ReservationTime;
import roomescape.domain.reservationTime.ReservationTimeCommand;
import roomescape.domain.reservationTime.ReservationTimeCondition;
import roomescape.domain.reservationTime.ReservationTimeWithAvailable;
import roomescape.domain.theme.Theme;
import roomescape.exception.ConflictException;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationTime.ReservationTimeRepository;

public class ReservationTimeServiceTest {
    private ReservationRepository createReservationRepository(boolean isExistTime) {
        return new ReservationRepository() {
            @Override public Optional<Reservation> getReservation(long id) { return Optional.empty(); }
            @Override public ReservationInfo addReservation(ReservationCommand command, ReservationTime reservationTime, Theme theme) { return null; }
            @Override public void deleteReservation(long id) {}
            @Override public int updateAll(long id, ReservationCommand reservationCommand) { return 0; }
            @Override public boolean existsByTimeId(long timeId) { return isExistTime; }
            @Override public boolean existsByThemeId(long themeId) { return false; }
            @Override public boolean existsByTimeIdAndThemeIdAndDate(long timeId, long themeId, LocalDate date) { return false; }
            @Override public List<ReservationInfo> getAllReservation(String name) { return List.of(); }
        };
    }

    private ReservationTimeRepository createReservationTimeRepository(Runnable runnable) {
        return new ReservationTimeRepository() {
            @Override public ReservationTime addReservationTime(ReservationTimeCommand reservationTimeCommand) { return null; }
            @Override public Optional<ReservationTime> getReservationTime(long id) { return Optional.empty(); }
            @Override public List<ReservationTime> getAllReservationTime() { return List.of(); }
            @Override public void deleteReservationTime(long id) { runnable.run(); }
            @Override public List<ReservationTimeWithAvailable> getAvailableReservationTimeByDateAndTheme(ReservationTimeCondition reservationTimeCondition) { return List.of(); }
        };
    }

    @Test
    @DisplayName("정상 삭제 테스트")
    void deleteReservationTimeTest() {
        ReservationTimeService reservationTimeService = new ReservationTimeService(
                createReservationTimeRepository(() -> {}),
                createReservationRepository(false)
        );

        assertThatCode(() -> reservationTimeService.deleteReservationTime(1L))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("이미 예약된 시간인 경우 ConflictException 발생")
    void deleteFailedWhenInUseTest() {
        ReservationTimeService reservationTimeService = new ReservationTimeService(
                createReservationTimeRepository(() -> {}),
                createReservationRepository(true)
        );

        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(1L))
                .isExactlyInstanceOf(ConflictException.class)
                .hasMessage("해당 시간을 참조하는 예약 데이터가 존재하기 때문에 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("데이터베이스 제약 조건 위반 시 ConflictException 발생")
    void deleteFailedByIntegrityTest() {
        ReservationTimeService reservationTimeService = new ReservationTimeService(
                createReservationTimeRepository(() -> {
                    throw new DataIntegrityViolationException("데이터 무결성 오류 발생");
                }),
                createReservationRepository(false)
        );

        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(1L))
                .isExactlyInstanceOf(ConflictException.class)
                .hasMessage("데이터 무결성 위반으로 삭제에 실패했습니다.");
    }
}