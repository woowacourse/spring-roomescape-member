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
import roomescape.domain.theme.PopularThemeCondition;
import roomescape.domain.theme.ThemeWithCount;
import roomescape.domain.theme.Theme;
import roomescape.domain.reservationTime.ReservationTime;
import roomescape.domain.reservationTime.ReservationTimeCondition;
import roomescape.domain.reservationTime.ReservationTimeWithAvailable;
import roomescape.dto.reservation.AddReservationRequest;
import roomescape.dto.theme.PopularConditionRequest;
import roomescape.exception.DuplicatedReservationRequestException;
import roomescape.exception.ErrorMessage;
import roomescape.exception.NotFoundResourceException;
import roomescape.repository.theme.ThemeRepository;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationTime.ReservationTimeRepository;

public class RoomReservationServiceTest {
    private ReservationRepository createReservationRepository(boolean isExist) {
        return new ReservationRepository() {
            @Override
            public List<Reservation> getAllReservation() {
                return List.of();
            }

            @Override
            public Reservation addReservation(Reservation reservation) {
                return new Reservation(1L, reservation.name(), reservation.date(), reservation.time(), reservation.theme());
            }

            @Override
            public void deleteReservation(long id) {

            }

            @Override
            public List<Reservation> getAllReservationByName(String name) {
                return List.of();
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
            public ReservationTime addReservationTime(ReservationTime reservationTime1) {
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

    private ThemeRepository createThemeRepository(Theme theme) {
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
                if(theme == null) {
                    return Optional.empty();
                }
                return Optional.of(theme);
            }

            @Override
            public void deleteTheme(long id) {

            }

            @Override
            public List<ThemeWithCount> getPopularTheme(PopularConditionRequest popularConditionRequest) {
                return List.of();
            }
        };
    }

    @Test
    @DisplayName("예약 생성 시 유효한 시간 ID, 테마 ID인 경우 정상 작동 테스트")
    void addReservationTest() {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.parse("10:00"));
        Theme theme = new Theme(1L, "name", "description", "image");

        RoomReservationService reservationService = new RoomReservationService(createReservationRepository(false), createReservationTimeRepository(reservationTime), createThemeRepository(
                theme));

        AddReservationRequest addReservationRequest = new AddReservationRequest("브라운", LocalDate.parse("2023-08-05"), 1L, 1L);

        Reservation reservation = reservationService.addReservation(addReservationRequest);

        assertThat(reservation)
                .usingRecursiveComparison()
                .isEqualTo(new Reservation(
                        1L,
                        "브라운",
                        LocalDate.parse("2023-08-05"),
                        reservationTime,
                        theme
                ));
    }

    @Test
    @DisplayName("예약 생성 시 존재하지 않는 시간ID인 경우 예외 테스트")
    void addReservationFailByInvalidTimeIdTest() {
        RoomReservationService reservationService = new RoomReservationService(createReservationRepository(false), createReservationTimeRepository(null), createThemeRepository(new Theme(1L, "테마1", "설명", "url")));
        AddReservationRequest addReservationRequest = new AddReservationRequest("브라운", LocalDate.parse("2023-08-05"), 1L, 1L);

        assertThatThrownBy(() -> reservationService.addReservation(addReservationRequest))
                .isExactlyInstanceOf(NotFoundResourceException.class)
                .hasMessage(ErrorMessage.INVALID_RESERVATION_TIME_ID.getMessage());
    }

    @Test
    @DisplayName("예약 생성 시 존재하지 않는 테마 ID인 경우 예외 테스트")
    void addReservationFailByInvalidThemeIdTest() {
        RoomReservationService reservationService = new RoomReservationService(createReservationRepository(false), createReservationTimeRepository(new ReservationTime(1L, LocalTime.parse("10:00"))), createThemeRepository(null));
        AddReservationRequest addReservationRequest = new AddReservationRequest("브라운", LocalDate.parse("2023-08-05"), 1L, 1L);

        assertThatThrownBy(() -> reservationService.addReservation(addReservationRequest))
                .isExactlyInstanceOf(NotFoundResourceException.class)
                .hasMessage(ErrorMessage.INVALID_THEME_ID.getMessage());
    }

    @Test
    @DisplayName("같은 시간, 날짜, themeId가 존재하는 경우, 예약 생성 시 예외 테스트")
    void test() {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.parse("10:00"));
        Theme theme = new Theme(1L, "name", "description", "image");

        RoomReservationService reservationService = new RoomReservationService(
                createReservationRepository(true),
                createReservationTimeRepository(reservationTime),
                createThemeRepository(theme)
        );

        assertThatThrownBy(() -> reservationService.addReservation(new AddReservationRequest("test", LocalDate.parse("2023-08-05"), 1L, 1L)))
                .isExactlyInstanceOf(DuplicatedReservationRequestException.class)
                .hasMessage(ErrorMessage.DUPLICATED_RESERVATION_REQUEST.getMessage()
        );
    }
}
