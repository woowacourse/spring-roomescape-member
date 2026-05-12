package roomescape.domain.reservationdate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservationdate.dto.AdminReservationDateResponse;
import roomescape.domain.reservationdate.dto.CreateReservationDateRequest;
import roomescape.domain.reservationdate.dto.CreateReservationDateResponse;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.support.exception.RoomescapeException;
import roomescape.support.fake.FakeReservationDateRepository;
import roomescape.support.fake.FakeReservationRepository;

class ReservationDateServiceTest {

    private FakeReservationRepository reservationRepository;
    private FakeReservationDateRepository reservationDateRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = new FakeReservationRepository();
        reservationDateRepository = new FakeReservationDateRepository();
    }

    @Test
    void 예약_날짜를_생성한다() {
        // given
        ReservationDateService reservationDateService = new ReservationDateService(
            reservationRepository,
            reservationDateRepository
        );

        // when
        CreateReservationDateResponse response = reservationDateService.createReservationDate(
            new CreateReservationDateRequest(LocalDate.of(2026, 5, 4))
        );
        ReservationDate reservationDate = reservationDateRepository.findById(response.id()).orElseThrow();

        // then
        assertSoftly(softly -> {
            assertThat(response.id()).isEqualTo(reservationDate.getId());
            assertThat(response.reservationDate()).isEqualTo(LocalDate.of(2026, 5, 4));
            assertThat(reservationDate.getDate()).isEqualTo(LocalDate.of(2026, 5, 4));
        });
    }

    @Test
    void 예약_날짜_목록을_조회한다() {
        // given
        ReservationDate reservationDate = reservationDateRepository.save(
            ReservationDate.createWithoutId(LocalDate.of(2026, 5, 4)));
        ReservationDateService reservationDateService = new ReservationDateService(
            reservationRepository,
            reservationDateRepository
        );

        // when
        List<AdminReservationDateResponse> responses = reservationDateService.getAllReservationDateForAdmin();

        // then
        assertSoftly(softly -> {
            assertThat(responses).hasSize(1);
            assertThat(responses.getFirst().id()).isEqualTo(reservationDate.getId());
            assertThat(responses.getFirst().reservationDate()).isEqualTo(LocalDate.of(2026, 5, 4));
        });
    }

    @Test
    void 이미_예약이_존재하는_날짜는_삭제할_수_없다() {
        // given
        ReservationDate reservationDate = reservationDateRepository.save(
            ReservationDate.createWithoutId(LocalDate.of(2026, 5, 4)));
        reservationRepository.save(
            Reservation.createWithoutId(
                "보예",
                reservationDate,
                ReservationTime.of(1L, LocalTime.of(10, 0)),
                Theme.of(1L, "공포", "무서운 테마", "theme-url")
            )
        );
        ReservationDateService reservationDateService = new ReservationDateService(
            reservationRepository,
            reservationDateRepository
        );

        // when & then
        assertThatThrownBy(() -> reservationDateService.deleteReservationDate(reservationDate.getId()))
            .isInstanceOf(RoomescapeException.class)
            .hasMessage("이미 예약이 존재하는 날짜는 삭제할 수 없습니다.");
    }

    @Test
    void 예약이_없는_날짜는_삭제한다() {
        // given
        ReservationDate reservationDate = reservationDateRepository.save(
            ReservationDate.createWithoutId(LocalDate.of(2026, 5, 4)));
        ReservationDateService reservationDateService = new ReservationDateService(
            reservationRepository,
            reservationDateRepository
        );

        // when
        reservationDateService.deleteReservationDate(reservationDate.getId());

        // then
        assertThat(reservationDateRepository.findById(reservationDate.getId())).isEmpty();
    }
}
