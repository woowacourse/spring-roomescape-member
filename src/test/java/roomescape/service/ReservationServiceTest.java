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
import roomescape.dao.ThemeDao;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationRequestDto;
import roomescape.dto.ReservationResponseDto;
import roomescape.dto.ReservationTimeResponseDto;
import roomescape.dto.ThemeResponseDto;
import roomescape.exception.CustomException;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @Autowired
    private ThemeDao themeDao;

    @Test
    void notExistReservationTimeExceptionTest() {
        themeDao.create(new Theme("피즈의 모험", "모험 이야기", "url.jpg"));
        ReservationRequestDto requestDto = new ReservationRequestDto("fizz", LocalDate.of(2026, 5, 2), 1L, 1L);
        assertThatThrownBy(() -> reservationService.create(requestDto))
                .hasMessage("[ERROR] 해당 ID의 예약 시간을 찾을 수 없습니다.")
                .isInstanceOf(CustomException.class);
    }

    @Test
    void notExistThemeExceptionTest() {
        reservationTimeDao.create(new ReservationTime(LocalTime.of(10, 0)));
        ReservationRequestDto requestDto = new ReservationRequestDto("fizz", LocalDate.of(2026, 5, 2), 1L, 1L);
        assertThatThrownBy(() -> reservationService.create(requestDto))
                .hasMessage("[ERROR] 해당 ID의 테마를 찾을 수 없습니다.")
                .isInstanceOf(CustomException.class);
    }

    @Test
    void createTest() {
        ReservationTime reservationTime = reservationTimeDao.create(new ReservationTime(LocalTime.of(10, 0)));
        ReservationTimeResponseDto reservationTimeResponseDto = ReservationTimeResponseDto.from(reservationTime);
        Theme theme = themeDao.create(new Theme("피즈의 모험", "모험 이야기", "url.jpg"));
        ThemeResponseDto themeResponseDto = ThemeResponseDto.from(theme);

        ReservationResponseDto responseDto = reservationService.create(
                new ReservationRequestDto("fizz", LocalDate.of(2026, 5, 2), 1L, 1L));

        assertThat(responseDto).isEqualTo(
                new ReservationResponseDto(1L, "fizz", LocalDate.of(2026, 5, 2), reservationTimeResponseDto,
                        themeResponseDto));
    }

    @Test
    void readAllTest() {
        ReservationTime reservationTime = reservationTimeDao.create(new ReservationTime(LocalTime.of(10, 0)));
        ReservationTimeResponseDto reservationTimeResponseDto = ReservationTimeResponseDto.from(reservationTime);
        Theme theme = themeDao.create(new Theme("피즈의 모험", "모험 이야기", "url.jpg"));
        ThemeResponseDto themeResponseDto = ThemeResponseDto.from(theme);

        reservationService.create(new ReservationRequestDto("fizz", LocalDate.of(2026, 5, 2), 1L, 1L));
        reservationService.create(new ReservationRequestDto("fizz2", LocalDate.of(2026, 5, 4), 1L, 1L));

        List<ReservationResponseDto> responseDtos = reservationService.readAll();

        assertThat(responseDtos.getFirst()).isEqualTo(
                new ReservationResponseDto(responseDtos.getFirst().id(), "fizz", LocalDate.of(2026, 5, 2),
                        reservationTimeResponseDto,
                        themeResponseDto));
        assertThat(responseDtos.get(1)).isEqualTo(
                new ReservationResponseDto(responseDtos.get(1).id(), "fizz2", LocalDate.of(2026, 5, 4),
                        reservationTimeResponseDto,
                        themeResponseDto));
    }

    @Test
    void deleteTest() {
        reservationTimeDao.create(new ReservationTime(LocalTime.of(10, 0)));
        themeDao.create(new Theme("피즈의 모험", "모험 이야기", "url.jpg"));

        reservationService.create(new ReservationRequestDto("fizz", LocalDate.of(2026, 5, 2), 1L, 1L));
        reservationService.delete(1L);

        List<ReservationResponseDto> responseDtos = reservationService.readAll();

        assertThat(responseDtos.size()).isEqualTo(0);
    }
}
