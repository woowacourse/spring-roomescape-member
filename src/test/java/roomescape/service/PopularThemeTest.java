package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase;

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
@Sql(value = "classpath:test_db_clean.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:test_data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class PopularThemeTest {

    @Autowired
    private ThemeService themeService;

    @Test
    @DisplayName("시작일부터 종료일까지 예약이 많이된 상위 10개의 테마를 응답한다.")
    void findPopulars() {
        //given
        LocalDate startDate = LocalDate.of(2024, 4, 25);
        LocalDate endDate = LocalDate.of(2024, 5, 1);

        //when
        List<ThemeResponse> results = themeService.findPopulars(startDate, endDate);
        ThemeResponse firstResponse = results.get(0);
        ThemeResponse secondResponse = results.get(1);
        ThemeResponse thirdResponse = results.get(2);
        ThemeResponse fourthResponse = results.get(3);
        ThemeResponse fifthResponse = results.get(4);
        /*
         *   테마 통계
         *   5번 방탈출 - 5개
         *   4번 방탈출 - 4개
         *   3번 방탈출 - 3개
         *   2번 방탈출 - 2개
         *   1번 방탈출 - 1개
         * */
        //then
        assertAll(
                () -> assertThat(results).hasSize(10),
                () -> assertThat(firstResponse.getId()).isSameAs(5L),
                () -> assertThat(secondResponse.getId()).isSameAs(4L),
                () -> assertThat(thirdResponse.getId()).isSameAs(3L),
                () -> assertThat(fourthResponse.getId()).isSameAs(2L),
                () -> assertThat(fifthResponse.getId()).isSameAs(1L)
        );
    }
}
