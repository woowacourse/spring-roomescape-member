package roomescape.reservationtime.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import roomescape.error.ReservationException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.stub.StubReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.dto.ReservationTimeRequest;
import roomescape.reservationtime.dto.ReservationTimeResponse;
import roomescape.reservationtime.stub.StubReservationTimeRepository;
import roomescape.theme.domain.Theme;

class ReservationTimeServiceTest {

    private final LocalTime t1 = LocalTime.of(10, 0);
    private final LocalTime t2 = LocalTime.of(11, 0);

    private final ReservationTime rt1 = new ReservationTime(1L, t1);
    private final ReservationTime rt2 = new ReservationTime(2L, t2);

    private final Theme theme1 = new Theme(1L, "테마1", "설명1", "썸네일1");
    private final Theme theme2 = new Theme(1L, "테마2", "설명2", "썸네일2");

    private final Reservation r1 = new Reservation(1L, "테스트", LocalDate.of(2025, 5, 11), rt1, theme1);
    private final Reservation r2 = new Reservation(2L, "테스트2", LocalDate.of(2025, 6, 11), rt2, theme2);

    private final StubReservationRepository stubReservationRepo = new StubReservationRepository(r1, r2);
    private final StubReservationTimeRepository stubReservationTimeRepo = new StubReservationTimeRepository(rt1, rt2);

    private final ReservationTimeService service = new ReservationTimeService(stubReservationTimeRepo,
            stubReservationRepo);


    @Test
    void 예약_시간이_저장된다() {
        // given
        ReservationTimeRequest request = new ReservationTimeRequest(LocalTime.of(12, 30));

        // when
        ReservationTimeResponse response = service.saveTime(request);

        // then
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(response.startAt()).isEqualTo(request.startAt());
            soft.assertThat(stubReservationTimeRepo.findAll())
                    .extracting(ReservationTime::getStartAt)
                    .contains(request.startAt());
        });
    }

    @Test
    void 모든_예약_시간을_조회한다() {
        // when
        List<ReservationTimeResponse> all = service.findAll();

        // then
        assertThat(all)
                .hasSize(2)
                .extracting(ReservationTimeResponse::startAt)
                .containsExactlyInAnyOrder(t1, t2);
    }

    @Test
    void 예약_시간이_삭제된다() {
        // given
        assertThat(service.findAll()).hasSize(2);

        // when
        service.delete(1L);

        // then
        List<ReservationTimeResponse> afterDelete = service.findAll();
        assertThat(afterDelete)
                .hasSize(1)
                .extracting(ReservationTimeResponse::id)
                .doesNotContain(1L);
    }

    @Test
    void 존재하지_않는_아이디면_빈_옵셔널을_반환한다() {
        // given
        Optional<ReservationTime> found = stubReservationTimeRepo.findById(999L);

        // when
        // then
        assertThat(found).isEmpty();
    }

    @Test
    void 예약이_존재하는_시간을_삭제하지_못_한다() {
        // given
        stubReservationRepo.setExistsByReservationTimeId(true);

        // when
        // then
        assertThatThrownBy(() -> service.delete(1L))
                .isInstanceOf(ReservationException.class)
                .hasMessage("해당 시간으로 예약된 건이 존재합니다.");
        stubReservationRepo.setExistsByReservationTimeId(false);
    }
}
