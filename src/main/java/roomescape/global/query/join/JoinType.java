package roomescape.global.query.join;

public enum JoinType {
    INNER(" INNER JOIN "),
    LEFT(" LEFT JOIN "),
    RIGHT(" RIGHT JOIN "),
    FULL(" FULL JOIN ");

    private final String type;

    JoinType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
