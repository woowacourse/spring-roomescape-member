package roomescape.global.query.join;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class JoinConditionTest {
    @Test
    void on절을_생성한다() {
        StringBuilder builder = new StringBuilder();
        JoinCondition joinCondition = JoinCondition.on("u.id", "o.user_id");

        joinCondition.assemble(builder);

        assertThat(builder.toString()).isEqualTo(" ON u.id = o.user_id");
    }
}
