package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.UserReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.error.NotFoundException;
import roomescape.error.ReservationException;
import roomescape.stub.StubReservationRepository;
import roomescape.stub.StubReservationTimeRepository;
import roomescape.stub.StubThemeRepository;

class ReservationServiceTest {

    private final ReservationTime time1 = new ReservationTime(1L, LocalTime.of(14, 0));
    private final ReservationTime time2 = new ReservationTime(2L, LocalTime.of(13, 0));

    private final Theme theme1 = new Theme(1L, "테마1", "설명1", "썸네일1");
    private final Theme theme2 = new Theme(2L, "테마2", "설명2", "썸네일2");

    private final Reservation reservation1 = new Reservation(1L, "테스트", LocalDate.of(2025, 5, 11), time1, theme1);
    private final Reservation reservation2 = new Reservation(2L, "테스트2", LocalDate.of(2025, 6, 11), time2, theme2);

    private StubReservationRepository stubReservationRepository;
    private ReservationService sut;

    @BeforeEach
    void setUp() {
        stubReservationRepository = new StubReservationRepository(reservation1, reservation2);
        StubReservationTimeRepository stubReservationTimeRepository = new StubReservationTimeRepository(time1, time2);
        StubThemeRepository stubThemeRepository = new StubThemeRepository(theme1, theme2);

        sut = new ReservationService(stubReservationRepository, stubReservationTimeRepository, stubThemeRepository);
    }

    @Test
    void 모든_예약을_조회한다() {
        // when
        List<ReservationResponse> responses = sut.findAllReservation();

        // then
        assertThat(responses).hasSize(2);
    }

    @Test
    void 중복된_날짜와_시간이면_예외가_발생한다() {
        // given: r1과 동일한 date/time 요청
        UserReservationRequest dupReq = new UserReservationRequest(
                reservation1.getName(), reservation1.getDate(), time1.getId(), theme1.getId()
        );

        // when
        // then
        assertThatThrownBy(() -> sut.saveReservation(dupReq))
                .isInstanceOf(ReservationException.class)
                .hasMessage("해당 시간은 이미 예약되어있습니다.");
    }

    @Test
    void 지나간_날짜와_시간이면_예외가_발생한다() {
        // given: r1과 동일한 date/time 요청
        UserReservationRequest afterReq = new UserReservationRequest(
                reservation1.getName(), LocalDate.now().minusYears(1), time1.getId(), theme1.getId()
        );

        // when
        // then
        assertThatThrownBy(() -> sut.saveReservation(afterReq))
                .isInstanceOf(ReservationException.class)
                .hasMessage("예약은 현재 시간 이후로 가능합니다.");
    }

    @Test
    void 새로운_예약은_정상_생성된다() {
        // given
        UserReservationRequest req = new UserReservationRequest(
                "철원", LocalDate.of(2999, 4, 21), time1.getId(), theme1.getId()
        );

        // when
        ReservationResponse result = sut.saveReservation(req);

        // then
        assertSoftly(soft -> {
            soft.assertThat(result.id()).isNotNull();
            soft.assertThat(result.name()).isEqualTo("철원");
            soft.assertThat(result.date()).isEqualTo(LocalDate.of(2999, 4, 21));
            soft.assertThat(result.time().id()).isEqualTo(1L);
            soft.assertThat(result.theme().id()).isEqualTo(1L);
        });
    }

    @Test
    void 예약을_삭제한다() {
        // when
        sut.deleteReservation(reservation1.getId());

        // then
        assertThat(stubReservationRepository.findAll()).hasSize(1);
    }

    @Test
    void 존재하지_않는_예약을_삭제하면_예외가_발생한다() {
        // when
        // then
        assertThatThrownBy(() -> sut.deleteReservation(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 예약입니다.");
    }
}
