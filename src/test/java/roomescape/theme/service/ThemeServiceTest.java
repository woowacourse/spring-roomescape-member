package roomescape.theme.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
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
import roomescape.global.exception.RoomEscapeException.BadRequestException;
import roomescape.global.exception.RoomEscapeException.ResourceNotFoundException;
import roomescape.reservation.dto.response.AvailableReservationResponse;
import roomescape.reservation.repository.ReservationDao;
import roomescape.reservationtime.repository.ReservationTimeDao;
import roomescape.theme.dto.request.AdminThemePageResponse;
import roomescape.theme.dto.request.ThemeRequest;
import roomescape.theme.dto.response.CreateThemeResponse;
import roomescape.theme.dto.response.ThemeResponse;
import roomescape.theme.repository.ThemeDao;

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
        service = new ThemeService(
                new ReservationDao(jdbcTemplate),
                new ReservationTimeDao(jdbcTemplate),
                new ThemeDao(jdbcTemplate)
        );
    }

    @Test
    void 테마_등록() {
        // given
        ThemeRequest request = new ThemeRequest("name", "description", "thumbnail");

        // when
        CreateThemeResponse response = service.addTheme(request);

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
                .isInstanceOf(BadRequestException.class);
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
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void 테마에_대해_예약이_존재하는_경우_삭제_시_예외발생() {
        // given
        Long id = 1L;

        // when, then
        assertThatThrownBy(() -> service.deleteTheme(id))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void 선택한_날짜와_테마의_시간과_예약_여부_조회하기() {
        // given
        LocalDate date = LocalDate.now().minusDays(5);
        Long themeId = 11L;

        // when
        List<AvailableReservationResponse> responses = service.getThemesTimesWithStatus(themeId,
                date);
        List<Boolean> list = responses.stream()
                .map(AvailableReservationResponse::isBooked).toList();

        // then
        assertThat(responses).hasSize(12);
        assertThat(list.stream().filter(b -> b).count()).isEqualTo(1L);
        assertThat(list.stream().filter(b -> !b).count()).isEqualTo(11L);
    }

    @Test
    void 페이지에_해당하는_테마들_반환() {
        // given
        int page = 2;

        // when
        AdminThemePageResponse themesByPage = service.getThemesByPage(page);

        // then
        assertThat(themesByPage.totalPages()).isEqualTo(2);
        assertThat(themesByPage.themes()).hasSize(6);
    }
}
