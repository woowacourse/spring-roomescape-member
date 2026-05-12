package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.theme.PopularThemeCondition;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeWithCount;
import roomescape.dto.theme.PopularConditionRequest;
import roomescape.exception.DataReferencedException;
import roomescape.exception.ErrorMessage;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.theme.ThemeRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ThemeServiceTest {
    private ReservationRepository createReservationRepository(boolean isExistTheme) {
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
                return false;
            }

            @Override
            public boolean existsByThemeId(long themeId) {
                return isExistTheme;
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

    private ThemeRepository createThemeRepository(Runnable runnable) {
        return new ThemeRepository() {
            @Override
            public Theme addTheme(Theme theme) {
                return new Theme(1L, theme.name(), theme.description(), theme.imageUrl());
            }

            @Override
            public List<Theme> getAllTheme() {
                return List.of();
            }

            @Override
            public Optional<Theme> getTheme(long id) {
                return Optional.empty();
            }

            @Override
            public void deleteTheme(long id) {
                runnable.run();
            }

            @Override
            public List<ThemeWithCount> getPopularTheme(PopularConditionRequest popularConditionRequest) {
                return List.of();
            }
        };
    }

    @Test
    @DisplayName("정상 삭제 테스트")
    void deleteThemeTest() {
        ThemeService themeService = new ThemeService(createThemeRepository(() -> {}), createReservationRepository(false));

        assertThatCode(() -> themeService.deleteTheme(1)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("외부 사용이 되었을 때 삭제 시 예외 테스트")
    void deleteFailedWhenInUseTest() {
        ThemeService themeService = new ThemeService(createThemeRepository(() -> {}), createReservationRepository(true));

        assertThatThrownBy(() -> themeService.deleteTheme(1))
                .isExactlyInstanceOf(DataReferencedException.class)
                .hasMessage(ErrorMessage.CANNOT_DELETE_THEME_IN_USE.getMessage());
    }

    @Test
    @DisplayName("조회 시점엔 없었으나 삭제 시점에 제약조건 위반이 발생한 경우 예외 테스트")
    void deleteFailedByIntegrityTest() {
        ThemeService themeService = new ThemeService(
                createThemeRepository(() -> {
                    throw new DataIntegrityViolationException("정합성 오류");
                }),
                createReservationRepository(false)
        );

        assertThatThrownBy(() -> themeService.deleteTheme(1))
                .isExactlyInstanceOf(DataReferencedException.class)
                .hasMessage(ErrorMessage.INTEGRITY_VIOLATION_ON_DELETE.getMessage());
    }
}
