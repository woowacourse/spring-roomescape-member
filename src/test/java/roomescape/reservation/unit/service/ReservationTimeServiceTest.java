package roomescape.reservation.unit.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import roomescape.global.error.exception.BadRequestException;
import roomescape.global.error.exception.ConflictException;
import roomescape.reservation.dto.request.ReservationTimeRequest.ReservationTimeCreateRequest;
import roomescape.reservation.dto.response.ReservationTimeResponse.ReservationTimeCreateResponse;
import roomescape.reservation.dto.response.ReservationTimeResponse.ReservationTimeReadResponse;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.entity.ReservationTime;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.ReservationTimeRepository;
import roomescape.reservation.service.ReservationTimeService;
import roomescape.reservation.unit.repository.FakeReservationTimeRepository;

class ReservationTimeServiceTest {

    private ReservationTimeRepository reservationTimeRepository;
    private ReservationRepository reservationRepository;
    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void beforeEach() {
        reservationTimeRepository = new FakeReservationTimeRepository();
        reservationTimeService = new ReservationTimeService(reservationTimeRepository, reservationRepository);
    }

    @Test
    @Disabled
    @DisplayName("모든 시간을 조회한다.")
    void getReservationTimes() {
        // given
        long id = 1L;
        LocalTime startAt = LocalTime.of(10, 0);
        ReservationTime reservationTime = new ReservationTime(
                id,
                startAt
        );
        reservationTimeRepository.save(reservationTime);

        // when
        List<ReservationTimeReadResponse> reservationTimeReadResponses = reservationTimeService.getAllTimes();

        // then
        ReservationTimeReadResponse reservationTimeResponse = reservationTimeReadResponses.getFirst();
        assertThat(reservationTimeResponse.id()).isEqualTo(id);
        assertThat(reservationTimeResponse.startAt()).isEqualTo(startAt);
    }

    @Test
    @Disabled
    @DisplayName("시간을 저장한다.")
    void createReservationTime() {
        // given
        LocalTime time = LocalTime.of(10, 0);
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(
                time
        );

        // when
        ReservationTimeCreateResponse reservationTimeCreateResponse = reservationTimeService.createTime(request);

        // then
        assertThat(reservationTimeCreateResponse.id()).isEqualTo(1L);
        assertThat(reservationTimeCreateResponse.startAt()).isEqualTo(time);
    }

    @Test
    @Disabled
    @DisplayName("시간이 중복되면 예외가 발생한다.")
    void createReservationTime_Duplicate() {
        // given
        ReservationTime reservationTime = new ReservationTime(
                1L,
                LocalTime.of(10, 0)
        );
        reservationTimeRepository.save(reservationTime);
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(
                LocalTime.of(10, 0)
        );

        // when & then
        assertThatThrownBy(() -> reservationTimeService.createTime(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("10:00은 이미 존재하는 시간입니다.");
    }

    @Test
    @Disabled
    @DisplayName("id로 시간을 삭제한다.")
    void deleteReservationTime() {
        // given
        ReservationTime reservationTime = new ReservationTime(
                1L,
                LocalTime.of(10, 0)
        );
        reservationTimeRepository.save(reservationTime);

        // when
        reservationTimeService.deleteTime(1L);

        // then
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        assertThat(reservationTimes.size()).isEqualTo(0);
    }


    @DisplayName("예약 생성이 가능한 시간은 10:00 ~ 22:00 이다.")
    @ParameterizedTest
    @MethodSource
    void validOperatingTime(LocalTime startAt) {
        // given
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(startAt);

        // when
        assertThatCode(() -> {
            reservationTimeService.createTime(request);
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
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(startAt);
        // when & then
        Assertions.assertThatThrownBy(() -> {
            reservationTimeService.createTime(request);
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

        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(duplicatedTime);

        // when & then
        Assertions.assertThatThrownBy(() -> {
            reservationTimeService.createTime(request);
        }).isInstanceOf(ConflictException.class);
    }

    @DisplayName("예약 내역이 존재하는 시간은 삭제할 수 없다.")
    @Test
    @Disabled
    void deleteTimeExistReservationTime() {
        // given
        LocalTime time = LocalTime.of(12, 0);
        ReservationTime reservationTime = new ReservationTime(1L, time);
        reservationTimeRepository.save(reservationTime);
        LocalDate date = LocalDate.of(2025, 1, 2);
        reservationRepository.save(new Reservation(1L, "test1", date, reservationTime, null));

        // when & then
        Assertions.assertThatThrownBy(() -> {
            reservationTimeService.deleteTime(1L);
        }).isInstanceOf(BadRequestException.class);
    }
}
