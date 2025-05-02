package roomescape.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationSlot;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.AddReservationRequest;
import roomescape.dto.request.AddReservationTimeRequest;
import roomescape.dto.request.AddThemeRequest;
import roomescape.dto.request.AvailableTimeRequest;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.ReservationService;
import roomescape.unit.repository.FakeReservationRepository;
import roomescape.unit.repository.FakeReservationTimeRepository;
import roomescape.unit.repository.FakeThemeRepository;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReservationServiceTest {

    static ReservationService reservationService;
    static ReservationTimeRepository reservationTimeRepository;
    static ReservationRepository reservationRepository;
    static ThemeRepository themeRepository;

    @BeforeEach
    void setup() {
        reservationRepository = new FakeReservationRepository();
        reservationTimeRepository = new FakeReservationTimeRepository();
        themeRepository = new FakeThemeRepository();
        reservationService = new ReservationService(new FakeReservationRepository(), reservationTimeRepository,
                themeRepository);
    }

    @Test
    void 예약을_추가하고_조회할_수_있다() {
        ReservationTime reservationTime = reservationTimeRepository.add(
                new AddReservationTimeRequest(LocalTime.now().plusHours(1L)).toEntity());
        Theme theme = themeRepository.add(new AddThemeRequest("공포의 숲", "무서운 테마", "image.png").toEntity());

        AddReservationRequest request = new AddReservationRequest("praisebak", LocalDate.now().plusDays(1L),
                reservationTime.getId(), theme.getId());
        reservationService.addReservation(request);

        assertThat(reservationService.allReservations().size()).isEqualTo(1);
    }

    @Test
    void 이전_날짜에_예약할_수_없다() {
        ReservationTime reservationTime = reservationTimeRepository.add(
                new AddReservationTimeRequest(LocalTime.now().plusHours(1L)).toEntity());
        Theme theme = themeRepository.add(new AddThemeRequest("과거의 방", "옛날 테마", "past.png").toEntity());

        assertThatThrownBy(() -> reservationService.addReservation(
                new AddReservationRequest("투다", LocalDate.now().minusDays(1), reservationTime.getId(), theme.getId())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 같은날짜일시_이전_시간에_예약할_수_없다() {
        ReservationTime reservationTime = reservationTimeRepository.add(
                new AddReservationTimeRequest(LocalTime.now().minusHours(1L)).toEntity());
        Theme theme = themeRepository.add(new AddThemeRequest("지각의 방", "지각 금지", "late.png").toEntity());

        assertThatThrownBy(() -> reservationService.addReservation(
                new AddReservationRequest("투다", LocalDate.now(), reservationTime.getId(), theme.getId())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 이후_날짜에_예약할_수_있다() {
        ReservationTime reservationTime = reservationTimeRepository.add(
                new AddReservationTimeRequest(LocalTime.now().plusHours(1L)).toEntity());
        Theme theme = themeRepository.add(new AddThemeRequest("미래의 방", "SF 컨셉", "future.png").toEntity());

        assertThatCode(() -> reservationService.addReservation(
                new AddReservationRequest("투다", LocalDate.now().plusDays(1), reservationTime.getId(), theme.getId())))
                .doesNotThrowAnyException();
    }

    @Test
    void 같은날짜일시_이후_시간_예약할_수_있다() {
        ReservationTime reservationTime = reservationTimeRepository.add(
                new AddReservationTimeRequest(LocalTime.now().plusHours(1L)).toEntity());
        Theme theme = themeRepository.add(new AddThemeRequest("정시의 방", "시간 엄수", "on_time.png").toEntity());

        assertThatCode(() -> reservationService.addReservation(
                new AddReservationRequest("투다", LocalDate.now(), reservationTime.getId(), theme.getId())))
                .doesNotThrowAnyException();
    }

    @Test
    void 예약을_삭제하고_조회할_수_있다() {
        ReservationTime reservationTime = reservationTimeRepository.add(
                new AddReservationTimeRequest(LocalTime.now().plusHours(1L)).toEntity());
        Theme theme = themeRepository.add(new AddThemeRequest("삭제의 방", "삭제 가능", "delete.png").toEntity());

        Reservation reservation = reservationService.addReservation(
                new AddReservationRequest("praisebak", LocalDate.now().plusDays(1L), reservationTime.getId(),
                        theme.getId()));

        assertThat(reservationService.allReservations().size()).isEqualTo(1);
        reservationService.deleteReservation(reservation.getId());
        assertThat(reservationService.allReservations().size()).isEqualTo(0);
    }

    @Test
    void 중복_예약은_불가능하다() {
        LocalTime localTime = LocalTime.now().plusHours(1L);
        ReservationTime reservationTime = reservationTimeRepository.add(
                new AddReservationTimeRequest(localTime).toEntity());
        Theme theme = themeRepository.add(new AddThemeRequest("중복 금지 방", "한 번만 가능", "unique.png").toEntity());

        reservationService.addReservation(
                new AddReservationRequest("투다", LocalDate.now(), reservationTime.getId(), theme.getId()));

        assertThatThrownBy(() -> reservationService.addReservation(
                new AddReservationRequest("투다", LocalDate.now(), reservationTime.getId(), theme.getId())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 선택된_테마와_날짜에_대해서_가능한_시간들을_확인할_수_있다() {
        LocalTime firstTime = LocalTime.now().plusHours(1L);
        LocalTime secondTime = LocalTime.now().plusHours(2L);

        LocalDate today = LocalDate.now();
        ReservationTime firstReservationTime = reservationTimeRepository.add(
                new AddReservationTimeRequest(firstTime).toEntity());
        ReservationTime secondReservationTime = reservationTimeRepository.add(
                new AddReservationTimeRequest(secondTime).toEntity());
        Theme theme = themeRepository.add(new AddThemeRequest("테마", "테마2", "unique.png").toEntity());

        reservationService.addReservation(
                new AddReservationRequest("투다", today, firstReservationTime.getId(), theme.getId()));

        AvailableTimeRequest availableTimeRequest = new AvailableTimeRequest(today, theme.getId());
        List<ReservationSlot> reservationAvailabilities = reservationService.availableReservationTimes(
                        availableTimeRequest)
                .getReservationSlots();

        List<ReservationSlot> reservationSlots = List.of(new ReservationSlot(1L, firstTime, true),
                new ReservationSlot(2L, secondTime, false));

        assertThat(reservationAvailabilities).containsExactlyInAnyOrderElementsOf(reservationSlots);
    }

    @Test
    void 최근_일주일을_기준으로_예약이_많은_테마_10개를_확인할_수_있다() {
        for (int i = 0; i < 10; i++) {
            Theme theme = new Theme(null, "테마" + 1, "테마", "thumbnail");
            themeRepository.add(theme);
        }
        for (int i = 0; i < 6; i++) {
            LocalTime localTime = LocalTime.of(10 + i, 0);
            reservationTimeRepository.add(new ReservationTime(null, localTime));
        }

        reservationService.addReservation(new AddReservationRequest("praisebak", LocalDate.now().plusDays(1), 1L, 1L));
        reservationService.addReservation(new AddReservationRequest("praisebak", LocalDate.now().plusDays(1), 2L, 1L));
        reservationService.addReservation(new AddReservationRequest("praisebak", LocalDate.now().plusDays(1), 3L, 1L));

        reservationService.addReservation(new AddReservationRequest("praisebak", LocalDate.now().plusDays(1), 1L, 2L));
        reservationService.addReservation(new AddReservationRequest("praisebak", LocalDate.now().plusDays(1), 2L, 2L));

        reservationService.addReservation(new AddReservationRequest("praisebak", LocalDate.now().plusDays(1), 1L, 3L));

        List<Theme> top10Theme = reservationService.getRankingThemes(LocalDate.now().plusDays(6));
        assertAll(() -> {
            assertThat(top10Theme.getFirst().getId()).isEqualTo(1L);
            assertThat(top10Theme.get(1).getId()).isEqualTo(2L);
            assertThat(top10Theme.get(2).getId()).isEqualTo(3L);
        });
    }

    @Test
    void 존재하지_않는_예약을_조회시_예외가_발생한다() {
        assertThatThrownBy(() -> reservationService.getReservationById(-1L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
