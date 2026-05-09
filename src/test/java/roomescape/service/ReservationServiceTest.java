package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.common.exception.NotFoundException;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.service.dto.request.ReservationCreateRequest;
import roomescape.service.dto.response.ReservationOptionResponse;
import roomescape.service.dto.response.ReservationResponse;
import roomescape.service.support.FakeReservationRepository;
import roomescape.service.support.FakeReservationTimeRepository;
import roomescape.service.support.FakeThemeRepository;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationServiceTest {

    private static final Clock FIXED_CLOCK = Clock.fixed(
            LocalDate.of(2026, 5, 8)
                    .atStartOfDay(ZoneId.of("Asia/Seoul"))
                    .toInstant(),
            ZoneId.of("Asia/Seoul")
    );

    private FakeReservationRepository reservationRepository;
    private FakeReservationTimeRepository reservationTimeRepository;
    private FakeThemeRepository themeRepository;
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        reservationRepository = new FakeReservationRepository();
        reservationTimeRepository = new FakeReservationTimeRepository();
        themeRepository = new FakeThemeRepository();
        reservationService = new ReservationService(
                reservationRepository,
                reservationTimeRepository,
                themeRepository,
                FIXED_CLOCK
        );
    }

    @Test
    void 예약을_생성한다() {
        // given
        reservationTimeRepository.add(ReservationTime.of(1L, LocalTime.of(10, 0), LocalTime.of(10, 30)));
        themeRepository.add(Theme.of(1L, "링", "공포 테마", "http:~"));

        // when
        ReservationResponse response = reservationService.create(
                new ReservationCreateRequest("브라운", LocalDate.of(2026, 8, 5), 1L, 1L)
        );

        // then
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("브라운");
        assertThat(reservationRepository.savedReservation().getCustomerName()).isEqualTo("브라운");
    }

    @Test
    void 존재하지_않는_예약_시간으로_예약하면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> reservationService.create(
                new ReservationCreateRequest("브라운", LocalDate.of(2026, 8, 5), 1L, 1L)
        ))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 예약 시간입니다.");
    }

    @Test
    void 존재하지_않는_테마로_예약하면_예외가_발생한다() {
        // given
        reservationTimeRepository.add(ReservationTime.of(1L, LocalTime.of(10, 0), LocalTime.of(10, 30)));

        // when & then
        assertThatThrownBy(() -> reservationService.create(
                new ReservationCreateRequest("브라운", LocalDate.of(2026, 8, 5), 1L, 1L)
        ))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 테마입니다.");
    }

    @Test
    void 예약_가능_날짜와_테마를_조회한다() {
        // given
        themeRepository.add(Theme.of(1L, "링", "공포 테마", "http:~"));

        // when
        ReservationOptionResponse response = reservationService.getReservationOptions();

        // then
        assertThat(response.dates()).hasSize(14);
        assertThat(response.dates().getFirst()).isEqualTo(LocalDate.of(2026, 5, 8));
        assertThat(response.dates().getLast()).isEqualTo(LocalDate.of(2026, 5, 21));
        assertThat(response.themes())
                .extracting(theme -> theme.name())
                .containsExactly("링");
    }

    @Test
    void 존재하지_않는_예약을_삭제하면_예외가_발생한다() {
        // given
        reservationRepository.failToDelete();

        // when & then
        assertThatThrownBy(() -> reservationService.delete(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 예약입니다.");
    }

}
