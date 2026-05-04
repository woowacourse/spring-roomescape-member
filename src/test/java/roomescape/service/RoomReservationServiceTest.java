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
import roomescape.domain.ReservationTime.ReservationTimeCondition;
import roomescape.domain.ReservationTime.ReservationTimeWithAvailable;
import roomescape.domain.ReservationTheme.ReservationTheme;
import roomescape.domain.ReservationTheme.ReservationThemeCommand;
import roomescape.exception.ErrorMessage;
import roomescape.exception.NotFoundResourceException;
import roomescape.repository.ReservationTheme.ReservationThemeRepository;
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
            public Reservation addReservation(ReservationCommand reservationCommand, ReservationTime reservationTime, ReservationTheme theme) {
                return new Reservation(1, reservationCommand.name(), reservationCommand.date(), reservationTime, theme);
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

            @Override
            public List<ReservationTimeWithAvailable> getReservationTimeByDateAndTheme(
                    ReservationTimeCondition reservationTimeCondition) {
                return List.of();
            }
        };
    }

    private ReservationThemeRepository createThemeRepository(ReservationTheme reservationTheme) {
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
                if(reservationTheme == null) {
                    return Optional.empty();
                }
                return Optional.of(reservationTheme);
            }

            @Override
            public void deleteTheme(long id) {

            }
        };
    }

    @Test
    @DisplayName("예약 생성 시 유효한 시간 ID, 테마 ID인 경우 정상 작동 테스트")
    void addReservationTest() {
        ReservationTime reservationTime = new ReservationTime(1, "10:00");
        ReservationTheme reservationTheme = new ReservationTheme(1, "name", "description", "image");

        RoomReservationService reservationService = new RoomReservationService(createReservationRepository(), createReservationTimeRepository(reservationTime), createThemeRepository(
                reservationTheme));
        ReservationCommand reservationCommand = new ReservationCommand("브라운", "2023-08-05", 1, 1);

        Reservation reservation = reservationService.addReservation(reservationCommand);

        assertThat(reservation).isEqualTo(new Reservation(1, "브라운", "2023-08-05", reservationTime, reservationTheme));
    }

    @Test
    @DisplayName("예약 생성 시 존재하지 않는 시간ID인 경우 예외 테스트")
    void addReservationFailByInvalidTimeIdTest() {
        RoomReservationService reservationService = new RoomReservationService(createReservationRepository(), createReservationTimeRepository(null), createThemeRepository(new ReservationTheme(1, "테마1", "설명", "url")));
        ReservationCommand reservationCommand = new ReservationCommand("브라운", "2023-08-05", 1, 1);

        assertThatThrownBy(() -> reservationService.addReservation(reservationCommand))
                .isExactlyInstanceOf(NotFoundResourceException.class)
                .hasMessage(ErrorMessage.INVALID_RESERVATION_TIME_ID.getMessage());
    }

    @Test
    @DisplayName("예약 생성 시 존재하지 않는 테마 ID인 경우 예외 테스트")
    void addReservationFailByInvalidThemeIdTest() {
        RoomReservationService reservationService = new RoomReservationService(createReservationRepository(), createReservationTimeRepository(new ReservationTime(1, "10:00")), createThemeRepository(null));
        ReservationCommand reservationCommand = new ReservationCommand("브라운", "2023-08-05", 1, 1);

        assertThatThrownBy(() -> reservationService.addReservation(reservationCommand))
                .isExactlyInstanceOf(NotFoundResourceException.class)
                .hasMessage(ErrorMessage.INVALID_THEME_ID.getMessage());
    }
}
