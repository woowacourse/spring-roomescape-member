package roomescape.global.query.condition;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ComparisonConditionTest {
    @Test
    void 동등_연산자_조건을_생성한다() {
        StringBuilder builder = new StringBuilder();
        ComparisonCondition condition = ComparisonCondition.equalTo("id", 1);

        condition.assemble(builder);

        assertThat(builder.toString()).isEqualTo("id = '1'");
    }
}
