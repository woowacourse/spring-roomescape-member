package roomescape.repository.condition;

public class BiggerInclusiveThan implements Condition {

    private final String column;

    public BiggerInclusiveThan(String column) {
        this.column = column;
    }

    @Override
    public String getConditionPhrase() {
        return column + " >= ?";
    }
}
