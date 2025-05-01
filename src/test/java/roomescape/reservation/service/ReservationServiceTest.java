package roomescape.reservation.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    private ReservationService reservationService;

    @BeforeEach
    void beforeEach() {
        DateTime dateTime = new DateTime() {
            @Override
            public LocalDateTime now() {
                return LocalDateTime.of(2025, 10, 5, 10, 0);
            }
        };
        ThemeRepository themeRepository = new FakeThemeRepository(null);
        Theme theme = Theme.createWithId(1L, "테스트1", "설명", "localhost:8080");
        themeRepository.save(theme);

        ReservationTime reservationTime1 = ReservationTime.createWithoutId(LocalTime.of(10, 0));
        ReservationTime reservationTime2 = ReservationTime.createWithoutId(LocalTime.of(9, 0));

        ReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository();
        reservationTimeRepository.save(reservationTime1);
        reservationTimeRepository.save(reservationTime2);

        ReservationRepository reservationRepository = new FakeReservationRepository();
        reservationRepository.save(Reservation.createWithoutId("홍길동", LocalDate.of(2024, 10, 6), reservationTime1, theme));

        reservationService = new ReservationService(
                dateTime,
                reservationRepository,
                reservationTimeRepository,
                themeRepository
        );
    }

    @DisplayName("지나간 날짜와 시간에 대한 예약을 생성할 수 없다.")
    @ParameterizedTest
    @MethodSource
    void cant_not_reserve_before_now(final LocalDate date, final Long timeId) {
        Assertions.assertThatThrownBy(
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
        Assertions.assertThatThrownBy(() -> reservationService.createReservation(
                        new ReservationRequest("홍길동", LocalDate.of(2024, 10, 6), 1L, 1L)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
