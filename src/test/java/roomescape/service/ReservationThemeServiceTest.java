package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationCommand;
import roomescape.domain.reservationTheme.PopularThemeCondition;
import roomescape.domain.reservationTheme.ReservationThemeWithCount;
import roomescape.domain.reservationTime.ReservationTime;
import roomescape.domain.reservationTheme.ReservationTheme;
import roomescape.domain.reservationTheme.ReservationThemeCommand;
import roomescape.exception.DataReferencedException;
import roomescape.exception.ErrorMessage;
import roomescape.repository.reservationTheme.ReservationThemeRepository;
import roomescape.repository.reservation.ReservationRepository;

public class ReservationThemeServiceTest {
    private ReservationRepository createReservationRepository(boolean isExistTheme) {
        return new ReservationRepository() {

            @Override
            public Reservation addReservation(ReservationCommand reservationCommand, ReservationTime reservationTime, ReservationTheme theme) {
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
            public boolean existsByTimeIdAndThemeIdAndDate(long timeId, long themeId, String date) {
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

    private ReservationThemeRepository createReservationThemeRepository(Runnable runnable) {
        return new ReservationThemeRepository() {
            @Override
            public ReservationTheme addTheme(ReservationThemeCommand reservationThemeCommand) {
                return new ReservationTheme(1, reservationThemeCommand.name(), reservationThemeCommand.description(), reservationThemeCommand.imageUrl());
            }

            @Override
            public List<ReservationTheme> getAllTheme() {
                return List.of();
            }

            @Override
            public Optional<ReservationTheme> getTheme(long id) {
                return Optional.empty();
            }

            @Override
            public void deleteTheme(long id) {
                runnable.run();
            }

            @Override
            public List<ReservationThemeWithCount> getPopularTheme(PopularThemeCondition popularThemeCondition) {
                return List.of();
            }
        };
    }

    @Test
    @DisplayName("정상 삭제 테스트")
    void deleteReservationThemeTest() {
        ReservationThemeService reservationThemeService = new ReservationThemeService(createReservationThemeRepository(() -> {}), createReservationRepository(false));

        assertThatCode(() -> reservationThemeService.deleteTheme(1)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("외부 사용이 되었을 때 삭제 시 예외 테스트")
    void deleteFailedWhenInUseTest() {
        ReservationThemeService reservationThemeService = new ReservationThemeService(createReservationThemeRepository(() -> {}), createReservationRepository(true));

        assertThatThrownBy(() -> reservationThemeService.deleteTheme(1))
                .isExactlyInstanceOf(DataReferencedException.class)
                .hasMessage(ErrorMessage.CANNOT_DELETE_RESERVATION_THEME_IN_USE.getMessage());
    }

    @Test
    @DisplayName("조회 시점엔 없었으나 삭제 시점에 제약조건 위반이 발생한 경우 예외 테스트")
    void deleteFailedByIntegrityTest() {
        ReservationThemeService reservationThemeService = new ReservationThemeService(
                createReservationThemeRepository(() -> {
                    throw new DataIntegrityViolationException("정합성 오류");
                }),
                createReservationRepository(false)
        );

        assertThatThrownBy(() -> reservationThemeService.deleteTheme(1))
                .isExactlyInstanceOf(DataReferencedException.class)
                .hasMessage(ErrorMessage.INTEGRITY_VIOLATION_ON_DELETE.getMessage());
    }
}
