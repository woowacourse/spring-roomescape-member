package roomescape.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.AddReservationTimeRequest;
import roomescape.exception.InvalidReservationTimeException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.ReservationTimeService;
import roomescape.unit.repository.FakeReservationRepository;
import roomescape.unit.repository.FakeReservationTimeRepository;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReservationTimeServiceTest {

    private ReservationTimeService reservationTimeService;

    private ReservationRepository reservationRepository;

    @BeforeEach
    void setup() {
        reservationRepository = new FakeReservationRepository();
        ReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository();

        reservationTimeService = new ReservationTimeService(reservationRepository, reservationTimeRepository);
    }

    @Test
    void 예약시간을_추가하고_조회할_수_있다() {
        reservationTimeService.addReservationTime(new AddReservationTimeRequest(LocalTime.now().plusMinutes(30L)));
        assertThat(reservationTimeService.allReservationTimes()).hasSize(1);
    }

    @Test
    void 예약시간을_삭제하고_조회할_수_있다() {
        // given
        ReservationTime reservationTime = reservationTimeService.addReservationTime(
                new AddReservationTimeRequest(LocalTime.now().plusMinutes(30L)));

        // when
        int before = reservationTimeService.allReservationTimes().size();
        reservationTimeService.deleteReservationTime(reservationTime.getId());
        int after = reservationTimeService.allReservationTimes().size();

        //then
        assertAll(() -> {
            assertThat(before).isEqualTo(1);
            assertThat(after).isEqualTo(0);
        });
    }

    @Test
    void 특정_시간에_대한_예약이_존재할때_시간을_삭제하려고하면_예외가_발생한다() {
        // given
        LocalTime startAt = LocalTime.now().plusMinutes(30L);
        AddReservationTimeRequest requset = new AddReservationTimeRequest(startAt);
        ReservationTime reservationTime = reservationTimeService.addReservationTime(requset);

        Theme theme = new Theme(0L, "공포", "공포테마입니다.", "thumbnail");

        Reservation reservation = new Reservation(null, "praisebak",
                LocalDate.now().plusDays(1), reservationTime, theme);
        reservationRepository.add(reservation);

        // when & then
        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(reservationTime.getId()))
                .isInstanceOf(InvalidReservationTimeException.class);
    }

    @Test
    void 중복_시간을_설정할_수_없다() {
        // given
        LocalTime startAt = LocalTime.now().plusMinutes(30L);
        AddReservationTimeRequest initialReservationTime = new AddReservationTimeRequest(startAt);
        reservationTimeService.addReservationTime(initialReservationTime);

        // when
        AddReservationTimeRequest duplicateAddReservationTime = new AddReservationTimeRequest(startAt);

        //then
        assertThatThrownBy(() -> reservationTimeService.addReservationTime(duplicateAddReservationTime))
                .isInstanceOf(InvalidReservationTimeException.class);
    }

    @Test
    void 존재하지_않는_시간을_조회시_예외가_발생한다() {
        assertThatThrownBy(() -> reservationTimeService.getReservationTimeById(-1L))
                .isInstanceOf(InvalidReservationTimeException.class);
    }
}
