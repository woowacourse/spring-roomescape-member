package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.error.ReservationException;
import roomescape.stub.StubReservationRepository;
import roomescape.stub.StubReservationTimeRepository;

class ReservationTimeServiceTest {

    private final ReservationTime reservationTime1 = new ReservationTime(1L, LocalTime.of(10, 0));
    private final ReservationTime reservationTIme2 = new ReservationTime(2L, LocalTime.of(11, 0));

    private final Theme theme1 = new Theme(1L, "테마1", "설명1", "썸네일1");
    private final Theme theme2 = new Theme(2L, "테마2", "설명2", "썸네일2");

    private final Reservation reservation1 = new Reservation(1L, "테스트", LocalDate.of(2025, 5, 11), reservationTime1, theme1);
    private final Reservation reservation2 = new Reservation(2L, "테스트2", LocalDate.of(2025, 6, 11), reservationTIme2, theme2);

    private StubReservationRepository stubReservationRepository;

    private ReservationTimeService sut;

    @BeforeEach
    void setUp() {
        stubReservationRepository = new StubReservationRepository(reservation1, reservation2);
        StubReservationTimeRepository stubReservationTimeRepository = new StubReservationTimeRepository(reservationTime1,
                reservationTIme2);

        sut = new ReservationTimeService(stubReservationTimeRepository, stubReservationRepository);
    }

    @Test
    void 예약_시간이_저장된다() {
        // given
        ReservationTimeRequest request = new ReservationTimeRequest(LocalTime.of(12, 30));

        // when
        ReservationTimeResponse response = sut.saveTime(request);

        // then
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(response.id()).isNotNull();
            soft.assertThat(response.startAt()).isEqualTo(LocalTime.of(12, 30));
        });
    }

    @Test
    void 모든_예약_시간을_조회한다() {
        // when
        List<ReservationTimeResponse> all = sut.findAll();

        // then
        assertThat(all).hasSize(2);
    }

    @Test
    void 예약_시간이_삭제된다() {
        // when
        sut.delete(reservationTime1.getId());

        // then
        List<ReservationTimeResponse> afterDelete = sut.findAll();
        assertThat(afterDelete).hasSize(1);
    }

    @Test
    void 예약이_존재하는_시간을_삭제하지_못_한다() {
        // given
        stubReservationRepository.setExistsByReservationTimeId(true);

        // when
        // then
        assertThatThrownBy(() -> sut.delete(1L))
                .isInstanceOf(ReservationException.class)
                .hasMessage("해당 시간으로 예약된 건이 존재합니다.");
    }
}
