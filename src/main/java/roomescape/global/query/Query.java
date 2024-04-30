package roomescape.global.query;

import java.util.Objects;

public abstract class Query implements Assemblable {
    protected final String table;

    protected Query(String table) {
        Objects.requireNonNull(table, "테이블명은 필수입니다.");
        this.table = table;
    }

    public String build() {
        StringBuilder builder = new StringBuilder();
        assemble(builder);
        return builder.toString();
    }
}
