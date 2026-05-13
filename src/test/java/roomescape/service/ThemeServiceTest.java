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
import roomescape.domain.theme.PopularThemeCondition;
import roomescape.domain.theme.ReservationThemeCommand;
import roomescape.domain.theme.ReservationThemeWithCount;
import roomescape.domain.theme.Theme;
import roomescape.exception.ConflictException;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.theme.ThemeRepository;

public class ThemeServiceTest {
    private ReservationRepository createReservationRepository(boolean isExistTheme) {
        return new ReservationRepository() {
            @Override public Optional<Reservation> getReservation(long id) { return Optional.empty(); }
            @Override public ReservationInfo addReservation(ReservationCommand command, ReservationTime reservationTime, Theme theme) { return null; }
            @Override public void deleteReservation(long id) {}
            @Override public int updateAll(long id, ReservationCommand command) { return 0; }
            @Override public boolean existsByTimeId(long timeId) { return false; }
            @Override public boolean existsByThemeId(long themeId) { return isExistTheme; }
            @Override public boolean existsByTimeIdAndThemeIdAndDate(long timeId, long themeId, LocalDate date) { return false; }
            @Override public List<ReservationInfo> getAllReservation(String name) { return List.of(); }
        };
    }

    private ThemeRepository createThemeRepository(Runnable runnable) {
        return new ThemeRepository() {
            @Override public Theme addTheme(ReservationThemeCommand command) { return null; }
            @Override public List<Theme> getAllTheme() { return List.of(); }
            @Override public Optional<Theme> getTheme(long id) { return Optional.empty(); }
            @Override public void deleteTheme(long id) { runnable.run(); }
            @Override public List<ReservationThemeWithCount> getPopularTheme(PopularThemeCondition condition) { return List.of(); }
        };
    }

    @Test
    @DisplayName("정상 삭제 테스트")
    void deleteThemeTest() {
        ThemeService themeService = new ThemeService(createThemeRepository(() -> {}), createReservationRepository(false));

        assertThatCode(() -> themeService.deleteTheme(1L)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("테마를 참조하는 예약이 존재하면 ConflictException 예외 발생")
    void deleteFailedWhenInUseTest() {
        ThemeService themeService = new ThemeService(createThemeRepository(() -> {}), createReservationRepository(true));

        assertThatThrownBy(() -> themeService.deleteTheme(1L))
                .isExactlyInstanceOf(ConflictException.class)
                .hasMessage("해당 테마를 참조하는 예약 데이터가 존재하기 때문에 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("삭제 시점에 DB 제약조건 위반이 발생하면 ConflictException 예외 발생")
    void deleteFailedByIntegrityTest() {
        ThemeService themeService = new ThemeService(
                createThemeRepository(() -> {
                    throw new DataIntegrityViolationException("데이터 무결성 위반");
                }),
                createReservationRepository(false)
        );

        assertThatThrownBy(() -> themeService.deleteTheme(1L))
                .isExactlyInstanceOf(ConflictException.class)
                .hasMessage("데이터 무결성 위반으로 삭제에 실패했습니다.");
    }
}