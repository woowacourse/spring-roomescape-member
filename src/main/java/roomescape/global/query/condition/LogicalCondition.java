package roomescape.global.query.condition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LogicalCondition extends Condition {
    private final Operator operator;
    private final List<Condition> conditions;

    private LogicalCondition(Operator operator) {
        this.operator = operator;
        this.conditions = new ArrayList<>();
    }

    public static LogicalCondition and() {
        return new LogicalCondition(Operator.AND);
    }

    public void addCondition(Condition condition) {
        conditions.add(condition);
    }

    public boolean isEmpty() {
        return conditions.isEmpty();
    }

    @Override
    public void assemble(StringBuilder builder) {
        Iterator<Condition> conditionIterator = conditions.iterator();
        while (conditionIterator.hasNext()) {
            conditionIterator.next().assemble(builder);
            appendOperator(builder, conditionIterator.hasNext());
        }
    }

    private void appendOperator(StringBuilder builder, boolean hasNext) {
        if (hasNext) {
            builder.append(operator);
        }
    }

    private enum Operator {
        AND(" AND "),
        OR(" OR ");

        private final String value;

        Operator(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }
}
