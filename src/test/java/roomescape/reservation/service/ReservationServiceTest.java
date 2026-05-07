package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.reservation.repository.PopularThemeQueryResult;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.service.dto.PopularThemesResult;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.repository.ReservationTimeRepository;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    ReservationTimeRepository reservationTimeRepository;

    @Mock
    ThemeRepository themeRepository;

    @DisplayName("인기 테마 조회 시 period=7이면 오늘 제외 직전 7일 범위로 조회한다.")
    @Test
    void findPopularThemesRange() {
        //given
        Clock clock = Clock.fixed(
                Instant.parse("2026-05-08T00:00:00Z"),
                ZoneId.of("Asia/Seoul")
        );

        ReservationService reservationService = new ReservationService(
                reservationRepository,
                reservationTimeRepository,
                themeRepository,
                clock
        );

        when(
                reservationRepository.findPopularThemes(
                        LocalDate.of(2026, 5, 1),
                        LocalDate.of(2026, 5, 7), 10
                )
        ).thenReturn(
                List.of(
                        new PopularThemeQueryResult(
                                1L,
                                "테마",
                                "설명",
                                "url"
                        )
                )
        );

        //when
        PopularThemesResult result = reservationService.findPopularThemes(7, 10);

        //then
        assertThat(result.popularThemes()).containsExactly(
                new PopularThemeQueryResult(
                        1L,
                        "테마",
                        "설명",
                        "url")
        );

        verify(reservationRepository).findPopularThemes(
                LocalDate.of(2026, 5, 1),
                LocalDate.of(2026, 5, 7),
                10
        );
    }
}
