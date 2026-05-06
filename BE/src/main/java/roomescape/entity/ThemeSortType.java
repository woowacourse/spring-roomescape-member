package roomescape.entity;

public enum ThemeSortType {
    POPULAR("COUNT(r.id) DESC");

    final String sql;

    ThemeSortType(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }
}
