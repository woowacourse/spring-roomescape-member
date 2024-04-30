package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.ReservationTime;
import roomescape.dto.reservationtime.ReservationTimeCreateRequest;
import roomescape.dto.reservationtime.ReservationTimeResponse;
import roomescape.exception.BadRequestException;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationtime.ReservationTimeRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@DisplayName("예약 시간 서비스")
class ReservationTimeServiceTest {

    private ReservationTimeService reservationTimeService;
    @Mock
    private ReservationTimeRepository reservationTimeRepository;
    @Mock
    private ReservationRepository reservationRepository;
    private Long id;
    private LocalTime startAt;
    private ReservationTime reservationTimeFixture;

    @BeforeEach
    void setUp() {
        this.reservationTimeService = new ReservationTimeService(reservationTimeRepository, reservationRepository);
        this.id = 1L;
        this.startAt = LocalTime.of(10, 10);
        this.reservationTimeFixture = new ReservationTime(id, startAt);
    }

    @DisplayName("에약 시간 서비스는 시간을 생성한다.")
    @Test
    void createTime() {
        // given
        Mockito.when(reservationTimeRepository.save(any()))
                .thenReturn(reservationTimeFixture);
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(startAt);

        // when
        ReservationTimeResponse reservationTime = reservationTimeService.createTime(request);

        // then
        assertThat(reservationTime.startAt())
                .isEqualTo(startAt);
    }

    @DisplayName("예약 시간 서비스는 중복된 예약 시간 요청이 들어오면 예외가 발생한다.")
    @Test
    void validateIsDuplicated() {
        // given
        Mockito.when(reservationTimeRepository.findAll())
                .thenReturn(List.of(reservationTimeFixture));
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(reservationTimeFixture.getStartAt());

        // when & then
        assertThatThrownBy(() -> reservationTimeService.createTime(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("중복된 예약 시간입니다.");
    }

    @DisplayName("예약 시간 서비스는 id에 맞는 시간을 반환한다.")
    @Test
    void readReservationTime() {
        // given
        Mockito.when(reservationTimeRepository.findById(id))
                .thenReturn(Optional.of(reservationTimeFixture));

        // when
        ReservationTimeResponse reservationTime = reservationTimeService.readReservationTime(id);

        // then
        assertThat(reservationTime.startAt())
                .isEqualTo(startAt);
    }

    @DisplayName("예약 시간 서비스는 시간들을 반환한다.")
    @Test
    void readReservationTimes() {
        // given
        Mockito.when(reservationTimeRepository.findAll())
                .thenReturn(List.of(reservationTimeFixture));

        // when
        List<ReservationTimeResponse> reservationTimes = reservationTimeService.readReservationTimes();

        // then
        assertThat(reservationTimes.size()).isEqualTo(1);
    }

    @DisplayName("예약 시간 서비스는 id에 맞는 시간을 삭제한다.")
    @Test
    void deleteTime() {
        // given
        Mockito.when(reservationRepository.existsByTimeId(id))
                        .thenReturn(false);
        Mockito.doNothing().when(reservationTimeRepository).deleteById(id);

        // when & then
        assertThatCode(() -> reservationTimeService.deleteTime(id))
                .doesNotThrowAnyException();
    }

    @DisplayName("예약 시간 서비스는 id에 맞는 시간에 예약이 존재하면 예외가 발생한다.")
    @Test
    void deleteTimeWithExistsReservation() {
        // given
        Mockito.when(reservationRepository.existsByTimeId(id))
                .thenReturn(true);

        // when & then
        assertThatThrownBy(() -> reservationTimeService.deleteTime(id))
                .isInstanceOf(BadRequestException.class);
    }
}
