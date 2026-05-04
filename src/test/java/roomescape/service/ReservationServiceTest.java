package roomescape.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import roomescape.dao.ReservationTimeDao;
import roomescape.dto.ReservationRequestDto;
import roomescape.dto.ReservationResponseDto;
import roomescape.dto.ReservationTimeRequestDto;
import roomescape.dto.ReservationTimeResponseDto;
import roomescape.exception.CustomException;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @Test
    void notExistReservationTimeExceptionTest() {
        ReservationRequestDto requestDto = new ReservationRequestDto("fizz", LocalDate.of(2026, 5, 2), 1L);
        assertThatThrownBy(() -> reservationService.create(requestDto))
                .hasMessage("[ERROR] 해당 ID의 예약 시간을 찾을 수 없습니다.")
                .isInstanceOf(CustomException.class);
    }

    ReservationTimeResponseDto createReservationTime() {
        reservationTimeDao.create(new ReservationTimeRequestDto(LocalTime.of(10, 0)));
        return new ReservationTimeResponseDto(1L, LocalTime.of(10, 0));
    }

    @Test
    void createTest() {
        ReservationTimeResponseDto reservationTimeResponseDto = createReservationTime();

        ReservationResponseDto responseDto = reservationService.create(
                new ReservationRequestDto("fizz", LocalDate.of(2026, 5, 2), 1L));

        assertThat(responseDto).isEqualTo(
                new ReservationResponseDto(1L, "fizz", LocalDate.of(2026, 5, 2), reservationTimeResponseDto));
    }

    @Test
    void readAllTest() {
        ReservationTimeResponseDto reservationTimeResponseDto = createReservationTime();
        reservationService.create(new ReservationRequestDto("fizz", LocalDate.of(2026, 5, 2), 1L));
        reservationService.create(new ReservationRequestDto("fizz2", LocalDate.of(2026, 5, 2), 1L));

        List<ReservationResponseDto> responseDtos = reservationService.readAll();

        assertThat(responseDtos.getFirst()).isEqualTo(
                new ReservationResponseDto(1L, "fizz", LocalDate.of(2026, 5, 2), reservationTimeResponseDto));
        assertThat(responseDtos.get(1)).isEqualTo(
                new ReservationResponseDto(2L, "fizz2", LocalDate.of(2026, 5, 2), reservationTimeResponseDto));
    }

    @Test
    void deleteTest() {
        createReservationTime();
        reservationService.create(new ReservationRequestDto("fizz", LocalDate.of(2026, 5, 2), 1L));
        reservationService.delete(1L);

        List<ReservationResponseDto> responseDtos = reservationService.readAll();

        assertThat(responseDtos.size()).isEqualTo(0);
    }
}
