package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class FakeThemeRepositoryTest {

    FakeThemeRepository themeRepository;
    Theme sampleTheme = new Theme(null, "a", "b", "c");

    @DisplayName("Theme을 저장할 수 있다")
    @Test
    void saveThemeTest() {
        themeRepository = new FakeThemeRepository(new ArrayList<>());

        Optional<Theme> optionalTheme = themeRepository.save(sampleTheme);
        Theme savedTheme = optionalTheme.get();
        Long saveId = savedTheme.id();

        assertThat(saveId).isEqualTo(1L);
    }

    @Nested
    @DisplayName("테마 조회")
    class ThemeFindTest {

        @DisplayName("저장된 Theme을 불러올 수 있다")
        @Test
        void findAllThemeTest() {
            Theme theme1 = new Theme(1L, "a", "b", "c");
            Theme theme2 = new Theme(2L, "a", "b", "c");
            Theme theme3 = new Theme(3L, "a", "b", "c");
            themeRepository = new FakeThemeRepository(List.of(theme1, theme2, theme3));

            List<Theme> allThemes = themeRepository.findAll();

            Long themeId1 = allThemes.get(0).id();
            Long themeId2 = allThemes.get(1).id();
            Long themeId3 = allThemes.get(2).id();

            assertAll(
                    () -> assertThat(themeId1).isEqualTo(1L),
                    () -> assertThat(themeId2).isEqualTo(2L),
                    () -> assertThat(themeId3).isEqualTo(3L),
                    () -> assertThat(allThemes).hasSize(3)
            );
        }

        @DisplayName("저장되지 않은 테마를 불러오면 빈 Optional 반환된다")
        @Test
        void findInvalidThemeTest() {
            themeRepository = new FakeThemeRepository(new ArrayList<>());

            assertThat(themeRepository.findById(2L)).isEmpty();
        }
    }

    @Nested
    @DisplayName("테마 삭제")
    class ThemeDeleteTest {

        @DisplayName("존재하는 Id의 Theme을 삭제할 수 있다")
        @Test
        void deleteThemeTest() {
            ArrayList<Theme> themes = new ArrayList<>();
            themes.add(new Theme(1L, "a", "b", "c"));
            themeRepository = new FakeThemeRepository(themes);

            Long deleteId = 1L;

            assertDoesNotThrow(() -> themeRepository.deleteById(deleteId));
        }

        @DisplayName("삭제된 Id의 갯수만큼 반환된다")
        @Test
        void deleteThemeCountTest() {
            ArrayList<Theme> themes = new ArrayList<>();
            themes.add(new Theme(1L, "a", "b", "c"));
            themes.add(new Theme(1L, "a", "b", "c"));
            themes.add(new Theme(1L, "a", "b", "c"));
            themeRepository = new FakeThemeRepository(themes);

            Long deleteId = 1L;
            int deletedCount = themeRepository.deleteById(deleteId);

            Assertions.assertThat(deletedCount).isEqualTo(3);
        }

        @DisplayName("삭제된 Id의 갯수만큼 반환된다")
        @Test
        void deleteThemeNotExistIdCountTest() {
            ArrayList<Theme> themes = new ArrayList<>();
            themes.add(new Theme(1L, "a", "b", "c"));
            themes.add(new Theme(1L, "a", "b", "c"));
            themes.add(new Theme(1L, "a", "b", "c"));
            themeRepository = new FakeThemeRepository(themes);

            Long deleteId = 2L;
            int deletedCount = themeRepository.deleteById(deleteId);

            Assertions.assertThat(deletedCount).isEqualTo(0);
        }

        @DisplayName("이미 예약이 존재하는 경우 테마를 삭제할 수 없다")
        @Test
        void deleteThemeOfExistingReservationTest() {
            ReservationTime reservationTime = new ReservationTime(1L, LocalTime.now());
            Theme theme = new Theme(1L, "우테코", "방탈출", ".png");
            Reservation reservation = new Reservation(1L, "가이온", LocalDate.now(), reservationTime, theme);
            List<Theme> themes = new ArrayList<>();
            themes.add(theme);

            themeRepository = new FakeThemeRepository(themes);
            themeRepository.addReservation(reservation);

            assertThatThrownBy(() -> themeRepository.deleteById(1L))
                    .isInstanceOf(IllegalStateException.class);
        }
    }
}
