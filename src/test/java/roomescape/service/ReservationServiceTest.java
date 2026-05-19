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
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
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
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Test
    void notExistReservationTimeExceptionTest() {
        ReservationTime createdTime = reservationTimeRepository.create(new ReservationTime(LocalTime.of(12, 34)));
        Long testId = createdTime.getId();
        reservationTimeRepository.delete(testId);

        ThemeResponseDto themeResponseDto = createTheme();
        ReservationRequestDto requestDto = new ReservationRequestDto(
                "fizz",
                LocalDate.of(2026, 5, 2),
                testId,
                themeResponseDto.id()
        );

        assertThatThrownBy(() -> reservationService.create(requestDto))
                .hasMessage("[ERROR] 해당 ID의 예약 시간을 찾을 수 없습니다.")
                .isInstanceOf(CustomException.class);
    }

    private ReservationTimeResponseDto createReservationTime() {
        ReservationTime createdTime = reservationTimeRepository.create(new ReservationTime(LocalTime.of(10, 0)));
        return ReservationTimeResponseDto.from(createdTime);
    }

    private ThemeResponseDto createTheme() {
        Theme createdTheme = themeRepository.create(new Theme("피즈의 모험", "모험 이야기", "url.jpg"));
        return ThemeResponseDto.from(createdTheme);
    }

    @Test
    void createTest() {
        ReservationTimeResponseDto reservationTimeResponseDto = createReservationTime();
        ThemeResponseDto themeResponseDto = createTheme();

        ReservationResponseDto responseDto = reservationService.create(
                new ReservationRequestDto("fizz", LocalDate.of(2026, 5, 2), reservationTimeResponseDto.id(),
                        themeResponseDto.id()));

        assertThat(responseDto).isEqualTo(
                new ReservationResponseDto(responseDto.id(), "fizz", LocalDate.of(2026, 5, 2),
                        reservationTimeResponseDto, themeResponseDto));
    }

    @Test
    void findAllTest() {
        ReservationTimeResponseDto reservationTimeResponseDto = createReservationTime();
        ThemeResponseDto themeResponseDto = createTheme();
        ReservationResponseDto firstResponse = reservationService.create(
                new ReservationRequestDto("fizz", LocalDate.of(2026, 5, 2), reservationTimeResponseDto.id(),
                        themeResponseDto.id()));
        ReservationResponseDto secondResponse = reservationService.create(
                new ReservationRequestDto("fizz2", LocalDate.of(2026, 5, 3), reservationTimeResponseDto.id(),
                        themeResponseDto.id()));

        List<ReservationResponseDto> responseDtos = reservationService.findAll();

        assertThat(responseDtos.getFirst()).isEqualTo(firstResponse);
        assertThat(responseDtos.get(1)).isEqualTo(secondResponse);
    }

    @Test
    void deleteTest() {
        ReservationTimeResponseDto reservationTimeResponseDto = createReservationTime();
        ThemeResponseDto themeResponseDto = createTheme();
        ReservationResponseDto responseDto = reservationService.create(
                new ReservationRequestDto("fizz", LocalDate.of(2026, 5, 2), reservationTimeResponseDto.id(),
                        themeResponseDto.id()));
        reservationService.delete(responseDto.id());

        List<ReservationResponseDto> responseDtos = reservationService.findAll();

        assertThat(responseDtos.size()).isEqualTo(0);
    }
}
