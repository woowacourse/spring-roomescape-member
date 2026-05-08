package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationCommand;
import roomescape.domain.reservationTheme.PopularThemeCondition;
import roomescape.domain.reservationTheme.ReservationThemeWithCount;
import roomescape.domain.reservationTime.ReservationTime;
import roomescape.domain.reservationTime.ReservationTimeCommand;
import roomescape.domain.reservationTime.ReservationTimeCondition;
import roomescape.domain.reservationTime.ReservationTimeWithAvailable;
import roomescape.domain.reservationTheme.ReservationTheme;
import roomescape.domain.reservationTheme.ReservationThemeCommand;
import roomescape.exception.DuplicatedReservationRequestException;
import roomescape.exception.ErrorMessage;
import roomescape.exception.NotFoundResourceException;
import roomescape.repository.reservationTheme.ReservationThemeRepository;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationTime.ReservationTimeRepository;

public class RoomReservationServiceTest {
    private ReservationRepository createReservationRepository(boolean isExist) {
        return new ReservationRepository() {
            @Override
            public List<Reservation> getAllReservation(String name) {
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

            @Override
            public boolean existsByTimeIdAndThemeIdAndDate(long timeId, long themeId, LocalDate date) {
                return isExist;
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
            public List<ReservationTimeWithAvailable> getAvailableReservationTimeByDateAndTheme(
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

            @Override
            public List<ReservationThemeWithCount> getPopularTheme(PopularThemeCondition popularThemeCondition) {
                return List.of();
            }
        };
    }

    @Test
    @DisplayName("예약 생성 시 유효한 시간 ID, 테마 ID인 경우 정상 작동 테스트")
    void addReservationTest() {
        ReservationTime reservationTime = new ReservationTime(1, LocalTime.parse("10:00"));
        ReservationTheme reservationTheme = new ReservationTheme(1, "name", "description", "image");

        RoomReservationService reservationService = new RoomReservationService(createReservationRepository(false), createReservationTimeRepository(reservationTime), createThemeRepository(
                reservationTheme));
        ReservationCommand reservationCommand = new ReservationCommand("브라운", "2023-08-05", 1, 1);

        Reservation reservation = reservationService.addReservation(reservationCommand);

        assertThat(reservation).isEqualTo(new Reservation(1, "브라운", "2023-08-05", reservationTime, reservationTheme));
    }

    @Test
    @DisplayName("예약 생성 시 존재하지 않는 시간ID인 경우 예외 테스트")
    void addReservationFailByInvalidTimeIdTest() {
        RoomReservationService reservationService = new RoomReservationService(createReservationRepository(false), createReservationTimeRepository(null), createThemeRepository(new ReservationTheme(1, "테마1", "설명", "url")));
        ReservationCommand reservationCommand = new ReservationCommand("브라운", "2023-08-05", 1, 1);

        assertThatThrownBy(() -> reservationService.addReservation(reservationCommand))
                .isExactlyInstanceOf(NotFoundResourceException.class)
                .hasMessage(ErrorMessage.INVALID_RESERVATION_TIME_ID.getMessage());
    }

    @Test
    @DisplayName("예약 생성 시 존재하지 않는 테마 ID인 경우 예외 테스트")
    void addReservationFailByInvalidThemeIdTest() {
        RoomReservationService reservationService = new RoomReservationService(createReservationRepository(false), createReservationTimeRepository(new ReservationTime(1, LocalTime.parse("10:00"))), createThemeRepository(null));
        ReservationCommand reservationCommand = new ReservationCommand("브라운", "2023-08-05", 1, 1);

        assertThatThrownBy(() -> reservationService.addReservation(reservationCommand))
                .isExactlyInstanceOf(NotFoundResourceException.class)
                .hasMessage(ErrorMessage.INVALID_THEME_ID.getMessage());
    }

    @Test
    @DisplayName("같은 시간, 날짜, themeId가 존재하는 경우, 예약 생성 시 예외 테스트")
    void test() {
        ReservationTime reservationTime = new ReservationTime(1, LocalTime.parse("10:00"));
        ReservationTheme reservationTheme = new ReservationTheme(1, "name", "description", "image");

        RoomReservationService reservationService = new RoomReservationService(
                createReservationRepository(true),
                createReservationTimeRepository(reservationTime),
                createThemeRepository(reservationTheme)
        );

        assertThatThrownBy(() -> reservationService.addReservation(new ReservationCommand("test", "2023-08-05", 1, 1)))
                .isExactlyInstanceOf(DuplicatedReservationRequestException.class)
                .hasMessage(ErrorMessage.DUPLICATED_RESERVATION_REQUEST.getMessage()
        );
    }
}
