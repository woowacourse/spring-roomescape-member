package roomescape.theme.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import roomescape.theme.domain.Theme;

@JdbcTest
class ThemeRepositoryTest {
    private JdbcThemeRepository jdbcThemeRepository;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup(){
        jdbcThemeRepository = new JdbcThemeRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("등록된 테마가 여러개이면 조회 시 등록된 갯수만큼 반환한다.")
    void findAll(){
        // given
        List<Theme> themes = List.of(Theme.create("테마1", "테마1 설명", "테마1 썸네일"),
                Theme.create("테마2", "테마2 설명", "테마2 썸네일"),
                Theme.create("테마3", "테마3 설명", "테마3 썸네일"));
        saveAll(themes);

        // when
        List<Theme> actual = jdbcThemeRepository.findAll();

        // then
        assertThat(actual)
                .hasSize(themes.size());
    }

    @Test
    @DisplayName("등록된 테마와 조회되는 테마의 모든 필드가 일치한다.")
    void findById(){
        // given
        Theme savedTheme =  jdbcThemeRepository.save(Theme.create("테마1", "테마1 설명", "테마1 썸네일"));

        // when
        Theme actual = jdbcThemeRepository.findById(savedTheme.id()).get();

        // then
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(savedTheme);
    }

    @Test
    @DisplayName("테마를 1개 등록하면 테마 데이터 수가 1 증가한다.")
    void save(){
        // given
        List<Theme> themes = List.of();
        Theme theme = Theme.create("테마1", "테마1 설명", "테마1 썸네일");

        // when
        jdbcThemeRepository.save(theme);

        // then
        assertThat(jdbcThemeRepository.findAll())
                .hasSize(themes.size() + 1);
    }

    @Test
    @DisplayName("테마를 활성화한다.")
    void updateStatus_active(){
        // given
        Theme savedTheme = jdbcThemeRepository.save(Theme.create("테마1", "테마1 설명", "테마1 썸네일"));
        savedTheme.updateStatus(true);

        // when
        jdbcThemeRepository.updateStatus(savedTheme);

        // then
        assertThat(jdbcThemeRepository.findById(savedTheme.id()).get().isActive())
                .isTrue();
    }


    @Test
    @DisplayName("테마를 비활성화한다.")
    void updateStatus_deactivate(){
        // given
        Theme theme = Theme.create("테마1", "테마1 설명", "테마1 썸네일");
        theme.updateStatus(true);
        Theme savedTheme = jdbcThemeRepository.save(theme);
        savedTheme.updateStatus(false);

        // when
        jdbcThemeRepository.updateStatus(savedTheme);

        // then
        assertThat(jdbcThemeRepository.findById(savedTheme.id()).get().isActive())
                .isFalse();
    }

    private void saveAll(List<Theme> themes){
        for(Theme theme : themes){
            jdbcThemeRepository.save(theme);
        }
    }
}
