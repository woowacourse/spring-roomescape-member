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
import roomescape.domain.reservationtime.dto.ReservationTimeAvailabilityResponse;
import roomescape.domain.reservationtime.dto.ReservationTimeResponse;
import roomescape.domain.theme.Theme;
import roomescape.support.exception.NotFoundException;
import roomescape.support.exception.RoomescapeException;
import roomescape.support.fake.FakeReservationDateRepository;
import roomescape.support.fake.FakeReservationRepository;
import roomescape.support.fake.FakeReservationTimeRepository;
import roomescape.support.fake.FakeThemeRepository;

class ReservationTimeServiceTest {

    private FakeReservationRepository reservationRepository;
    private FakeReservationTimeRepository reservationTimeRepository;
    private FakeThemeRepository themeRepository;
    private FakeReservationDateRepository reservationDateRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = new FakeReservationRepository();
        reservationTimeRepository = new FakeReservationTimeRepository();
        themeRepository = new FakeThemeRepository();
        reservationDateRepository = new FakeReservationDateRepository();
    }

    @Test
    void 예약_시간을_생성한다() {
        // given
        ReservationTimeService reservationTimeService = createReservationTimeService();

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
        ReservationTimeService reservationTimeService = createReservationTimeService();

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
        ReservationTimeService reservationTimeService = createReservationTimeService();

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
        ReservationTimeService reservationTimeService = createReservationTimeService();

        // when
        reservationTimeService.deleteReservationTime(reservationTime.getId());

        // then
        assertThat(reservationTimeRepository.findById(reservationTime.getId())).isEmpty();
    }

    @Test
    void 예약_가능_시간을_조회한다() {
        // given
        ReservationTime firstReservationTime = reservationTimeRepository.save(
            ReservationTime.createWithoutId(LocalTime.of(10, 0))
        );
        ReservationTime secondReservationTime = reservationTimeRepository.save(
            ReservationTime.createWithoutId(LocalTime.of(11, 0))
        );
        ReservationDate reservationDate = reservationDateRepository.save(
            ReservationDate.createWithoutId(LocalDate.of(2026, 5, 16))
        );
        Theme theme = themeRepository.save(Theme.createWithoutId("공포", "무서운 테마", "theme-url"));
        reservationRepository.save(
            Reservation.createWithoutId("보예", reservationDate, firstReservationTime, theme)
        );
        ReservationTimeService reservationTimeService = createReservationTimeService();

        // when
        List<ReservationTimeAvailabilityResponse> responses = reservationTimeService.getReservationTimeAvailability(
            theme.getId(),
            reservationDate.getId()
        );

        // then
        assertThat(responses)
            .extracting(
                ReservationTimeAvailabilityResponse::timeId,
                ReservationTimeAvailabilityResponse::startAt,
                ReservationTimeAvailabilityResponse::available
            )
            .containsExactly(
                tuple(firstReservationTime.getId(), LocalTime.of(10, 0), false),
                tuple(secondReservationTime.getId(), LocalTime.of(11, 0), true)
            );
    }

    @Test
    void 존재하지_않는_테마로_예약_가능_시간을_조회할_수_없다() {
        // given
        ReservationDate reservationDate = reservationDateRepository.save(
            ReservationDate.createWithoutId(LocalDate.of(2026, 5, 16))
        );
        ReservationTimeService reservationTimeService = createReservationTimeService();

        // when & then
        assertThatThrownBy(() -> reservationTimeService.getReservationTimeAvailability(1L, reservationDate.getId()))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("존재하지 않는 테마 입니다.");
    }

    @Test
    void 존재하지_않는_날짜로_예약_가능_시간을_조회할_수_없다() {
        // given
        Theme theme = themeRepository.save(Theme.createWithoutId("공포", "무서운 테마", "theme-url"));
        ReservationTimeService reservationTimeService = createReservationTimeService();

        // when & then
        assertThatThrownBy(() -> reservationTimeService.getReservationTimeAvailability(theme.getId(), 1L))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("존재하지 않는 날짜 입니다.");
    }

    private ReservationTimeService createReservationTimeService() {
        return new ReservationTimeService(
            reservationTimeRepository,
            reservationRepository,
            themeRepository,
            reservationDateRepository
        );
    }
}
