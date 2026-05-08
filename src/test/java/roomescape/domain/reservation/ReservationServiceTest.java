package roomescape.domain.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import roomescape.domain.reservation.dto.CreateReservationRequest;
import roomescape.domain.reservation.dto.CreateReservationResponse;
import roomescape.domain.reservation.dto.ReservationResponse;
import roomescape.domain.reservationdate.ReservationDate;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.support.exception.RoomescapeException;
import roomescape.support.fake.FakeReservationDateRepository;
import roomescape.support.fake.FakeReservationRepository;
import roomescape.support.fake.FakeReservationTimeRepository;
import roomescape.support.fake.FakeThemeRepository;

class ReservationServiceTest {

    @Test
    void 존재하는_예약_시간으로_예약을_생성한다() {
        // given
        FakeReservationRepository reservationRepository = new FakeReservationRepository();
        FakeReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository();
        FakeReservationDateRepository reservationDateRepository = new FakeReservationDateRepository();
        FakeThemeRepository themeRepository = new FakeThemeRepository();
        ReservationTime reservationTime = ReservationTime.of(1L, LocalTime.of(10, 0));
        ReservationDate reservationDate = ReservationDate.of(2L, LocalDate.of(2026, 5, 4));
        Theme theme = Theme.of(3L, "공포", "무서운 테마", "theme-url");
        reservationTimeRepository.reservationTime = reservationTime;
        reservationDateRepository.reservationDate = reservationDate;
        themeRepository.theme = theme;
        ReservationService reservationService = new ReservationService(
            reservationRepository,
            reservationTimeRepository,
            reservationDateRepository,
            themeRepository
        );
        CreateReservationRequest request = new CreateReservationRequest("보예", 2L, 1L, 3L);

        // when
        CreateReservationResponse response = reservationService.createReservation(request);

        // then
        assertSoftly(softly -> {
            assertThat(response.id()).isEqualTo(1L);
            assertThat(response.name()).isEqualTo("보예");
            assertThat(response.date()).isEqualTo(LocalDate.of(2026, 5, 4));
            assertThat(response.time()).isEqualTo(LocalTime.of(10, 0));
            assertThat(response.theme().name()).isEqualTo("공포");
            assertThat(reservationRepository.savedReservation.getTime()).isEqualTo(reservationTime);
            assertThat(reservationRepository.savedReservation.getTheme()).isEqualTo(theme);
        });
    }

    @Test
    void 존재하지_않는_예약_시간으로_예약을_생성하면_예외가_발생한다() {
        // given
        ReservationService reservationService = new ReservationService(
            new FakeReservationRepository(),
            new FakeReservationTimeRepository(),
            new FakeReservationDateRepository(),
            new FakeThemeRepository()
        );
        CreateReservationRequest request = new CreateReservationRequest("보예", 1L, 1L, 1L);

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(request))
            .isInstanceOf(RoomescapeException.class)
            .hasMessage("존재하지 않는 예약 시간대 입니다.");
    }

    @Test
    void 존재하지_않는_테마로_예약을_생성하면_예외가_발생한다() {
        // given
        FakeReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository();
        FakeReservationDateRepository reservationDateRepository = new FakeReservationDateRepository();
        reservationTimeRepository.reservationTime = ReservationTime.of(1L, LocalTime.of(10, 0));
        reservationDateRepository.reservationDate = ReservationDate.of(2L, LocalDate.of(2026, 5, 4));
        ReservationService reservationService = new ReservationService(
            new FakeReservationRepository(),
            reservationTimeRepository,
            reservationDateRepository,
            new FakeThemeRepository()
        );
        CreateReservationRequest request = new CreateReservationRequest("보예", 2L, 1L, 3L);

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(request))
            .isInstanceOf(RoomescapeException.class)
            .hasMessage("존재하지 않는 테마 입니다.");
    }

    @Test
    void 예약_목록을_조회한다() {
        // given
        FakeReservationRepository reservationRepository = new FakeReservationRepository();
        FakeReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository();
        reservationRepository.findAllResult = List.of(
            Reservation.of(
                1L,
                "보예",
                ReservationDate.of(3L, LocalDate.of(2026, 5, 4)),
                ReservationTime.of(2L, LocalTime.of(10, 0)),
                Theme.of(4L, "공포", "무서운 테마", "theme-url")
            )
        );
        ReservationService reservationService = new ReservationService(
            reservationRepository,
            reservationTimeRepository,
            new FakeReservationDateRepository(),
            new FakeThemeRepository()
        );

        // when
        List<ReservationResponse> responses = reservationService.getAllReservations();

        // then
        assertSoftly(softly -> {
            assertThat(responses).hasSize(1);
            assertThat(responses.getFirst().id()).isEqualTo(1L);
            assertThat(responses.getFirst().name()).isEqualTo("보예");
            assertThat(responses.getFirst().date()).isEqualTo(LocalDate.of(2026, 5, 4));
            assertThat(responses.getFirst().time().id()).isEqualTo(2L);
            assertThat(responses.getFirst().time().startAt()).isEqualTo(LocalTime.of(10, 0));
            assertThat(responses.getFirst().theme().id()).isEqualTo(4L);
            assertThat(responses.getFirst().theme().name()).isEqualTo("공포");
        });
    }
}
