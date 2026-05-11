package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.NotFoundException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationStatus;
import roomescape.theme.domain.Theme;
import roomescape.theme.service.ThemeService;
import roomescape.time.domain.ReservationTime;
import roomescape.time.service.ReservationTimeService;

@SpringBootTest
@Transactional
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Autowired
    private ThemeService themeService;

    private final String name = "한다";
    private final LocalDate date1 = LocalDate.now().plusWeeks(1);
    private final LocalDate date2 = LocalDate.now().plusWeeks(2);
    private ReservationTime reservationTime1;
    private ReservationTime reservationTime2;
    private Theme theme1;
    private Theme theme2;

    @BeforeEach
    void setup() {
        reservationTime1 = reservationTimeService.create(LocalTime.of(15, 40));
        reservationTime2 = reservationTimeService.create(LocalTime.of(16, 0));
        theme1 = themeService.register("테마1", "설명1", "썸네일1");
        theme2 = themeService.register("테마2", "설명2", "썸네일2");
    }

    @Test
    @DisplayName("전체 예약 정보를 가져온다.")
    void readAll() {
        // given
        reservationService.create("한다", date1, reservationTime1.id(), theme1.id());
        reservationService.create("송송", date2, reservationTime1.id(), theme2.id());

        // when
        List<Reservation> actual = reservationService.readAll();

        // then
        assertThat(actual).hasSize(2);
    }

    @Test
    @DisplayName("나의 예약들을 조회하면 날짜/시간 오름차순으로 정렬해 모두 조회한다.")
    void readAllByName() {
        // given
        reservationService.create(name, date1, reservationTime1.id(), theme1.id());
        reservationService.create(name, date1, reservationTime2.id(), theme1.id());
        reservationService.create(name, date2, reservationTime1.id(), theme1.id());
        reservationService.create(name, date2, reservationTime2.id(), theme1.id());

        // when
        List<Reservation> actual = reservationService.readAllByName(name);

        // then
        assertThat(actual).hasSize(4);
        assertThat(actual).isSortedAccordingTo(
                Comparator.comparing(Reservation::date).thenComparing(Reservation::time));
    }

    @Test
    @DisplayName("예약을 추가한다.")
    void create() {
        // given & when
        reservationService.create(name, date1, reservationTime1.id(), theme1.id());

        // then
        assertThat(reservationService.readAll()).hasSize(1);
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간이면 예외를 발생한다.")
    void create_does_not_exist_reservation_time() {
        // given
        Long wrongTimeId = Long.MIN_VALUE;

        // when & then
        assertThatThrownBy(() -> reservationService.create(name, date1, wrongTimeId, theme1.id()))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 예약 시간입니다.");
    }

    @Test
    @DisplayName("예약 생성 시 예약 날짜/시간/테마가 중복되면 예외를 발생한다.")
    void create_duplicate_reservation() {
        // given
        reservationService.create("브라운", date1, reservationTime1.id(), theme1.id());

        // when & then
        assertThatThrownBy(() -> reservationService.create("한다", date1, reservationTime1.id(), theme1.id()))
                .isInstanceOf(ConflictException.class)
                .hasMessage("해당 날짜/시간/테마는 이미 예약되었습니다.");
    }

    @Test
    @DisplayName("예약을 취소하면 CANCELED 상태가 된다.")
    void updateStatus_canceled() {
        // given
        Reservation savedReservation = reservationService.create(name, date1, reservationTime1.id(), theme1.id());

        // when
        Reservation actual = reservationService.cancel(savedReservation.id());

        // then
        assertThat(actual.status()).isEqualTo(ReservationStatus.CANCELED);
    }
}
