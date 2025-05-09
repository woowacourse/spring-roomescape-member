package roomescape.domain.reservation.repository.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

@JdbcTest
@Import(ThemeDAO.class)
class ThemeDAOTest {

    @Autowired
    private ThemeDAO themeDAO;

    @DisplayName("테마 순위 테스트")
    @Test
    void test1() {
        // given

        // when

        // then
    }

}
