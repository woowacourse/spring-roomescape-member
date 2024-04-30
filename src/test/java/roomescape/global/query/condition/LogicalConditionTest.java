package roomescape.global.query.condition;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class LogicalConditionTest {
    @Test
    void 논리_연산자_AND_조건을_생성한다() {
        StringBuilder builder = new StringBuilder();
        LogicalCondition logicalCondition = LogicalCondition.and();
        logicalCondition.addCondition(ComparisonCondition.equalTo("id", 1));
        logicalCondition.addCondition(ComparisonCondition.equalTo("name", "sudal"));

        logicalCondition.assemble(builder);

        assertThat(builder.toString()).isEqualTo("id = '1' AND name = 'sudal'");
    }
}
