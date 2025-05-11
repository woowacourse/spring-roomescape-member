package roomescape.reservation.infrastructure;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SqlBuilderTest {

    @Test
    @DisplayName("초기 SQL 문자열을 통해 SqlBuilder를 생성할 수 있다")
    void createBuilder_withBaseSql() {
        SqlBuilder builder = new SqlBuilder("SELECT * FROM table WHERE 1=1");

        assertThat(builder.getSql()).isEqualTo("SELECT * FROM table WHERE 1=1");
        assertThat(builder.getParams()).isEmpty();
    }

    @Test
    @DisplayName("값이 null이 아닌 경우 AND 절과 파라미터가 추가된다")
    void appendAnd_whenValueIsNotNull() {
        SqlBuilder builder = new SqlBuilder("SELECT * FROM table WHERE 1=1");

        builder.and("col = :val", "val", "abc");

        assertThat(builder.getSql()).contains("AND col = :val");
        assertThat(builder.getParams()).containsEntry("val", "abc");
    }

    @Test
    @DisplayName("값이 null인 경우 AND 절과 파라미터가 추가되지 않는다")
    void skipAnd_whenValueIsNull() {
        SqlBuilder builder = new SqlBuilder("SELECT * FROM table WHERE 1=1");

        builder.and("col = :val", "val", null);

        assertThat(builder.getSql()).isEqualTo("SELECT * FROM table WHERE 1=1");
        assertThat(builder.getParams()).isEmpty();
    }

    @Test
    @DisplayName("여러 개의 조건을 순차적으로 추가하면 모두 반영된다")
    void appendMultipleConditions() {
        SqlBuilder builder = new SqlBuilder("SELECT * FROM table WHERE 1=1");

        builder.and("a = :a", "a", 1)
                .and("b = :b", "b", 2)
                .and("c = :c", "c", 3);

        assertThat(builder.getSql())
                .contains("AND a = :a")
                .contains("AND b = :b")
                .contains("AND c = :c");

        assertThat(builder.getParams())
                .containsExactly(
                        entry("a", 1),
                        entry("b", 2),
                        entry("c", 3)
                );
    }

    @Test
    @DisplayName("값이 null인 조건은 제외하고 나머지만 추가된다")
    void appendOnlyNonNullConditions() {
        SqlBuilder builder = new SqlBuilder("SELECT * FROM table WHERE 1=1");

        builder.and("a = :a", "a", null)
                .and("b = :b", "b", 2);

        assertThat(builder.getSql()).contains("AND b = :b");
        assertThat(builder.getSql()).doesNotContain("a = :a");
        assertThat(builder.getParams())
                .containsExactly(entry("b", 2));
    }
}
