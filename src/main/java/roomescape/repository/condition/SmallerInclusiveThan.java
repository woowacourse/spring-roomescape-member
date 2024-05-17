package roomescape.repository.condition;

public class SmallerInclusiveThan implements Condition {

    private final String column;

    public SmallerInclusiveThan(String column) {
        this.column = column;
    }

    @Override
    public String getConditionPhrase() {
        return column + " <= ?";
    }
}
