package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Theme;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("/data.sql")
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @DisplayName("인기 테마를 조회한다")
    @Test
    void 최근_1주_동안의_예약_상위_10개의_테마를_조회한다() {
        List<Theme> popularThemes = reservationService.getPopularThemes();
        assertThat(popularThemes)
                .map(theme -> theme.getId())
                .containsExactly(
                        1L, 2L, 3L, // 1순위: 테마의 예약 수 내림차순 정렬
                        6L, 5L, 4L, 8L, 7L, // 2순위: 예약 수가 같으면 테마 이름 오름차순 정렬
                        10L, 9L // 예약 개수가 0개여도, 상위 10위 이내라면 조회되어야 함 (예약 개수 0개인 테마들은 2순위 정렬 기준으로 비교)
                );
    }
}
