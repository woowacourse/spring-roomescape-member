package roomescape.domain.reservationtime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.tuple;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservationdate.ReservationDate;
import roomescape.domain.reservationtime.dto.CreateTimeRequest;
import roomescape.domain.reservationtime.dto.CreateTimeResponse;
import roomescape.domain.reservationtime.dto.ReservationTimeResponse;
import roomescape.domain.theme.Theme;
import roomescape.support.exception.RoomescapeException;
import roomescape.support.fake.FakeReservationRepository;
import roomescape.support.fake.FakeReservationTimeRepository;

class ReservationTimeServiceTest {

    private FakeReservationRepository reservationRepository;
    private FakeReservationTimeRepository reservationTimeRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = new FakeReservationRepository();
        reservationTimeRepository = new FakeReservationTimeRepository();
    }

    @Test
    void 예약_시간을_생성한다() {
        // given
        ReservationTimeService reservationTimeService = new ReservationTimeService(
            reservationTimeRepository,
            reservationRepository
        );

        // when
        CreateTimeResponse response = reservationTimeService.createReservationTime(
            new CreateTimeRequest(LocalTime.of(10, 0))
        );
        ReservationTime savedReservationTime = reservationTimeRepository.findById(response.id()).orElseThrow();

        // then
        assertSoftly(softly -> {
            assertThat(response.id()).isEqualTo(savedReservationTime.getId());
            assertThat(response.startAt()).isEqualTo(LocalTime.of(10, 0));
            assertThat(savedReservationTime.getStartAt()).isEqualTo(LocalTime.of(10, 0));
        });
    }

    @Test
    void 예약_시간_목록을_조회한다() {
        // given
        reservationTimeRepository.save(ReservationTime.createWithoutId(LocalTime.of(10, 0)));
        reservationTimeRepository.save(ReservationTime.createWithoutId(LocalTime.of(11, 0)));
        ReservationTimeService reservationTimeService = new ReservationTimeService(
            reservationTimeRepository,
            reservationRepository
        );

        // when
        List<ReservationTimeResponse> responses = reservationTimeService.getAllReservationTime();

        // then
        assertSoftly(softly -> {
            assertThat(responses).hasSize(2);
            assertThat(responses)
                .extracting(ReservationTimeResponse::id, ReservationTimeResponse::startAt)
                .containsExactly(
                    tuple(1L, LocalTime.of(10, 0)),
                    tuple(2L, LocalTime.of(11, 0))
                );
        });
    }

    @Test
    void 이미_예약이_존재하는_시간은_삭제할_수_없다() {
        // given
        ReservationTime reservationTime = reservationTimeRepository.save(
            ReservationTime.createWithoutId(LocalTime.of(10, 0))
        );
        reservationRepository.save(
            Reservation.createWithoutId(
                "보예",
                ReservationDate.of(1L, LocalDate.of(2026, 5, 12)),
                reservationTime,
                Theme.of(1L, "공포", "무서운 테마", "theme-url")
            )
        );
        ReservationTimeService reservationTimeService = new ReservationTimeService(
            reservationTimeRepository,
            reservationRepository
        );

        // when & then
        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(reservationTime.getId()))
            .isInstanceOf(RoomescapeException.class)
            .hasMessage("이미 예약이 존재하는 시간대는 삭제할 수 없습니다.");
    }

    @Test
    void 예약이_없는_시간은_삭제한다() {
        // given
        ReservationTime reservationTime = reservationTimeRepository.save(
            ReservationTime.createWithoutId(LocalTime.of(10, 0))
        );
        ReservationTimeService reservationTimeService = new ReservationTimeService(
            reservationTimeRepository,
            reservationRepository
        );

        // when
        reservationTimeService.deleteReservationTime(reservationTime.getId());

        // then
        assertThat(reservationTimeRepository.findById(reservationTime.getId())).isEmpty();
    }
}
