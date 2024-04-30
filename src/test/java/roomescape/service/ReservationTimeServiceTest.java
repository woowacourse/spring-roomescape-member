package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.dao.ReservationDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;
import roomescape.exception.ExistReservationInReservationTimeException;
import roomescape.exception.NotExistReservationTimeException;
import roomescape.service.dto.ReservationTimeInput;
import roomescape.service.dto.ReservationTimeOutput;

@SpringBootTest
public class ReservationTimeServiceTest {

    @Autowired
    ReservationDao reservationDao;

    @Autowired
    ReservationTimeService reservationTimeService;

    @Test
    @DisplayName("유효한 값을 입력하면 예외를 발생하지 않는다.")
    void create_reservationTime() {
        ReservationTimeInput input = new ReservationTimeInput("10:00");
        assertThatCode(() -> reservationTimeService.createReservationTime(input))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("유효하지 않은 값을 입력하면 예외를 발생한다.")
    void throw_exception_when_input_is_invalid() {
        ReservationTimeInput input = new ReservationTimeInput("");
        assertThatThrownBy(() -> reservationTimeService.createReservationTime(input))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 시간 ID 를 삭제하려 하면 에외를 발생한다.")
    void throw_exception_when_not_exist_id() {
        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(-1))
                .isInstanceOf(NotExistReservationTimeException.class);
    }

    @Test
    @DisplayName("특정 시간에 대한 예약이 존재하면 그 시간을 삭제하려 할 때 예외를 발생한다.")
    void throw_exception_when_delete_id_that_exist_reservation() {
        ReservationTimeOutput output = reservationTimeService.createReservationTime(
                new ReservationTimeInput("10:00"));
        reservationDao.create(new Reservation(null, "제리", ReservationDate.from("2024-04-30"),
                ReservationTime.from(output.id(), output.startAt())));

        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(output.id()))
                .isInstanceOf(ExistReservationInReservationTimeException.class);
    }
}
