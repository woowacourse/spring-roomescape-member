package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationTimeRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeServiceTest {

    @Autowired
    ReservationTimeService reservationTimeService;
    @Autowired
    ReservationService reservationService;

    @Test
    @DisplayName("예약 시간을 저장할 수 있다.")
    void save() {
        final ReservationTime reservationTime = reservationTimeService.save(new ReservationTimeRequest(LocalTime.now()));

        assertThat(reservationTime).isNotNull();
    }

    @Test
    @DisplayName("전체 예약 시간을 조회할 수 있다.")
    void findAll() {
        reservationTimeService.save(new ReservationTimeRequest(LocalTime.now()));

        final List<ReservationTime> reservationTimes = reservationTimeService.findAll();

        assertThat(reservationTimes).hasSize(1);
    }

    @Test
    @DisplayName("예약 시간을 삭제할 수 있다.")
    void delete() {
        reservationTimeService.save(new ReservationTimeRequest(LocalTime.now()));

        reservationTimeService.delete(1L);
        final List<ReservationTime> reservationTimes = reservationTimeService.findAll();

        assertThat(reservationTimes).hasSize(0);
    }

    @Test
    @DisplayName("이미 예약된 예약 시간을 삭제하려 하면 예외가 발생한다.")
    void invalidDelete() {
        LocalTime localTime = LocalTime.now();

        ReservationTime savedReservationTime = reservationTimeService.save(new ReservationTimeRequest(localTime.plusHours(1)));
        reservationService.save(new ReservationRequest("abc", LocalDate.now(), savedReservationTime.getId()));

        assertThatThrownBy(() -> reservationTimeService.delete(savedReservationTime.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이미 예약된 예약 시간을 중복하여 저장하려 하면 예외가 발생한다.")
    void invalidSave() {
        LocalTime localTime = LocalTime.of(15, 40);
        reservationTimeService.save(new ReservationTimeRequest(localTime));

        assertThatThrownBy(() -> reservationTimeService.save(new ReservationTimeRequest(localTime)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
