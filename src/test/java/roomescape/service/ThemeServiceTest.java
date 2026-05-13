package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import java.time.Clock;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import roomescape.TestTimeConfig;
import roomescape.dto.ThemeResponseDTO;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Import(TestTimeConfig.class)
@Sql(scripts = "/empty.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ThemeServiceTest {

    @LocalServerPort
    int port;
    @Autowired
    Clock clock;
    @Autowired
    private ThemeService themeService;

    @BeforeEach
    public void init() {
        RestAssured.port = port;
    }

    @DisplayName("인기 테마를 조회한다")
    @Test
    void 최근_1주_동안의_예약_상위_10개의_테마를_조회한다() {
        List<ThemeResponseDTO> popularThemes = themeService.getPopularThemes(1L, 10L);
        assertThat(popularThemes)
                .map(ThemeResponseDTO::id)
                .containsExactly(
                        1L, 2L, 3L, // 1순위: 테마의 예약 수 내림차순 정렬
                        6L, 5L, 4L, 8L, 7L, // 2순위: 예약 수가 같으면 테마 이름 오름차순 정렬
                        10L, 9L // 예약 개수가 0개여도, 상위 10위 이내라면 조회되어야 함 (예약 개수 0개인 테마들은 2순위 정렬 기준으로 비교)
                );
    }
}
