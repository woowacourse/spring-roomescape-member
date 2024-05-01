package roomescape.repository;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;
import roomescape.repository.rowmapper.ThemeRowMapper;

@TestExecutionListeners(value = {
        DatabaseCleanupListener.class,
        DependencyInjectionTestExecutionListener.class
})
@JdbcTest
class JdbcThemeRepositoryTest {

    @Autowired
    DataSource dataSource;
    private ThemeRepository themeRepository;

    private final Theme theme1 = new Theme(null, "안돌", "안녕하세요", "hi.jpg");
    private final Theme theme2 = new Theme(null, "러너덕", "반가워요!", "hi.jpg");

    @BeforeEach
    void setUp() {
        RowMapper<Theme> rowMapper = new ThemeRowMapper();
        themeRepository = new JdbcThemeRepository(dataSource, rowMapper);
    }

    @DisplayName("저장된 모든 테마 정보를 가져온다.")
    @Test
    void find_all_reservation_times() {
        themeRepository.insertTheme(theme1);
        themeRepository.insertTheme(theme2);

        List<Theme> allReservationTimes = themeRepository.findAllThemes();

        assertThat(allReservationTimes.size()).isEqualTo(2);
    }

    @DisplayName("테마를 저장한다.")
    @Test
    void save_reservation_time() {
        Theme time = themeRepository.insertTheme(theme1);

        assertAll(
                () -> assertThat(time.getId()).isEqualTo(1),
                () -> assertThat(time.getName()).isEqualTo("안돌"),
                () -> assertThat(time.getDescription()).isEqualTo("안녕하세요"),
                () -> assertThat(time.getThumbnail()).isEqualTo("hi.jpg")
        );
    }

    @Test
    @DisplayName("테마를 id로 삭제한다.")
    void delete_reservation_time_by_id() {
        themeRepository.insertTheme(theme1);
        int beforeSize = themeRepository.findAllThemes().size();

        themeRepository.deleteThemeById(1L);
        int afterSize = themeRepository.findAllThemes().size();

        assertAll(
                () -> assertThat(beforeSize).isEqualTo(1),
                () -> assertThat(afterSize).isEqualTo(0)
        );
    }

    @Test
    @DisplayName("테이블에 테마 존재 여부를 판단한다.")
    void is_Exist_reservation_time() {
        themeRepository.insertTheme(theme1);

        boolean exist = themeRepository.isExistThemeOf(1L);
        boolean notExist = themeRepository.isExistThemeOf(2L);

        assertAll(
                () -> assertThat(exist).isTrue(),
                () -> assertThat(notExist).isFalse()
        );
    }
}
