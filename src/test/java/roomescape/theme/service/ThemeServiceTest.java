package roomescape.theme.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.DuplicateResourceException;
import roomescape.exception.ResourceInUseException;
import roomescape.exception.ResourceNotFoundException;
import roomescape.support.FixedClockConfig;
import roomescape.theme.controller.dto.ThemeRequest;
import roomescape.theme.domain.Theme;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@Import(FixedClockConfig.class)
class ThemeServiceTest {

    @Autowired
    private ThemeService themeService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        @Test
        @DisplayName("새로운 테마 이름이면 정상적으로 저장한다.")
        void saveSuccess() {
            // given
            ThemeRequest request = new ThemeRequest("공포의 수랏간", "매우 무섭습니다.", "https://example.com/image.png");

            // when
            Theme saved = themeService.save(request);

            // then
            assertThat(saved.getId()).isNotNull();
            assertThat(saved.getName()).isEqualTo("공포의 수랏간");
            assertThat(themeService.findAll()).hasSize(1);
        }

        @Test
        @DisplayName("이미 존재하는 테마 이름이면 DuplicateResourceException 이 발생한다.")
        void saveFailWhenDuplicateName() {
            // given
            themeService.save(new ThemeRequest("중복 이름", "설명", "https://example.com/a.png"));

            // when & then
            assertThatThrownBy(() ->
                    themeService.save(new ThemeRequest("중복 이름", "다른 설명", "https://example.com/b.png"))
            )
                    .isInstanceOf(DuplicateResourceException.class)
                    .hasMessageContaining("이미 존재하는 테마 이름입니다.");
        }
    }

    @Nested
    @DisplayName("getById 메서드는")
    class GetById {

        @Test
        @DisplayName("존재하는 ID 면 해당 테마를 반환한다.")
        void getByIdSuccess() {
            // given
            Theme saved = themeService.save(new ThemeRequest("테마", "설명", "https://example.com/a.png"));

            // when
            Theme result = themeService.getById(saved.getId());

            // then
            assertThat(result.getId()).isEqualTo(saved.getId());
            assertThat(result.getName()).isEqualTo("테마");
        }

        @Test
        @DisplayName("존재하지 않는 ID 면 ResourceNotFoundException 이 발생한다.")
        void getByIdFailWhenNotFound() {
            assertThatThrownBy(() -> themeService.getById(999L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("해당 ID의 테마가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("deleteById 메서드는")
    class DeleteById {

        @Test
        @DisplayName("참조되지 않는 테마면 정상적으로 삭제한다.")
        void deleteByIdSuccess() {
            // given
            Theme saved = themeService.save(new ThemeRequest("테마", "설명", "https://example.com/a.png"));

            // when
            themeService.deleteById(saved.getId());

            // then
            assertThat(themeService.findAll()).isEmpty();
        }

        @Test
        @DisplayName("예약에 사용 중인 테마는 ResourceInUseException 이 발생하고 삭제되지 않는다.")
        void deleteByIdFailWhenInUse() {
            // given
            Theme saved = themeService.save(new ThemeRequest("테마", "설명", "https://example.com/a.png"));
            Long timeId = insertReservationTime(LocalTime.of(10, 0));
            insertReservation("브라운", LocalDate.of(2026, 12, 31), timeId, saved.getId());

            // when & then
            assertThatThrownBy(() -> themeService.deleteById(saved.getId()))
                    .isInstanceOf(ResourceInUseException.class)
                    .hasMessageContaining("이 테마를 참조하는 예약이 있어 삭제할 수 없습니다.");

            assertThat(themeService.findAll()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("findAll 메서드는")
    class FindAll {

        @Test
        @DisplayName("저장된 모든 테마를 반환한다.")
        void findAllReturnsAll() {
            // given
            themeService.save(new ThemeRequest("A", "설명A", "https://example.com/a.png"));
            themeService.save(new ThemeRequest("B", "설명B", "https://example.com/b.png"));

            // when
            List<Theme> result = themeService.findAll();

            // then
            assertThat(result).extracting(Theme::getName).containsExactlyInAnyOrder("A", "B");
        }

        @Test
        @DisplayName("테마가 없으면 빈 목록을 반환한다.")
        void findAllReturnsEmpty() {
            assertThat(themeService.findAll()).isEmpty();
        }
    }

    @Nested
    @DisplayName("findPopularThemes 메서드는")
    class FindPopularThemes {

        @Test
        @DisplayName("최근 7일 이내 예약 수가 많은 테마를 내림차순으로 반환한다.")
        void findPopularThemesReturnsTopByRecentReservations() {
            // given
            Theme themeA = themeService.save(new ThemeRequest("A", "설명A", "https://example.com/a.png"));
            Theme themeB = themeService.save(new ThemeRequest("B", "설명B", "https://example.com/b.png"));
            Theme themeC = themeService.save(new ThemeRequest("C", "설명C", "https://example.com/c.png"));

            Long t10 = insertReservationTime(LocalTime.of(10, 0));
            Long t11 = insertReservationTime(LocalTime.of(11, 0));
            Long t12 = insertReservationTime(LocalTime.of(12, 0));

            // 기준 시각: 2026-05-06 → 최근 7일 = 2026-04-29 ~ 2026-05-06
            // A: 2건 (최근 7일), B: 3건 (최근 7일), C: 0건
            insertReservation("u1", LocalDate.of(2026, 5, 5), t10, themeA.getId());
            insertReservation("u2", LocalDate.of(2026, 5, 4), t11, themeA.getId());

            insertReservation("u3", LocalDate.of(2026, 5, 5), t11, themeB.getId());
            insertReservation("u4", LocalDate.of(2026, 5, 4), t10, themeB.getId());
            insertReservation("u5", LocalDate.of(2026, 5, 3), t12, themeB.getId());

            // when
            List<Theme> popular = themeService.findPopularThemes();

            // then
            assertThat(popular).extracting(Theme::getName).containsExactly("B", "A");
            assertThat(popular).extracting(Theme::getName).doesNotContain("C");
        }

        @Test
        @DisplayName("7일 보다 더 이전의 예약은 인기 테마 집계에서 제외된다.")
        void findPopularThemesExcludesOldReservations() {
            // given
            Theme theme = themeService.save(new ThemeRequest("Old", "설명", "https://example.com/o.png"));
            Long timeId = insertReservationTime(LocalTime.of(10, 0));

            // 기준 시각 2026-05-06 의 8일 전 = 2026-04-28 → 제외돼야 함
            insertReservation("oldUser", LocalDate.of(2026, 4, 28), timeId, theme.getId());

            // when
            List<Theme> popular = themeService.findPopularThemes();

            // then
            assertThat(popular).extracting(Theme::getName).doesNotContain("Old");
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class Update {

        @Test
        @DisplayName("존재하는 ID 의 테마를 수정한다.")
        void updateSuccess() {
            // given
            Theme saved = themeService.save(new ThemeRequest("OLD", "설명", "https://example.com/a.png"));

            // when
            Theme updated = themeService.update(
                    saved.getId(),
                    new ThemeRequest("NEW", "새 설명", "https://example.com/b.png")
            );

            // then
            assertThat(updated.getId()).isEqualTo(saved.getId());
            assertThat(updated.getName()).isEqualTo("NEW");
            assertThat(themeService.getById(saved.getId()).getName()).isEqualTo("NEW");
        }

        @Test
        @DisplayName("이름을 다른 테마와 같은 값으로 변경하려 하면 DuplicateResourceException 이 발생한다.")
        void updateFailWhenDuplicateName() {
            // given
            themeService.save(new ThemeRequest("이미있음", "설명1", "https://example.com/a.png"));
            Theme target = themeService.save(new ThemeRequest("바꿀것", "설명2", "https://example.com/b.png"));

            // when & then
            assertThatThrownBy(() -> themeService.update(
                    target.getId(),
                    new ThemeRequest("이미있음", "설명3", "https://example.com/c.png")
            ))
                    .isInstanceOf(DuplicateResourceException.class);
        }

        @Test
        @DisplayName("존재하지 않는 ID 면 ResourceNotFoundException 이 발생한다.")
        void updateFailWhenNotFound() {
            assertThatThrownBy(() -> themeService.update(
                    999L,
                    new ThemeRequest("X", "설명", "https://example.com/x.png")
            ))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    private Long insertReservationTime(LocalTime startAt) {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", startAt.toString());
        return jdbcTemplate.queryForObject(
                "SELECT id FROM reservation_time WHERE start_at = ?",
                Long.class,
                startAt.toString()
        );
    }

    private void insertReservation(String name, LocalDate date, Long timeId, Long themeId) {
        jdbcTemplate.update(
                "INSERT INTO reservation (name, reservation_date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                name, date.toString(), timeId, themeId
        );
    }
}
