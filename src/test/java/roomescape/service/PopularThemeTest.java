package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.theme.ThemeResponse;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(value = "classpath:test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class PopularThemeTest {

    @Autowired
    private ThemeService themeService;

    @Test
    @DisplayName("시작일부터 종료일까지 예약이 많이된 상위 10개의 테마를 응답한다.")
    void findPopulars() {
        //given
        LocalDate today = LocalDate.of(2024, 5, 2);

        //when
        List<ThemeResponse> results = themeService.findPopulars(today);

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
