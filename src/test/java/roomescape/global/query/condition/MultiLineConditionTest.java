package roomescape.global.query.condition;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import roomescape.global.query.QueryBuilder;
import roomescape.global.query.SelectQuery;

class MultiLineConditionTest {
    @Test
    void exists_조건을_생성한다() {
        SelectQuery subQuery = QueryBuilder.select("users")
                .addAllColumns()
                .where(ComparisonCondition.equalTo("id", 1));
        MultiLineCondition condition = MultiLineCondition.exists(subQuery);
        StringBuilder builder = new StringBuilder();

        condition.assemble(builder);

        assertThat(builder.toString()).isEqualTo("EXISTS (SELECT * FROM users WHERE id = '1')");
    }

}
