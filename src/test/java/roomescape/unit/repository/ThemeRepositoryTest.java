package roomescape.unit.repository;

import static org.assertj.core.api.Assertions.*;
import static roomescape.common.Constant.FIXED_CLOCK;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.LongStream;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.common.RepositoryBaseTest;
import roomescape.reservation.domain.ReservationDate;
import roomescape.theme.domain.LastWeekRange;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.unit.fixture.ReservationDateFixture;
import roomescape.unit.fixture.ReservationDbFixture;
import roomescape.unit.fixture.ReservationTimeDbFixture;
import roomescape.unit.fixture.ThemeDbFixture;

public class ThemeRepositoryTest extends RepositoryBaseTest {

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private ReservationDbFixture reservationDbFixture;

    @Autowired
    private ThemeDbFixture themeDbFixture;

    @Autowired
    private ReservationTimeDbFixture reservationTimeDbFixture;

    private static final String SELECT_THEME_BY_ID = "SELECT * FROM theme WHERE id = ?";
    private static final String COUNT_THEME_BY_ID = "SELECT COUNT(*) FROM theme WHERE id = ?";

    @Test
    void 테마를_저장한다() {
        // given
        Theme saved = themeRepository.save("공포", "무섭다", "thumb.jpg");

        // when
        Map<String, Object> row = jdbcTemplate.queryForMap(SELECT_THEME_BY_ID, saved.getId());

        // then
        assertThat(row.get("name")).isEqualTo("공포");
        assertThat(row.get("description")).isEqualTo("무섭다");
        assertThat(row.get("thumbnail")).isEqualTo("thumb.jpg");
    }

    @Test
    void 테마를_여러개_조회한다() {
        // given
        themeDbFixture.공포();
        themeDbFixture.로맨스();

        // when
        int count = themeRepository.findAll().size();

        // then
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("테마가 존재하는 경우")
    void 특정_테마를_조회한다1() {
        // given
        Theme 공포 = themeDbFixture.공포();

        // when
        Optional<Theme> found = themeRepository.findById(공포.getId());

        // then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(found).isPresent();

        Theme theme = found.get();
        softly.assertThat(theme.getId()).isEqualTo(공포.getId());
        softly.assertThat(theme.getName()).isEqualTo(공포.getName());
        softly.assertThat(theme.getThumbnail()).isEqualTo(공포.getThumbnail());
        softly.assertThat(theme.getDescription()).isEqualTo(공포.getDescription());
    }

    @Test
    @DisplayName("테마가 존재하지 않는 경우")
    void 특정_테마를_조회한다2() {
        // given
        themeDbFixture.공포();

        // when
        Optional<Theme> found = themeRepository.findById(2L);

        // then
        assertThat(found).isNotPresent();
    }

    @Test
    void 테마를_삭제할_수_있다() {
        // given
        Theme saved = themeRepository.save("공포", "무섭다", "thumb.jpg");

        // when
        themeRepository.deleteById(saved.getId());

        // then
        Long count = jdbcTemplate.queryForObject(COUNT_THEME_BY_ID, Long.class, saved.getId());
        assertThat(count).isEqualTo(0);
    }

    @Test
    void 최근_일주일_인기_테마를_조회할_수_있다() {
        // given
        List<Theme> themes = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            themes.add(themeDbFixture.커스텀_테마("테마" + i));
        }

        // 예약 수 차등 생성: 오늘과 7일 전
        for (int i = 0; i < 20; i++) {
            addReservation(i, ReservationDateFixture.예약날짜_오늘, reservationTimeDbFixture.예약시간_10시(), themes.get(i));
            addReservation(19 - i, ReservationDateFixture.예약날짜_7일전, reservationTimeDbFixture.예약시간_10시(), themes.get(i));
        }

        // when
        LastWeekRange range = new LastWeekRange(LocalDate.now(FIXED_CLOCK));
        List<Theme> popularThemes = themeRepository.findPopularThemeDuringAWeek(10, range);

        // then
        assertThat(popularThemes).hasSize(10);
        assertThat(popularThemes)
                .extracting(Theme::getId)
                .containsExactlyElementsOf(
                        LongStream.rangeClosed(1, 10)
                                .boxed()
                                .toList()
                );
    }

    private void addReservation(int count, ReservationDate date, ReservationTime time, Theme theme) {
        for (int i = 0; i < count; i++) {
            reservationDbFixture.예약_생성_한스(date, time, theme);
        }
    }
}
