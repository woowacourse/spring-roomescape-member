package roomescape.global.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import roomescape.global.query.condition.Condition;
import roomescape.global.query.condition.LogicalCondition;
import roomescape.global.query.join.JoinCondition;
import roomescape.global.query.join.JoinType;

public class SelectQuery extends Query {
    private static final String ALL_COLUMNS = "*";

    private final List<String> columns;
    private final LogicalCondition condition;
    private Join join = Join.EMPTY;
    private Alias alias = Alias.EMPTY;

    protected SelectQuery(String table) {
        super(table);
        this.columns = new ArrayList<>();
        this.condition = LogicalCondition.and();
    }

    public SelectQuery addColumns(String... columnNames) {
        this.columns.addAll(Arrays.asList(columnNames));
        return this;
    }

    public SelectQuery addAllColumns() {
        this.columns.add(ALL_COLUMNS);
        return this;
    }

    public SelectQuery where(Condition condition) {
        this.condition.addCondition(condition);
        return this;
    }

    public SelectQuery alias(String aliasName) {
        if (aliasName == null || aliasName.isBlank()) {
            throw new IllegalArgumentException("alias가 비어있습니다.");
        }
        this.alias = new Alias(aliasName);
        return this;
    }

    public SelectQuery join(JoinType joinType, String joinTable, JoinCondition joinCondition, String alias) {
        this.join = new Join(joinType, joinTable, joinCondition, new Alias(alias));
        return this;
    }

    @Override
    public void assemble(StringBuilder builder) {
        if (columns.isEmpty()) {
            throw new IllegalArgumentException("지정된 컬럼이 없습니다.");
        }
        builder.append("SELECT ").append(String.join(", ", columns)).append(" FROM ").append(table);
        alias.assemble(builder);
        join.assemble(builder);
        if (!condition.isEmpty()) {
            builder.append(" WHERE ");
            condition.assemble(builder);
        }
    }

    private static class Join implements Assemblable {
        private static final Join EMPTY = new Join(null, null, null) {
            @Override
            public void assemble(StringBuilder builder) {
            }
        };

        private final JoinType joinType;
        private final String joinTable;
        private final JoinCondition condition;
        private final Alias alias;

        public Join(JoinType joinType, String joinTable, JoinCondition condition) {
            this(joinType, joinTable, condition, Alias.EMPTY);
        }

        public Join(JoinType joinType, String joinTable, JoinCondition condition, Alias alias) {
            this.joinType = joinType;
            this.joinTable = joinTable;
            this.condition = condition;
            this.alias = alias;
        }

        @Override
        public void assemble(StringBuilder builder) {
            builder.append(joinType).append(joinTable);
            alias.assemble(builder);
            condition.assemble(builder);
        }
    }

    private static class Alias implements Assemblable {
        private static final Alias EMPTY = new Alias(null) {
            @Override
            public void assemble(StringBuilder builder) {
            }
        };

        private final String alias;

        public Alias(String alias) {
            this.alias = alias;
        }

        @Override
        public void assemble(StringBuilder builder) {
            builder.append(" AS ").append(alias);
        }
    }
}
