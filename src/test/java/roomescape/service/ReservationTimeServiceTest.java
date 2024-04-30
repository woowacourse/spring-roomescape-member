package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeResponse;
import roomescape.repository.ReservationTimeRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static roomescape.TestFixture.MIA_RESERVATION_TIME;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceTest {
    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @InjectMocks
    private ReservationTimeService reservationTimeService;

    @Test
    @DisplayName("예약 시간을 생성한다.")
    void create() {
        // given
        ReservationTime reservationTime = new ReservationTime(MIA_RESERVATION_TIME);
        ReservationTime expectedReservationTime = new ReservationTime(1L, reservationTime);

        BDDMockito.given(reservationTimeRepository.save(any()))
                .willReturn(expectedReservationTime);

        // when
        ReservationTimeResponse response = reservationTimeService.create(reservationTime);

        // then
        assertAll(
                () -> assertThat(response.id()).isEqualTo(expectedReservationTime.getId()),
                () -> assertThat(response.startAt()).isEqualTo(expectedReservationTime.getStartAt().toString())
        );
    }

    @Test
    @DisplayName("예약 시간 목록을 조회한다.")
    void getAll() {
        // given
        ReservationTime reservationTime = new ReservationTime(MIA_RESERVATION_TIME);

        BDDMockito.given(reservationTimeRepository.findAll())
                .willReturn(List.of(reservationTime));

        // when
        List<ReservationTimeResponse> responses = reservationTimeService.findAll();

        // then
        assertThat(responses).hasSize(1)
                .extracting(ReservationTimeResponse::startAt)
                .contains(MIA_RESERVATION_TIME);
    }

    @Test
    @DisplayName("예약을 삭제한다.")
    void delete() {
        // given
        ReservationTime reservationTime = new ReservationTime(MIA_RESERVATION_TIME);

        BDDMockito.given(reservationTimeRepository.findById(anyLong()))
                .willReturn(Optional.of(reservationTime));

        // when & then
        assertThatCode(() -> reservationTimeService.delete(1L))
                .doesNotThrowAnyException();
    }
}
