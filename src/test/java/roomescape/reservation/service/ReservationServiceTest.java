package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.error.ReservationException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.stub.StubReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.stub.StubReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.stub.StubThemeRepository;

class ReservationServiceTest {

    private final ReservationTime time1 = new ReservationTime(1L, LocalTime.of(14, 0));
    private final ReservationTime time2 = new ReservationTime(2L, LocalTime.of(13, 0));

    private final Theme theme1 = new Theme(1L, "테마1", "설명1", "썸네일1");
    private final Theme theme2 = new Theme(1L, "테마2", "설명2", "썸네일2");

    private final Reservation r1 = new Reservation(1L, "테스트", LocalDate.of(2025, 5, 11), time1, theme1);
    private final Reservation r2 = new Reservation(2L, "테스트2", LocalDate.of(2025, 6, 11), time2, theme2);

    private StubReservationRepository repo;
    private StubReservationTimeRepository timeRepo;
    private StubThemeRepository themeRepo;
    private ReservationService service;

    @BeforeEach
    void setUp() {
        repo = new StubReservationRepository(r1, r2);
        timeRepo = new StubReservationTimeRepository(time1, time2);
        themeRepo = new StubThemeRepository(theme1, theme2);
        service = new ReservationService(repo, timeRepo, themeRepo);
    }

    @Test
    void 모든_예약을_조회한다() {
        // when
        List<ReservationResponse> responses = service.getReservations();

        // then
        assertThat(responses)
                .hasSize(2)
                .containsExactly(
                        new ReservationResponse(r1),
                        new ReservationResponse(r2)
                );
    }

    @Test
    void 중복된_날짜와_시간이면_예외가_발생한다() {
        // given: r1과 동일한 date/time 요청
        ReservationRequest dupReq = new ReservationRequest(
                r1.getName(), r1.getDate(), time1.getId(), theme1.getId()
        );

        // when
        // then
        assertThatThrownBy(() -> service.saveReservation(dupReq))
                .isInstanceOf(ReservationException.class)
                .hasMessage("해당 시간은 이미 예약되어있습니다.");
    }

    @Test
    void 지나간_날짜와_시간이면_예외가_발생한다() {
        // given: r1과 동일한 date/time 요청
        ReservationRequest dupReq = new ReservationRequest(
                r1.getName(), LocalDate.of(2000, 10, 8), time1.getId(), theme1.getId()
        );

        // when
        // then
        assertThatThrownBy(() -> service.saveReservation(dupReq))
                .isInstanceOf(ReservationException.class)
                .hasMessage("예약은 현재 시간 이후로 가능합니다.");
    }

    @Test
    void 새로운_예약은_정상_생성된다() {
        // given
        repo = new StubReservationRepository();
        timeRepo = new StubReservationTimeRepository(time1);
        service = new ReservationService(repo, timeRepo, themeRepo);

        ReservationRequest req = new ReservationRequest(
                "철원", LocalDate.of(2999, 4, 21), time1.getId(), theme1.getId()
        );

        // when
        ReservationResponse result = service.saveReservation(req);
        System.out.println(result);

        // then
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(result.id()).isEqualTo(1L);
            soft.assertThat(result.name()).isEqualTo("철원");
            soft.assertThat(result.date()).isEqualTo(LocalDate.of(2999, 4, 21));
            soft.assertThat(result.time())
                    .satisfies(rt -> {
                        assertThat(rt.id()).isEqualTo(time1.getId());
                        assertThat(rt.startAt()).isEqualTo(time1.getStartAt());
                    });
        });
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(repo.findAll()).hasSize(1);
            soft.assertThat(repo.findAll().get(0).getTime().getStartAt())
                    .isEqualTo(time1.getStartAt());
        });
    }

    @Test
    void 예약을_삭제한다() {
        // when
        service.deleteReservation(r1.getId());

        // then
        assertThat(repo.findAll())
                .hasSize(1)
                .extracting(Reservation::getId)
                .doesNotContain(r1.getId());
    }

    @Test
    void 존재하지_않는_예약을_삭제하면_예외가_발생한다() {
        // when
        // then
        assertThatThrownBy(() -> service.deleteReservation(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 예약입니다.");
    }
}
