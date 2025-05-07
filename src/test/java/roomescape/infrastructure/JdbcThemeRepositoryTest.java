package roomescape.infrastructure;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.business.model.entity.Theme;
import roomescape.test_util.JdbcTestUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
@Import(JdbcThemeRepository.class)
class JdbcThemeRepositoryTest {

    private static final LocalDate DATE = LocalDate.now().plusDays(5);

    private final JdbcThemeRepository sut;
    private final JdbcTestUtil testUtil;

    @Autowired
    public JdbcThemeRepositoryTest(final JdbcThemeRepository sut, final JdbcTemplate jdbcTemplate) {
        this.sut = sut;
        this.testUtil = new JdbcTestUtil(jdbcTemplate);
    }

    @AfterEach
    void tearDown() {
        testUtil.deleteAll();
    }

    @Test
    void 테마를_저장할_수_있다() {
        final Theme result = sut.save(Theme.beforeSave("주홍색 연구", "", ""));

        assertThat(result).isNotNull();
    }

    @Test
    void 모든_테마를_찾을_수_있다() {
        final long id1 = testUtil.insertTheme("주홍색 연구");
        final long id2 = testUtil.insertTheme("아라비아해의 비밀");

        final List<Theme> result = sut.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(id1);
        assertThat(result.get(1).getId()).isEqualTo(id2);
    }

    @Test
    void 예약이_많은_순으로_테마를_찾을_수_있다() {
        final long timeId = testUtil.insertReservationTime(LocalTime.of(10, 0));
        final long themeId1 = testUtil.insertTheme("주홍색 연구");
        final long themeId2 = testUtil.insertTheme("아라비아해의 비밀");
        final long themeId3 = testUtil.insertTheme("범위_외부_테마");
        final long userId1 = testUtil.insertUser("돔푸");
        final long userId2 = testUtil.insertUser("레몬");
        testUtil.insertReservation(userId1, DATE, timeId, themeId1);
        testUtil.insertReservation(userId1, DATE, timeId, themeId2);
        testUtil.insertReservation(userId1, DATE, timeId, themeId2);
        testUtil.insertReservation(userId2, DATE.minusDays(10), timeId, themeId3);
        testUtil.insertReservation(userId2, DATE.minusDays(10), timeId, themeId3);
        testUtil.insertReservation(userId2, DATE.plusDays(10), timeId, themeId3);

        final List<Theme> result = sut.findPopularThemes(DATE.minusDays(5), DATE.plusDays(5), 2);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(themeId2);
        assertThat(result.get(1).getId()).isEqualTo(themeId1);
    }

    @Test
    void ID를_기준으로_테마를_찾을_수_있다() {
        final long id = testUtil.insertTheme("주홍색 연구");

        final Optional<Theme> result = sut.findById(id);

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getId()).isEqualTo(id);
        assertThat(result.get().getName()).isEqualTo("주홍색 연구");
    }

    @Test
    void ID를_기준으로_존재하는지_확인할_수_있다() {
        final long id = testUtil.insertTheme("주홍색 연구");

        final boolean result = sut.existById(id);

        assertThat(result).isTrue();
    }

    @Test
    void ID를_기준으로_삭제할_수_있다() {
        final long id = testUtil.insertTheme("주홍색 연구");

        sut.deleteById(id);

        assertThat(testUtil.countTheme()).isEqualTo(0);
    }
}
