package roomescape.theme.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import roomescape.theme.domain.Theme;

class JdbcThemeRepositoryTest {

    private static EmbeddedDatabase db;
    private JdbcThemeRepository repository;

    @BeforeEach
    void setUp() {
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:schema.sql")
                .addScript("classpath:data.sql")
                .build();
        repository = new JdbcThemeRepository(db);
    }

    @AfterEach
    void shutdownDatabase() {
        db.shutdown();
    }

    @Test
    void 테마을를_올바르게_저장한다() {
        // given
        Theme theme = new Theme("테마이름", "테마설명", "테마썸네일");

        // when
        Theme savedTheme = repository.save(theme);

        // then
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(savedTheme.getId()).isNotNull();
            soft.assertThat(savedTheme.getName()).isEqualTo("테마이름");
            soft.assertThat(savedTheme.getDescription()).isEqualTo("테마설명");
            soft.assertThat(savedTheme.getThumbnail()).isEqualTo("테마썸네일");
        });
    }

    @Test
    void 모든_예약_시간을_조회한다() {
        // given
        // when
        List<Theme> themes = repository.findAll();

        // then
        assertThat(themes).hasSize(2);
    }

    @Test
    void id에_알맞은_예약_시간을_삭제한다() {
        // given
        // when
        repository.deleteById(1L);
        List<Theme> themes = repository.findAll();

        // then
        assertThat(themes).hasSize(1)
                .extracting(Theme::getId)
                .doesNotContain(1L);
    }

    @Test
    void id에_알맞은_예약_시간을_가져온다() {
        // given
        Long id = 1L;

        // when
        Theme theme = repository.findById(id).get();

        // then
        assertThat(theme.getId()).isEqualTo(id);
    }

    @Test
    void 존재하지_않는_id면_빈_Optional을_반환한다() {
        // given
        Long invalidId = 999L;
        // when
        // then
        assertThat(repository.findById(invalidId)).isEmpty();
    }
}


