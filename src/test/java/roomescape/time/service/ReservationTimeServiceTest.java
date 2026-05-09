package roomescape.time.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.NotFoundException;
import roomescape.time.dto.request.ReservationTimeSaveDto;
import roomescape.time.dto.response.ReservationTimeDetailDto;
import roomescape.time.repository.JdbcReservationTimeRepository;

@JdbcTest
class ReservationTimeServiceTest {
    private ReservationTimeService reservationTimeService;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        JdbcReservationTimeRepository reservationTimeRepository = new JdbcReservationTimeRepository(jdbcTemplate);
        this.reservationTimeService = new ReservationTimeService(reservationTimeRepository);

        reservationTimeService.create(new ReservationTimeSaveDto(LocalTime.of(15, 40)));
        reservationTimeService.create(new ReservationTimeSaveDto(LocalTime.of(16, 0)));
    }

    @Test
    @DisplayName("모든 예약 시간 정보를 조회한다.")
    void findAll() {
        //given & when
        List<ReservationTimeDetailDto> result = reservationTimeService.findAll();

        //then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("예약 시간을 추가한다.")
    void create() {
        //given & when
        reservationTimeService.create(new ReservationTimeSaveDto(LocalTime.of(12, 0)));

        //then
        assertThat(reservationTimeService.findAll().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("이미 존재하는 예약 시간 생성 시 예외가 발생한다.")
    void create_already_exist() {
        assertThatThrownBy(() -> reservationTimeService.create(new ReservationTimeSaveDto(LocalTime.of(15, 40))))
                .isInstanceOf(ConflictException.class)
                .hasMessage("이미 존재하는 예약 시간입니다.");
    }

    @Test
    @DisplayName("예약 시간을 삭제한다.")
    void delete() {
        //given
        ReservationTimeDetailDto response = reservationTimeService.create(
                new ReservationTimeSaveDto(LocalTime.of(12, 0)));
        Long id = response.id();

        //when
        reservationTimeService.delete(id);

        //then
        assertThat(reservationTimeService.findAll().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간 삭제 시 예외가 발생한다.")
    void deleteNotExist() {
        assertThatThrownBy(() -> reservationTimeService.delete(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 예약 시간입니다.");
    }
}
