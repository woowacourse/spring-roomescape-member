package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.theme.ThemeResponse;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(value = "classpath:test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class PopularThemeTest {

    @Autowired
    private ThemeService themeService;

    @MockBean
    private Clock clock;

    @BeforeEach
    void setUp() {
        given(clock.instant()).willReturn(Instant.parse("2024-05-02T00:00:00Z"));
        given(clock.getZone()).willReturn(ZoneId.of("Asia/Seoul"));
    }

    @Test
    @DisplayName("전달한 날짜 기준 이전 일주일 간 예약이 많이된 상위 10개의 테마를 응답한다.")
    void findPopulars() {
        //when
        List<ThemeResponse> results = themeService.findPopulars();

        //then
        assertAll(
                () -> assertThat(results).hasSize(10),
                () -> assertThat(results.get(0).id()).isSameAs(5L),
                () -> assertThat(results.get(1).id()).isSameAs(4L),
                () -> assertThat(results.get(2).id()).isSameAs(3L),
                () -> assertThat(results.get(3).id()).isSameAs(2L),
                () -> assertThat(results.get(4).id()).isSameAs(1L)
        );
    }
}
