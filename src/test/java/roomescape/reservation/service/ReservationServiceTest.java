package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import roomescape.common.util.DateTime;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.domain.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeRepository;

class ReservationServiceTest {

    private DateTime dateTime = new DateTime() {
        @Override
        public LocalDateTime now() {
            return LocalDateTime.of(2025, 10, 5, 10, 0);
        }

        @Override
        public LocalDate nowDate() {
            return LocalDate.of(2025,10,5);
        }
    };

    private List<Reservation> reservations = new ArrayList<>();
    private List<Theme> themes = new ArrayList<>();
    private List<ReservationTime> reservationTimes = new ArrayList<>();
    private ThemeRepository themeRepository = new FakeThemeRepository(themes,reservations);
    private ReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository(reservationTimes);
    private ReservationRepository reservationRepository = new FakeReservationRepository(reservations);
    private ReservationService reservationService = new ReservationService(dateTime, reservationRepository, reservationTimeRepository, themeRepository);

    @BeforeEach
    void beforeEach() {
        Theme theme = Theme.createWithId(1L, "테스트1", "설명", "localhost:8080");
        themeRepository.save(theme);
        ReservationTime reservationTime1 = ReservationTime.createWithoutId(LocalTime.of(10, 0));
        ReservationTime reservationTime2 = ReservationTime.createWithoutId(LocalTime.of(9, 0));
        reservationTimeRepository.save(reservationTime1);
        reservationTimeRepository.save(reservationTime2);
        reservationRepository.save(Reservation.createWithoutId(LocalDateTime.of(1999,11,2,20,10),"홍길동", LocalDate.of(2024, 10, 6), reservationTime1, theme));
    }

    @DisplayName("지나간 날짜와 시간에 대한 예약을 생성할 수 없다.")
    @ParameterizedTest
    @MethodSource
    void cant_not_reserve_before_now(final LocalDate date, final Long timeId) {
        assertThatThrownBy(
                        () -> reservationService.createReservation(new ReservationRequest("홍길동", date, timeId, 1L)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> cant_not_reserve_before_now() {
        return Stream.of(
                Arguments.of(LocalDate.of(2024, 10, 5), 1L),
                Arguments.of(LocalDate.of(2025, 9, 5), 1L),
                Arguments.of(LocalDate.of(2025, 10, 4), 1L),
                Arguments.of(LocalDate.of(2025, 10, 5), 2L)
        );
    }

    @DisplayName("중복 예약이 불가하다.")
    @Test
    void cant_not_reserve_duplicate() {
        assertThatThrownBy(() -> reservationService.createReservation(
                        new ReservationRequest("홍길동", LocalDate.of(2024, 10, 6), 1L, 1L)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
