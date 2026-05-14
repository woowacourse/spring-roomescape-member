package roomescape.dao.theme;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
import roomescape.dao.ThemeDao;
import roomescape.dao.row.ThemeRow;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public abstract class ThemeDaoContract {

    static final Long NOT_EXISTS_ID = Long.MAX_VALUE;

    @BeforeEach
    void cleanUp() {
        clear();
    }

    abstract ThemeDao dao();
    abstract void clear();

    private ThemeRow themeRow(String name, String thumbnailUrl, String description) {
        return new ThemeRow(name, thumbnailUrl, description);
    }

    @Nested
    @DisplayName("create는 ")
    class Create {

        @Test
        void 저장_후_ID가_채워진_객체를_반환한다() {
            ThemeRow theme = themeRow("테마", "www.test.com", "테스트 테마입니다.");

            ThemeRow saved = dao().create(theme);

            assertThat(saved.id()).isNotNull();
            assertThat(saved.name()).isEqualTo("테마");
        }

        @Test
        void 같은_name이면_DuplicateException() {
            ThemeRow theme = themeRow("테마", "www.test.com", "테스트 테마입니다.");

            dao().create(theme);

            assertThatThrownBy(() -> dao().create(theme))
                    .isInstanceOf(DuplicateKeyException.class);
        }
    }

    @Nested
    @DisplayName("findById는 ")
    class FindById {

        @Test
        void 존재하면_Optinal로_감싸_반환한다() {
            ThemeRow theme = themeRow("테마", "www.test.com", "테스트 테마입니다.");
            ThemeRow saved = dao().create(theme);

            Optional<ThemeRow> found = dao().findById(saved.id());

            assertThat(found).isPresent()
                    .get()
                    .isEqualTo(saved);
        }

        @Test
        void 존재하지_않으면_Optional_empty() {
            assertThat(dao().findById(NOT_EXISTS_ID)).isEmpty();
        }
    }

    @Nested
    @DisplayName("findAll은 ")
    class FindAll {

        @Test
        void 저장된_모든_테마를_반환한다() {
            ThemeRow theme1 = themeRow("테마1", "www.test1.com", "테스트 테마1입니다.");
            ThemeRow theme2 = themeRow("테마2", "www.test2.com", "테스트 테마2입니다.");

            ThemeRow first = dao().create(theme1);
            ThemeRow second = dao().create(theme2);

            List<ThemeRow> all = dao().findAll();

            assertThat(all).hasSize(2).contains(first, second);
        }

        @Test
        void 비어있으면_빈_리스트() {
            assertThat(dao().findAll()).isEmpty();
        }
    }

    @Nested
    @DisplayName("delete는 ")
    class Delete {

        @Test
        void 존재하는_ID는_1를_반환하고_삭제한다() {
            ThemeRow theme = themeRow("테마", "www.test.com", "테스트 테마입니다.");
            ThemeRow saved = dao().create(theme);

            int affected = dao().delete(saved.id());

            assertThat(affected).isEqualTo(1);
            assertThat(dao().findById(saved.id())).isEmpty();
        }

        @Test
        void 존재하지_않는_ID는_0을_반환한다() {
            assertThat(dao().delete(NOT_EXISTS_ID)).isZero();
        }

        @Test
        void 삭제_후_같은_조합으로_재예약_가능() {
            ThemeRow theme = themeRow("테마", "www.test.com", "테스트 테마입니다.");
            ThemeRow saved = dao().create(theme);
            dao().delete(saved.id());

            assertThatCode(() -> dao().create(theme))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("existBy는 ")
    class ExistSBy {

        @Test
        void existsById_저장된_ID는_true() {
            ThemeRow theme = themeRow("테마", "www.test.com", "테스트 테마입니다.");
            ThemeRow saved = dao().create(theme);

            assertThat(dao().existsById(saved.id())).isTrue();
        }

        @Test
        void existsById_없는_ID는_false() {
            assertThat(dao().existsById(NOT_EXISTS_ID)).isFalse();
        }

        @Test
        void existsByName_저장된_이름은_true() {
            ThemeRow theme = themeRow("테마", "www.test.com", "테스트 테마입니다.");
            ThemeRow saved = dao().create(theme);

            assertThat(dao().existsByName(saved.name())).isTrue();
        }

        @Test
        void existsByName_없는_이름은_false() {
            assertThat(dao().existsByName("없는 테마 이름")).isFalse();
        }
    }
}
