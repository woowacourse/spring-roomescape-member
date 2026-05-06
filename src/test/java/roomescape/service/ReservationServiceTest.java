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
                .containsExactly(2L, 1L);
    }
}
