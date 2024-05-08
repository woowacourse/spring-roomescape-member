package roomescape.dao;

import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Member> memberRowMapper = (rs, rowNum) -> new Member(
            rs.getLong("member_id"),
            rs.getString("member_name"),
            rs.getString("member_email"),
            rs.getString("member_password"));

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Member> findByEmailAndPassword(String email, String password) {
        String selectSQL = """
               SELECT
                   m.ID as member_id,
                   m.NAME as member_name,
                   m.EMAIL as member_email,
                   m.PASSWORD as member_password
               FROM member AS m
               WHERE m.EMAIL = ? and m.PASSWORD = ?
               """;

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(selectSQL, memberRowMapper, email, password));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<Member> findById(Long id) {
        String selectSQL = """
               SELECT
                   m.ID as member_id,
                   m.NAME as member_name,
                   m.EMAIL as member_email,
                   m.PASSWORD as member_password
               FROM member AS m
               WHERE m.ID = ?
               """;

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(selectSQL, memberRowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}
