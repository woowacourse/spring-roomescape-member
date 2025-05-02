package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import roomescape.global.error.exception.BadRequestException;
import roomescape.global.error.exception.ConflictException;
import roomescape.reservation.dto.request.ReservationTimeRequest;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.entity.ReservationTime;
import roomescape.reservation.repository.FakeReservationRepository;
import roomescape.reservation.repository.FakeReservationTimeRepository;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.ReservationTimeRepository;

class ReservationTimeServiceTest {

    private final ReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository();
    private final ReservationRepository reservationRepository = new FakeReservationRepository();
    private final ReservationTimeService service = new ReservationTimeService(reservationTimeRepository,
            reservationRepository);

    @DisplayName("예약 생성이 가능한 시간은 10:00 ~ 22:00 이다.")
    @ParameterizedTest
    @MethodSource
    void validOperatingTime(LocalTime startAt) {
        // given
        ReservationTimeRequest requestDto = new ReservationTimeRequest(startAt);

        // when
        assertThatCode(() -> {
            service.createTime(requestDto);
        }).doesNotThrowAnyException();
    }

    private static Stream<Arguments> validOperatingTime() {
        return Stream.of(
                Arguments.of(LocalTime.of(10, 0)),
                Arguments.of(LocalTime.of(21, 59))
        );
    }

    @DisplayName("운영 시간 이외에는 예약할 수 없다.")
    @ParameterizedTest
    @MethodSource
    void invalidOperatingTime(LocalTime startAt) {
        // given
        ReservationTimeRequest requestDto = new ReservationTimeRequest(startAt);
        // when & then
        assertThatThrownBy(() -> {
            service.createTime(requestDto);
        }).isInstanceOf(BadRequestException.class);
    }

    private static Stream<Arguments> invalidOperatingTime() {
        return Stream.of(
                Arguments.of(LocalTime.of(9, 59)),
                Arguments.of(LocalTime.of(22, 1))
        );
    }

    @DisplayName("기존에 존재하는 시간과 러닝 타임(2시간)이 겹치는 경우 생성할 수 없다.")
    @Test
    void duplicateByRunningTime() {
        // given
        LocalTime time = LocalTime.of(10, 0);
        LocalTime duplicatedTime = time.plusHours(1);
        reservationTimeRepository.save(new ReservationTime(1L, time));

        ReservationTimeRequest requestDto = new ReservationTimeRequest(duplicatedTime);

        // when & then
        assertThatThrownBy(() -> {
            service.createTime(requestDto);
        }).isInstanceOf(ConflictException.class);
    }

    @DisplayName("예약 내역이 존재하는 시간은 삭제할 수 없다.")
    @Test
    void deleteTimeExistReservationTime() {
        // given
        LocalTime time = LocalTime.of(12, 0);
        ReservationTime reservationTime = new ReservationTime(1L, time);
        reservationTimeRepository.save(reservationTime);
        LocalDate date = LocalDate.of(2025, 1, 2);
        reservationRepository.save(new Reservation(1L, "test1", date, reservationTime, null));

        // when & then
        assertThatThrownBy(() -> {
            service.deleteTime(1L);
        }).isInstanceOf(BadRequestException.class);
    }
}
