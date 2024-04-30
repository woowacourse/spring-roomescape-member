package roomescape.global.query.condition;

public class ComparisonCondition extends Condition {
    private final String column;
    private final Object value;
    private final Operator operator;
    private final boolean usingSingleQuotes;

    private ComparisonCondition(String column, Object value, Operator operator, boolean usingSingleQuotes) {
        this.column = column;
        this.value = value;
        this.operator = operator;
        this.usingSingleQuotes = usingSingleQuotes;
    }

    public static ComparisonCondition equalTo(String column, Object value) {
        return equalTo(column, value, true);
    }

    public static ComparisonCondition equalTo(String column, Object value, boolean usingSingleQuotes) {
        return new ComparisonCondition(column, value, Operator.EQUAL_TO, usingSingleQuotes);
    }

    @Override
    public void assemble(StringBuilder builder) {
        builder.append(column)
                .append(operator)
                .append(value());
    }

    private String value() {
        if (usingSingleQuotes) {
            return "'" + value + "'";
        }
        return value.toString();
    }

    private enum Operator {
        EQUAL_TO(" = "),
        NOT_EQUAL_TO(" != "),
        GREATER_THAN(" > "),
        LESS_THAN(" < ");

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
