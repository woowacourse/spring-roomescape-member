package roomescape.global.query;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import roomescape.global.query.condition.ComparisonCondition;

class DeleteQueryTest {
    @Test
    void delete_쿼리를_생성한다() {
        DeleteQuery deleteQuery = new DeleteQuery("users");

        assertThat(deleteQuery.build()).isEqualTo("DELETE FROM users");
    }

    @Test
    void WHERE_절을_추가한다() {
        DeleteQuery deleteQuery = new DeleteQuery("users")
                .where(ComparisonCondition.equalTo("id", 1));

        assertThat(deleteQuery.build()).isEqualTo("DELETE FROM users WHERE id = '1'");
    }
}
