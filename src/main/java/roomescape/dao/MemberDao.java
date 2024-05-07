package roomescape.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Member findByEmailAndPassword(String email, String password) {
        String selectSQL = """
               SELECT
                   m.ID as member_id,
                   m.NAME as member_name,
                   m.EMAIL as member_email,
                   m.PASSWORD as member_password
               FROM member AS m
               WHERE m.EMAIL = ? and m.PASSWORD = ?
               """;

        return jdbcTemplate.queryForObject(selectSQL, (rs, rowNum) -> new Member(
                rs.getLong("member_id"),
                rs.getString("member_name"),
                rs.getString("member_email"),
                rs.getString("member_password")
        ), email, password);
    }

    public Member findById(Long id) {
        String selectSQL = """
               SELECT
                   m.ID as member_id,
                   m.NAME as member_name,
                   m.EMAIL as member_email,
                   m.PASSWORD as member_password
               FROM member AS m
               WHERE m.ID = ?
               """;

        return jdbcTemplate.queryForObject(selectSQL, (rs, rowNum) -> new Member(
                rs.getLong("member_id"),
                rs.getString("member_name"),
                rs.getString("member_email"),
                rs.getString("member_password")
        ), id);
    }
}
