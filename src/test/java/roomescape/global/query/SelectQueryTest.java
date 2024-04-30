package roomescape.global.query;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import roomescape.global.query.condition.ComparisonCondition;
import roomescape.global.query.join.JoinCondition;
import roomescape.global.query.join.JoinType;

class SelectQueryTest {
    @Test
    void 컬럼을_추가한다() {
        SelectQuery selectQuery = new SelectQuery("users")
                .addColumns("id", "name");

        assertThat(selectQuery.build()).isEqualTo("SELECT id, name FROM users");
    }

    @Test
    void 모든_컬럼을_추가한다() {
        SelectQuery selectQuery = new SelectQuery("users")
                .addAllColumns();

        assertThat(selectQuery.build()).isEqualTo("SELECT * FROM users");
    }

    @Test
    void WHERE_절을_추가한다() {
        SelectQuery selectQuery = new SelectQuery("users")
                .addColumns("id", "name")
                .where(ComparisonCondition.equalTo("id", 1));

        assertThat(selectQuery.build()).isEqualTo("SELECT id, name FROM users WHERE id = '1'");
    }

    @Test
    void alias를_사용한다() {
        SelectQuery selectQuery = new SelectQuery("users")
                .alias("u")
                .addColumns("u.id", "u.name");

        assertThat(selectQuery.build()).isEqualTo("SELECT u.id, u.name FROM users AS u");
    }

    @Test
    void 모든_컬럼을_조회하는_JOIN_절을_추가한다() {
        SelectQuery selectQuery = new SelectQuery("users")
                .alias("u")
                .addAllColumns()
                .join(JoinType.INNER, "orders", JoinCondition.on("u.id", "o.user_id"), "o");

        assertThat(selectQuery.build())
                .isEqualTo("SELECT * FROM users AS u INNER JOIN orders AS o ON u.id = o.user_id");
    }

    @Test
    void join_테이블의_컬럼도_조회하는_JOIN_절을_추가한다() {
        SelectQuery selectQuery = new SelectQuery("users")
                .alias("u")
                .addColumns("u.id", "u.name", "o.price")
                .join(JoinType.INNER, "orders", JoinCondition.on("u.id", "o.user_id"), "o");

        assertThat(selectQuery.build())
                .isEqualTo("SELECT u.id, u.name, o.price FROM users AS u INNER JOIN orders AS o ON u.id = o.user_id");
    }

    @Test
    void WHERE_절과_JOIN_절을_추가한다() {
        SelectQuery selectQuery = new SelectQuery("users")
                .alias("u")
                .addAllColumns()
                .join(JoinType.INNER, "orders", JoinCondition.on("u.id", "o.user_id"), "o")
                .where(ComparisonCondition.equalTo("u.id", 1));

        assertThat(selectQuery.build())
                .isEqualTo("SELECT * FROM users AS u INNER JOIN orders AS o ON u.id = o.user_id WHERE u.id = '1'");
    }
}
