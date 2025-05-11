package roomescape.service.reservationtime;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dao.reservationtime.FakeReservationTimeDaoImpl;
import roomescape.dto.reservationtime.request.ReservationTimeRequestDto;

public class ReservationTimeServiceTest {

    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void init() {
        reservationTimeService = new ReservationTimeService(new FakeReservationTimeDaoImpl());
    }

    @DisplayName("reservationTimeRequestDto가 들어왔을 때, Fake 객체에 저장되어야 한다.")
    @Test
    void given_reservation_time_request_dto_then_save_db() {
        //given
        ReservationTimeRequestDto reservationTimeRequestDto = new ReservationTimeRequestDto(
                "10:00");

        //when
        reservationTimeService.saveReservationTime(reservationTimeRequestDto);
        //then
        assertThat(reservationTimeService.getAllReservationTimes().size()).isEqualTo(1);
    }

    @DisplayName("여러 번 ReservationTime을 저장할 때, 성공적으로 Fake객체 및 캐싱 DB에 저장되고, 읽어올 수 있어야 한다.")
    @Test
    void given_multiple_reservation_time_request_dto_then_all_save() {
        //given
        ReservationTimeRequestDto reservationTimeRequestDto1 = new ReservationTimeRequestDto(
                "10:00");
        ReservationTimeRequestDto reservationTimeRequestDto2 = new ReservationTimeRequestDto(
                "11:00");
        ReservationTimeRequestDto reservationTimeRequestDto3 = new ReservationTimeRequestDto(
                "12:00");

        //when
        reservationTimeService.saveReservationTime(reservationTimeRequestDto1);
        reservationTimeService.saveReservationTime(reservationTimeRequestDto2);
        reservationTimeService.saveReservationTime(reservationTimeRequestDto3);

        //then
        assertThat(reservationTimeService.getAllReservationTimes().size()).isEqualTo(3);
    }

    @DisplayName("reservationTimeId가 주어졌을 떄, Fake 객체 및 캐싱DB에서 삭제되어야 한다.")
    @Test
    void given_reservation_time_id_then_remove_db() {
        //given
        ReservationTimeRequestDto reservationTimeRequestDto = new ReservationTimeRequestDto(
                "10:00");
        reservationTimeService.saveReservationTime(reservationTimeRequestDto);

        //when
        reservationTimeService.deleteReservationTime(1L);

        //then
        assertThat(reservationTimeService.getAllReservationTimes().size()).isEqualTo(0);
    }
}
