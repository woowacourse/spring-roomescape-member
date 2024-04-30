package roomescape.global.query.condition;

import roomescape.global.query.SelectQuery;

public class MultiLineCondition extends Condition {
    private final Operator operator;
    private final SelectQuery subQuery;

    private MultiLineCondition(Operator operator, SelectQuery subQuery) {
        this.operator = operator;
        this.subQuery = subQuery;
    }

    public static MultiLineCondition exists(SelectQuery subQuery) {
        return new MultiLineCondition(Operator.EXISTS, subQuery);
    }

    @Override
    public void assemble(StringBuilder builder) {
        builder.append(operator)
                .append("(")
                .append(subQuery.build())
                .append(")");
    }

    private enum Operator {
        EXISTS("EXISTS "),
        NOT_EXISTS("NOT EXISTS "),
        SOME("SOME "),
        ALL("ALL "),
        UNIQUE("UNIQUE ");

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
