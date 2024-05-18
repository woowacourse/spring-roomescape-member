package roomescape.repository.condition;

public class EqualsTo implements Condition {

    private final String column;

    public EqualsTo(String column) {
        this.column = column;
    }

    @Override
    public String getConditionPhrase() {
        return column + " = ?";
    }
}
