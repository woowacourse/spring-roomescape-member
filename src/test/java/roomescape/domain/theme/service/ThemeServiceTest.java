package roomescape.domain.theme.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.theme.dto.request.ThemeCreateRequestDTO;
import roomescape.domain.theme.dto.response.ThemeResponseDTO;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.repository.FakeThemeRepository;
import roomescape.domain.time.entity.Time;

class ThemeServiceTest {

    private final ThemeService themeService;
    private final FakeThemeRepository themeRepository;

    ThemeServiceTest() {
        this.themeRepository = new FakeThemeRepository();
        this.themeService = new ThemeService(themeRepository);
    }

    @Nested
    class GetThemesTest {

        @Test
        void 성공() {
            // when
            List<ThemeResponseDTO> actual = themeService.getThemes();

            // then
            assertEquals(0, actual.size());
        }
    }

    @Nested
    class GetPopularThemesTest {

        @Test
        void 성공() {
            List<Theme> themes = IntStream.rangeClosed(1, 15).mapToObj(i -> {
                return Theme.create("테마" + i, "설명" + i, "https://image.com/" + i + ".png");
            }).collect(Collectors.toList());

            themeRepository.saveAllThemes(themes);

            List<Reservation> reservations = new ArrayList<>();
            LocalDate targetDate = LocalDate.of(2026, 5, 5); // 조회 범위 내 날짜

            List<Theme> savedThemes = themeRepository.findAllThemes();
            for (int i = 0; i < savedThemes.size(); i++) {
                Theme theme = savedThemes.get(i);
                int reservationCount = (15 - i) * 5;
                for (int j = 0; j < reservationCount; j++) {
                    reservations.add(Reservation.create(
                        "예약자" + j,
                        targetDate,
                        Time.reconstruct(1L, LocalTime.of(10, 0)),
                        theme
                    ));
                }
            }

            themeRepository.saveAllReservations(reservations);

            // when
            List<ThemeResponseDTO> actual = themeService.getPopularThemes(
                LocalDate.of(2026, 5, 1),
                LocalDate.of(2026, 5, 31),
                10
            );

            // then
            assertAll(
                () -> assertEquals(10, actual.size()),
                () -> assertEquals("테마1", actual.get(0).name()),
                () -> assertEquals("테마2", actual.get(1).name()),
                () -> assertEquals("테마3", actual.get(2).name()),
                () -> assertEquals("테마4", actual.get(3).name()),
                () -> assertEquals("테마5", actual.get(4).name()),
                () -> assertEquals("테마6", actual.get(5).name()),
                () -> assertEquals("테마7", actual.get(6).name()),
                () -> assertEquals("테마8", actual.get(7).name()),
                () -> assertEquals("테마9", actual.get(8).name()),
                () -> assertEquals("테마10", actual.get(9).name())
            );
        }
    }

    @Nested
    class SaveThemeTest {

        @Test
        void 성공() {
            // given
            ThemeCreateRequestDTO request = new ThemeCreateRequestDTO(
                "피온",
                "테마 설명",
                "https://roomescape.com/images/themes/prison-room.png"
            );

            // when
            ThemeResponseDTO actual = themeService.saveTheme(request);

            // then
            assertAll(
                () -> assertEquals(1L, actual.id()),
                () -> assertEquals("피온", actual.name()),
                () -> assertEquals("테마 설명", actual.description()),
                () -> assertEquals("https://roomescape.com/images/themes/prison-room.png", actual.imageUrl())
            );
        }
    }

    @Nested
    class DeleteThemeTest {

        @Test
        void 성공() {
            // given
            themeRepository.save(Theme.create("브라운", "테마 설명", "https://roomescape.com/images/themes/prison-room.png"));

            // when
            themeService.deleteThemeById(1L);
            List<Theme> actual = themeRepository.findAllThemes();

            // then
            assertThat(actual).asInstanceOf(InstanceOfAssertFactories.LIST).hasSize(0);
        }
    }
}
