package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayNameGeneration(ReplaceUnderscores.class)
@ActiveProfiles(value = "test")
public class ThemeServiceTest {

    private ThemeService service;

    @BeforeEach
    void setUp() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .addScript("data.sql")
                .build();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        Clock clock = Clock.fixed(Instant.parse("2023-03-26T08:25:24Z"), ZoneId.systemDefault());
        service = new ThemeService(new ReservationDao(jdbcTemplate), new ThemeDao(jdbcTemplate), clock);
    }

    @Test
    void 테마_등록() {
        // given
        ThemeRequest request = new ThemeRequest("name", "description", "thumbnail");

        // when
        ThemeResponse response = service.addTheme(request);

        // then
        assertThat(response.id()).isEqualTo(17);
        assertThat(response.name()).isEqualTo(request.name());
        assertThat(response.description()).isEqualTo(request.description());
        assertThat(response.thumbnail()).isEqualTo(request.thumbnail());
    }

    @Test
    void 테마_이름이_중복되면_예외발생() {
        // given
        ThemeRequest request = new ThemeRequest("name", "description", "thumbnail");

        // when
        service.addTheme(request);

        // then
        assertThatThrownBy(() -> service.addTheme(request))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 모든_테마_조회() {
        // when
        List<ThemeResponse> responses = service.getThemes();

        // then
        assertThat(responses).hasSize(16);
    }

    @Test
    void 일주일동안_가장_인기있는_테마_10개_조회() {
        // when
        List<ThemeResponse> topTenResponses = service.getTopTenTheme();

        // then
        assertThat(topTenResponses).hasSize(6);
    }

    @Test
    void 테마_삭제_성공() {
        // given
        Long id = 16L;

        // when, then
        assertThatCode(() -> service.deleteTheme(id))
                .doesNotThrowAnyException();
    }

    @Test
    void 없는_id로_삭제시_예외발생() {
        // given
        Long id = 999L;

        // when, then
        assertThatThrownBy(() -> service.deleteTheme(id))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테마에_대해_예약이_존재하는_경우_삭제_시_예외발생() {
        // given
        Long id = 1L;

        // when, then
        assertThatThrownBy(() -> service.deleteTheme(id))
                .isInstanceOf(IllegalStateException.class);
    }
}
