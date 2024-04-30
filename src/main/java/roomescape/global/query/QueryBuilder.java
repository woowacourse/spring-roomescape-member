package roomescape.global.query;

public class QueryBuilder {
    private QueryBuilder() {
    }

    public static SelectQuery select(String tableName) {
        return new SelectQuery(tableName);
    }

    public static DeleteQuery delete(String tableName) {
        return new DeleteQuery(tableName);
    }
}
