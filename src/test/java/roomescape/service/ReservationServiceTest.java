package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.InvalidRequestException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@SpringBootTest
class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Mock
    private ThemeRepository themeRepository;

    @Test
    @DisplayName("이미 예약된 날짜, 시간, 테마로 예약하면 예외가 발생한다.")
    public void create_fail_whenDuplicatedReservation() {
        // given
        String name = "브라운";
        LocalDate date = LocalDate.of(2026, 5, 8);
        Long timeId = 1L;
        Long themeId = 1L;

        given(reservationTimeRepository.findById(timeId))
                .willReturn(Optional.of(new ReservationTime(timeId, LocalTime.of(10, 0))));
        given(themeRepository.findById(themeId))
                .willReturn(Optional.of(new Theme(themeId, "레벨2 탈출", "설명", "https://example.com/theme.png")));
        given(reservationRepository.existsByDateAndTimeIdAndThemeId(date, timeId, themeId))
                .willReturn(true);

        // when, then
        assertThatThrownBy(() -> reservationService.create(name, date, timeId, themeId))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("이미 예약된 시간입니다.");

        then(reservationRepository).should(never()).save(any());
    }
}
