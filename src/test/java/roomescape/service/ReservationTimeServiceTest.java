package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dao.ReservationDao;
import roomescape.dto.ReservationTimeRequestDto;
import roomescape.dto.ReservationTimeResponseDto;
import roomescape.exception.InvalidReservationException;

@Sql(scripts = {"/test-schema.sql"})
@SpringBootTest
class ReservationTimeServiceTest {

    @Autowired
    private ReservationTimeService reservationTimeService;

    @MockitoBean
    private ReservationDao reservationDao;

    @DisplayName("예약 객체가 주어졌을 때, ID를 설정할 수 있어야 한다.")
    @Test
    void given_reservation_then_set_id() {
        ReservationTimeRequestDto reservationTimeRequestDto = new ReservationTimeRequestDto(
            "10:00");
        ReservationTimeResponseDto reservationTimeResponseDto = reservationTimeService.saveReservationTime(
            reservationTimeRequestDto);
        assertThat(reservationTimeResponseDto.id()).isNotNull();
    }

    @DisplayName("다른 예약에서 예약 시간을 이미 사용하고 있다면, 삭제할 수 없다.")
    @Test
    void delete_invalid_time_id_then_throw_exception() {
        when(reservationDao.findByTimeId(1L)).thenReturn(1);
        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(1L))
            .isInstanceOf(InvalidReservationException.class);
    }

    @DisplayName("다른 예약에서 예약 시간을 사용하고 있지 않다면, 삭제할 수 있어야 한다..")
    @Test
    void delete_valid_time_id_then_throw_exception() {
        when(reservationDao.findByTimeId(1L)).thenReturn(0);
        assertThatCode(() -> reservationTimeService.deleteReservationTime(1L))
            .doesNotThrowAnyException();
    }
}
