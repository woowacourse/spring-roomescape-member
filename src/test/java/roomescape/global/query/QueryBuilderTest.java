package roomescape.global.query;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class QueryBuilderTest {
    private static final String TABLE_NAME = "users";

    @Test
    void select_쿼리객체를_리턴한다() {
        assertThat(QueryBuilder.select(TABLE_NAME)).isExactlyInstanceOf(SelectQuery.class);
    }

    @Test
    void delete_쿼리객체를_리턴한다() {
        assertThat(QueryBuilder.delete(TABLE_NAME)).isExactlyInstanceOf(DeleteQuery.class);
    }
}
