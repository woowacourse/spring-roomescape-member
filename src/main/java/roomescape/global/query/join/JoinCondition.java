package roomescape.global.query.join;

import roomescape.global.query.Assemblable;
import roomescape.global.query.condition.ComparisonCondition;

public class JoinCondition implements Assemblable {
    private final ComparisonCondition condition;

    private JoinCondition(ComparisonCondition condition) {
        this.condition = condition;
    }

    public static JoinCondition on(String column, Object value) {
        return new JoinCondition(ComparisonCondition.equalTo(column, value, false));
    }

    @Override
    public void assemble(StringBuilder builder) {
        builder.append(" ON ");
        condition.assemble(builder);
    }
}
