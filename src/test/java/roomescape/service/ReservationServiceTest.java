package roomescape.service;

import static org.assertj.core.api.Assertions.*;
import static roomescape.exception.dto.ErrorCode.*;
import static roomescape.exception.dto.ErrorCode.NOT_FOUND_THEME;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.theme.ThemeWithCount;
import roomescape.domain.theme.Theme;
import roomescape.domain.reservationTime.ReservationTime;
import roomescape.domain.reservationTime.ReservationTimeCondition;
import roomescape.domain.reservationTime.ReservationTimeWithAvailable;
import roomescape.dto.reservation.AddReservationRequest;
import roomescape.dto.theme.PopularConditionRequest;
import roomescape.exception.exception.DuplicatedResourceException;
import roomescape.exception.exception.InvalidRequestException;
import roomescape.exception.exception.NotFoundResourceException;
import roomescape.repository.theme.ThemeRepository;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationTime.ReservationTimeRepository;

public class ReservationServiceTest {
    private ReservationRepository createReservationRepository(boolean isExist, Reservation reservation) {
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

            @Override
            public Optional<Reservation> getReservationById(long id) {
                return Optional.ofNullable(reservation);
            }
        };
    }

    private ReservationTimeRepository createReservationTimeRepository(ReservationTime reservationTime, boolean isExist) {
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

            @Override
            public boolean existsByStartAt(LocalTime localTime) {
                return isExist;
            }
        };
    }

    private ThemeRepository createThemeRepository(Theme theme, boolean isExistTheme) {
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

            @Override
            public boolean existsByName(String name) {
                return isExistTheme;
            }
        };
    }

    @Test
    @DisplayName("예약 생성 시 유효한 시간 ID, 테마 ID인 경우 정상 작동 테스트")
    void addReservationTest() {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.parse("10:00"));
        Theme theme = new Theme(1L, "name", "description", "image");

        ReservationService reservationService = new ReservationService(createReservationRepository(false, null), createReservationTimeRepository(reservationTime, false), createThemeRepository(
                theme, false));

        LocalDate futureDate = LocalDate.now().plusDays(1);

        AddReservationRequest addReservationRequest = new AddReservationRequest("브라운", futureDate, 1L, 1L);

        Reservation reservation = reservationService.addReservation(addReservationRequest);

        assertThat(reservation)
                .usingRecursiveComparison()
                .isEqualTo(new Reservation(
                        1L,
                        "브라운",
                        futureDate,
                        reservationTime,
                        theme
                ));
    }

    @Test
    @DisplayName("예약 생성 시 지나간 날짜인 경우 예외 테스트")
    void addReservationFailByPastDateTest() {

        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.parse("10:00"));
        Theme theme = new Theme(1L, "name", "description", "image");

        ReservationService reservationService = new ReservationService(
                createReservationRepository(false, null),
                createReservationTimeRepository(reservationTime, false),
                createThemeRepository(theme, false)
        );

        LocalDate pastDate = LocalDate.now().minusDays(1);

        AddReservationRequest addReservationRequest =
                new AddReservationRequest("브라운", pastDate, 1L, 1L);

        assertThatThrownBy(() -> reservationService.addReservation(addReservationRequest))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage(INVALID_RESERVATION_DATE.getMessage());
    }

    @Test
    @DisplayName("오늘 날짜에서 지난 시간 예약 시 예외 발생 테스트")
    void addReservationFailByPastTimeTest() {

        LocalTime fixedNow = LocalTime.of(12, 0);

        ReservationTime reservationTime =
                new ReservationTime(1L, fixedNow.minusHours(1)); // 11:00

        Theme theme = new Theme(1L, "name", "description", "image");

        ReservationService reservationService = new ReservationService(
                createReservationRepository(false, null),
                createReservationTimeRepository(reservationTime, false),
                createThemeRepository(theme, false)
        );

        AddReservationRequest request = new AddReservationRequest(
                "브라운",
                LocalDate.now(),
                1L,
                1L
        );

        assertThatThrownBy(() -> reservationService.addReservation(request))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage(INVALID_RESERVATION_TIME.getMessage());
    }

    @Test
    @DisplayName("예약 생성 시 존재하지 않는 시간ID인 경우 예외 테스트")
    void addReservationFailByInvalidTimeIdTest() {
        ReservationService reservationService = new ReservationService(createReservationRepository(false, null), createReservationTimeRepository(null, false), createThemeRepository(new Theme(1L, "테마1", "설명", "url"), false));
        LocalDate futureDate = LocalDate.now().plusDays(1);

        AddReservationRequest addReservationRequest = new AddReservationRequest("브라운", futureDate, 1L, 1L);

        assertThatThrownBy(() -> reservationService.addReservation(addReservationRequest))
                .isExactlyInstanceOf(NotFoundResourceException.class)
                .hasMessage(NOT_FOUND_RESERVATION_TIME.getMessage());
    }

    @Test
    @DisplayName("예약 생성 시 존재하지 않는 테마 ID인 경우 예외 테스트")
    void addReservationFailByInvalidThemeIdTest() {
        ReservationService reservationService = new ReservationService(createReservationRepository(false, null), createReservationTimeRepository(new ReservationTime(1L, LocalTime.parse("10:00")), false), createThemeRepository(null, false));
        LocalDate futureDate = LocalDate.now().plusDays(1);

        AddReservationRequest addReservationRequest = new AddReservationRequest("브라운", futureDate, 1L, 1L);

        assertThatThrownBy(() -> reservationService.addReservation(addReservationRequest))
                .isExactlyInstanceOf(NotFoundResourceException.class)
                .hasMessage(NOT_FOUND_THEME.getMessage());
    }

    @Test
    @DisplayName("같은 시간, 날짜, themeId가 존재하는 경우, 예약 생성 시 예외 테스트")
    void test() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.parse("10:00"));
        Theme theme = new Theme(1L, "name", "description", "image");

        ReservationService reservationService = new ReservationService(
                createReservationRepository(true, null),
                createReservationTimeRepository(reservationTime, false),
                createThemeRepository(theme, false)
        );

        assertThatThrownBy(() -> reservationService.addReservation(new AddReservationRequest("test", futureDate, 1L, 1L)))
                .isExactlyInstanceOf(DuplicatedResourceException.class)
                .hasMessage(DUPLICATED_RESERVATION.getMessage()
        );
    }

    @Test
    @DisplayName("이름으로 삭제할 경우, 존재하지 않는 예약 id 입력 시 예외 테스트")
    void deleteReservationByNameFailByNotFoundTest() {
        ReservationService reservationService = new ReservationService(
                createReservationRepository(false, null),
                createReservationTimeRepository(null, false),
                createThemeRepository(null, false)
        );

        assertThatThrownBy(() -> reservationService.deleteReservationByName(1L, "브라운"))
                .isExactlyInstanceOf(NotFoundResourceException.class)
                .hasMessage(NOT_FOUND_RESERVATION.getMessage());
    }

    @Test
    @DisplayName("이름으로 삭제할 경우, 이름 불일치 시 삭제 예외 테스트")
    void deleteReservationByNameFailByNameMismatchTest() {
        Reservation reservation = new Reservation(1L, "브라운", LocalDate.now().plusDays(1),
                new ReservationTime(1L, LocalTime.parse("10:00")),
                new Theme(1L, "name", "description", "image"));

        ReservationService reservationService = new ReservationService(
                createReservationRepository(false, reservation),
                createReservationTimeRepository(null, false),
                createThemeRepository(null, false)
        );

        assertThatThrownBy(() -> reservationService.deleteReservationByName(1L, "다른이름"))
                .isExactlyInstanceOf(InvalidRequestException.class)
                .hasMessage(UNAUTHORIZED_RESERVATION_ACCESS.getMessage());
    }

    @Test
    @DisplayName("정상적으로 예약 삭제 테스트")
    void deleteReservationByNameTest() {
        Reservation reservation = new Reservation(1L, "브라운", LocalDate.now().plusDays(1),
                new ReservationTime(1L, LocalTime.parse("10:00")),
                new Theme(1L, "name", "description", "image"));

        ReservationService reservationService = new ReservationService(
                createReservationRepository(false, reservation),
                createReservationTimeRepository(null, false),
                createThemeRepository(null, false)
        );

        assertThatCode(() -> reservationService.deleteReservationByName(1L, "브라운"))
                .doesNotThrowAnyException();
    }
}
