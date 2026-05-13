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
import roomescape.exception.NotFoundResourceException;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationTime.ReservationTimeRepository;

public class ReservationTimeServiceTest {
    private ReservationRepository createReservationRepository(boolean isUsedInReservation) {
        return new ReservationRepository() {
            @Override public Optional<Reservation> getReservation(long id) { return Optional.empty(); }
            @Override public ReservationInfo addReservation(ReservationCommand command, ReservationTime rt, Theme t) { return null; }
            @Override public void deleteReservation(long id) {}
            @Override public int updateAll(long id, ReservationCommand cmd) { return 0; }
            @Override public boolean existsByTimeId(long timeId) { return isUsedInReservation; }
            @Override public boolean existsByThemeId(long themeId) { return false; }
            @Override public boolean existsByTimeIdAndThemeIdAndDate(long tid, long thid, LocalDate d) { return false; }
            @Override public List<ReservationInfo> getAllReservation(String name) { return List.of(); }
        };
    }

    private ReservationTimeRepository createReservationTimeRepository(boolean isExist, Runnable runnable) {
        return new ReservationTimeRepository() {
            @Override public ReservationTime addReservationTime(ReservationTimeCommand cmd) { return null; }
            @Override public Optional<ReservationTime> getReservationTime(long id) { return Optional.empty(); }
            @Override public List<ReservationTime> getAllReservationTime() { return List.of(); }
            @Override public void deleteReservationTime(long id) { runnable.run(); }
            @Override public List<ReservationTimeWithAvailable> getAvailableReservationTimeByDateAndTheme(ReservationTimeCondition cond) { return List.of(); }
            @Override public boolean isExistsById(long id) { return isExist; }
        };
    }

    @Test
    @DisplayName("정상적으로 예약 시간을 삭제한다")
    void deleteReservationTimeTest() {
        ReservationTimeService reservationTimeService = new ReservationTimeService(
                createReservationTimeRepository(true, () -> {}), // 시간 존재함
                createReservationRepository(false)              // 예약에 사용 안 됨
        );

        assertThatCode(() -> reservationTimeService.deleteReservationTime(1L))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("존재하지 않는 시간 ID 삭제 시 NotFoundResourceException 발생")
    void deleteFailedByNotFoundTest() {
        ReservationTimeService reservationTimeService = new ReservationTimeService(
                createReservationTimeRepository(false, () -> {}), // 시간 존재 안 함
                createReservationRepository(false)
        );

        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(1L))
                .isExactlyInstanceOf(NotFoundResourceException.class)
                .hasMessage("존재하지 않은 시간 id입니다.");
    }

    @Test
    @DisplayName("이미 예약에 사용 중인 시간인 경우 ConflictException 발생")
    void deleteFailedWhenInUseTest() {
        ReservationTimeService reservationTimeService = new ReservationTimeService(
                createReservationTimeRepository(true, () -> {}), // 시간 존재함
                createReservationRepository(true)               // 예약에 사용 중
        );

        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(1L))
                .isExactlyInstanceOf(ConflictException.class)
                .hasMessage("해당 시간을 참조하는 예약 데이터가 존재하기 때문에 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("삭제 도중 데이터베이스 무결성 제약 조건 위반 시 ConflictException 발생")
    void deleteFailedByIntegrityTest() {
        ReservationTimeService reservationTimeService = new ReservationTimeService(
                createReservationTimeRepository(true, () -> {
                    throw new DataIntegrityViolationException("DB Error");
                }),
                createReservationRepository(false)
        );

        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(1L))
                .isExactlyInstanceOf(ConflictException.class)
                .hasMessage("데이터 무결성 위반으로 삭제에 실패했습니다.");
    }
}