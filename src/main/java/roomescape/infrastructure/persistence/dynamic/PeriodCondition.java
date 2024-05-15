package roomescape.infrastructure.persistence.dynamic;

class PeriodCondition implements Condition {

    private final String column;

    public PeriodCondition(String column) {
        this.column = column;
    }

    @Override
    public String getCondition() {
        return column + " BETWEEN ? AND ?";
    }
}
