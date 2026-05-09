package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeRequestDTO;
import roomescape.dto.ReservationTimeResponseDTO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeServiceTest {

    @Autowired
    ReservationTimeService reservationTimeService;

    @DisplayName("예약 시간을 생성한다")
    @Test
    void ReservationTimeRequestDTO를_받아_ReservationTimeResponseDTO를_리턴한다() {
        ReservationTimeRequestDTO reservationTimeRequestDTO = new ReservationTimeRequestDTO("10:00");

        ReservationTimeResponseDTO addedTime =
                reservationTimeService.addReservationTime(reservationTimeRequestDTO);

        assertThat(addedTime)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(reservationTimeRequestDTO);
    }

    @DisplayName("모든 예약 시간을 조회한다")
    @Test
    void 존재하는_모든_예약_시간의_ReservationTimeResponseDto가_담긴_리스트를_리턴한다() {
        // given
        ReservationTimeResponseDTO addedTime10 = reservationTimeService.addReservationTime(
                new ReservationTimeRequestDTO("10:00")
        );
        ReservationTimeResponseDTO addedTime11 = reservationTimeService.addReservationTime(
                new ReservationTimeRequestDTO("11:00")
        );

        // when
        List<ReservationTimeResponseDTO> allReservationTimes = reservationTimeService.findAllReservationTime();

        // then
        assertThat(allReservationTimes)
                .hasSize(2)
                .containsExactlyInAnyOrder(addedTime10, addedTime11);
    }

    @DisplayName("예약된 시간을 조회한다")
    @Sql("/data.sql")
    @Test
    void 특정_테마의_특정_날짜의_예약된_시간을_조회한다() {
        List<ReservationTime> reservedTimes = reservationTimeService.findReservedTimes(
                LocalDate.now(),
                11L
        );

        assertThat(reservedTimes).hasSize(1);
    }

    @DisplayName("예약 시간을 삭제한다")
    @Test
    void 예약_시간의_id로_예약_시간을_삭제한다() {
        ReservationTimeResponseDTO addedTime = reservationTimeService.addReservationTime(
                new ReservationTimeRequestDTO("10:00")
        );

        reservationTimeService.deleteReservationTime(addedTime.id());

        assertThat(reservationTimeService.findAllReservationTime()).isEmpty();
    }
}