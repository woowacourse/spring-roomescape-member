package roomescape.domain.reservationtime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import roomescape.domain.reservationtime.dto.CreateTimeRequest;
import roomescape.domain.reservationtime.dto.CreateTimeResponse;
import roomescape.domain.reservationtime.dto.ReservationTimeResponse;
import roomescape.support.exception.RoomescapeException;
import roomescape.support.fake.FakeReservationRepository;
import roomescape.support.fake.FakeReservationTimeRepository;

class ReservationTimeServiceTest {

    @Test
    void 예약_시간을_생성한다() {
        // given
        FakeReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository();
        FakeReservationRepository reservationRepository = new FakeReservationRepository();
        ReservationTimeService reservationTimeService = new ReservationTimeService(
            reservationTimeRepository,
            reservationRepository
        );

        // when
        CreateTimeResponse response = reservationTimeService.createReservationTime(
            new CreateTimeRequest(LocalTime.of(10, 0))
        );

        // then
        assertSoftly(softly -> {
            assertThat(response.id()).isEqualTo(1L);
            assertThat(response.startAt()).isEqualTo(LocalTime.of(10, 0));
            assertThat(reservationTimeRepository.savedReservationTime.getStartAt()).isEqualTo(LocalTime.of(10, 0));
        });
    }

    @Test
    void 예약_시간_목록을_조회한다() {
        // given
        FakeReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository();
        reservationTimeRepository.findAllResult = List.of(ReservationTime.of(1L, LocalTime.of(10, 0)));
        ReservationTimeService reservationTimeService = new ReservationTimeService(
            reservationTimeRepository,
            new FakeReservationRepository()
        );

        // when
        List<ReservationTimeResponse> responses = reservationTimeService.getAllReservationTime();

        // then
        assertSoftly(softly -> {
            assertThat(responses).hasSize(1);
            assertThat(responses.getFirst().id()).isEqualTo(1L);
            assertThat(responses.getFirst().startAt()).isEqualTo(LocalTime.of(10, 0));
        });
    }

    @Test
    void 이미_예약이_존재하는_시간은_삭제할_수_없다() {
        // given
        FakeReservationRepository reservationRepository = new FakeReservationRepository();
        reservationRepository.countByTimeIdResult = 1;
        FakeReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository();
        ReservationTimeService reservationTimeService = new ReservationTimeService(
            reservationTimeRepository,
            reservationRepository
        );

        // when & then
        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(1L))
            .isInstanceOf(RoomescapeException.class)
            .hasMessage("이미 예약이 존재하는 시간대는 삭제할 수 없습니다.");
    }

    @Test
    void 예약이_없는_시간은_삭제한다() {
        // given
        FakeReservationRepository reservationRepository = new FakeReservationRepository();
        FakeReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository();
        ReservationTimeService reservationTimeService = new ReservationTimeService(
            reservationTimeRepository,
            reservationRepository
        );

        // when
        reservationTimeService.deleteReservationTime(1L);

        // then
        assertThat(reservationTimeRepository.deletedId).isEqualTo(1L);
    }
}
