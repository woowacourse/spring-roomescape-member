package roomescape.infrastructure.persistence.dynamic;

class SimpleCondition implements Condition {

    private final String column;

    public SimpleCondition(String column) {
        this.column = column;
    }

    @Override
    public String getCondition() {
        return column + " = ?";
    }
}
