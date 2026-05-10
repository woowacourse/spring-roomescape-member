package roomescape.domain.reservationdate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import roomescape.domain.reservationdate.dto.AdminReservationDateResponse;
import roomescape.domain.reservationdate.dto.CreateReservationDateRequest;
import roomescape.domain.reservationdate.dto.CreateReservationDateResponse;
import roomescape.support.exception.RoomescapeException;
import roomescape.support.fake.FakeReservationDateRepository;
import roomescape.support.fake.FakeReservationRepository;

class ReservationDateServiceTest {

    @Test
    void 예약_날짜를_생성한다() {
        // given
        FakeReservationRepository reservationRepository = new FakeReservationRepository();
        FakeReservationDateRepository reservationDateRepository = new FakeReservationDateRepository();
        ReservationDateService reservationDateService = new ReservationDateService(
            reservationRepository,
            reservationDateRepository
        );

        // when
        CreateReservationDateResponse response = reservationDateService.createReservationDate(
            new CreateReservationDateRequest(LocalDate.of(2026, 5, 4))
        );

        // then
        assertSoftly(softly -> {
            assertThat(response.id()).isEqualTo(1L);
            assertThat(response.reservationDate()).isEqualTo(LocalDate.of(2026, 5, 4));
            assertThat(reservationDateRepository.savedReservationDate.getDate()).isEqualTo(LocalDate.of(2026, 5, 4));
        });
    }

    @Test
    void 예약_날짜_목록을_조회한다() {
        // given
        FakeReservationRepository reservationRepository = new FakeReservationRepository();
        FakeReservationDateRepository reservationDateRepository = new FakeReservationDateRepository();
        reservationDateRepository.findAllResult = List.of(ReservationDate.of(1L, LocalDate.of(2026, 5, 4)));
        ReservationDateService reservationDateService = new ReservationDateService(
            reservationRepository,
            reservationDateRepository
        );

        // when
        List<AdminReservationDateResponse> responses = reservationDateService.getAllReservationDateForAdmin();

        // then
        assertSoftly(softly -> {
            assertThat(responses).hasSize(1);
            assertThat(responses.getFirst().id()).isEqualTo(1L);
            assertThat(responses.getFirst().reservationDate()).isEqualTo(LocalDate.of(2026, 5, 4));
        });
    }

    @Test
    void 이미_예약이_존재하는_날짜는_삭제할_수_없다() {
        // given
        FakeReservationRepository reservationRepository = new FakeReservationRepository();
        reservationRepository.countByReservationDateIdResult = 1;
        FakeReservationDateRepository reservationDateRepository = new FakeReservationDateRepository();
        ReservationDateService reservationDateService = new ReservationDateService(
            reservationRepository,
            reservationDateRepository
        );

        // when & then
        assertThatThrownBy(() -> reservationDateService.deleteReservationDate(1L))
            .isInstanceOf(RoomescapeException.class)
            .hasMessage("이미 예약이 존재하는 날짜는 삭제할 수 없습니다.");
    }

    @Test
    void 예약이_없는_날짜는_삭제한다() {
        // given
        FakeReservationRepository reservationRepository = new FakeReservationRepository();
        FakeReservationDateRepository reservationDateRepository = new FakeReservationDateRepository();
        ReservationDateService reservationDateService = new ReservationDateService(
            reservationRepository,
            reservationDateRepository
        );

        // when
        reservationDateService.deleteReservationDate(1L);

        // then
        assertThat(reservationDateRepository.deletedId).isEqualTo(1L);
    }
}
