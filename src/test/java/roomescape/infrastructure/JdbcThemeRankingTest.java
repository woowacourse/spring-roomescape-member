package roomescape.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.infrastructure.JdbcThemeRepository;

@JdbcTest
@Import(JdbcThemeRepository.class)
@ActiveProfiles("test")
@Sql(scripts = "/ranking-data.sql")
public class JdbcThemeRankingTest {

    @Autowired
    private JdbcThemeRepository jdbcThemeRepository;

    @DisplayName("기간 내 예약 수 기준으로 테마 랭킹을 조회한다")
    @Test
    void findThemeRanking() {
        // given
        LocalDate start = LocalDate.of(2025, 1, 1);
        LocalDate end = LocalDate.of(2025, 1, 7);
        int limit = 10;

        // when
        List<Theme> themeRanking = jdbcThemeRepository.findThemeRanking(start, end, limit);

        // then
        assertAll(
                () -> assertThat(themeRanking).hasSize(10),
                () -> assertThat(themeRanking.get(0).getName()).isEqualTo("1등테마"),
                () -> assertThat(themeRanking.get(1).getName()).isEqualTo("2등테마"),
                () -> assertThat(themeRanking.get(2).getName()).isEqualTo("3등테마")
        );
    }

    @DisplayName("limit 개수대로 테마 랭킹이 조회된다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10})
    void findThemeRanking_withLimit(int limit) {
        // given
        LocalDate start = LocalDate.of(2025, 1, 1);
        LocalDate end = LocalDate.of(2025, 1, 7);

        // when
        List<Theme> themeRanking = jdbcThemeRepository.findThemeRanking(start, end, limit);

        // then
        assertThat(themeRanking).hasSize(limit);
    }

    @DisplayName("기간 내 예약이 존재하지 않아도 기본적으로 limit을 채워서 조회된다.")
    @Test
    void noRanking_whenNoReservation_inDuration() {
        // given
        LocalDate start = LocalDate.of(2025, 2, 1);
        LocalDate end = LocalDate.of(2025, 2, 7);

        int limit = 10;

        // when
        List<Theme> themeRanking = jdbcThemeRepository.findThemeRanking(start, end, limit);

        // then
        assertThat(themeRanking).hasSize(limit);
    }
}
