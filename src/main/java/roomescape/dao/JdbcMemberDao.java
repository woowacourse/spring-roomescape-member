package roomescape.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberEmail;
import roomescape.domain.member.MemberName;
import roomescape.domain.member.MemberPassword;
import roomescape.domain.member.MemberRole;

@Repository
public class JdbcMemberDao implements MemberDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcMemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Member> readAll() {
        String sql = """
                SELECT
                id, name, email, password, role
                FROM
                member
                """;

        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> getMember(resultSet)
        );
    }

    @Override
    public Optional<Member> findById(long id) {
        String sql = """
                SELECT
                id, name, email, password, role
                FROM
                member
                WHERE id = ?
                """;
        List<Member> members = jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> getMember(resultSet),
                id
        );
        if (members.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(members.get(0));
    }

    @Override
    public Optional<Member> findByEmail(MemberEmail email) {
        String sql = """
                SELECT
                id, name, email, password, role
                FROM
                member
                WHERE email = ?;
                """;

        List<Member> members = jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> getMember(resultSet),
                email.getValue()
        );

        if (members.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(members.get(0));
    }

    @Override
    public Member create(Member member) {
        String sql = """
                INSERT
                INTO member
                (name, email, password, role)
                VALUES
                (?, ?, ?, ?);
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, member.getName().getValue());
                    ps.setString(2, member.getEmail().getValue());
                    ps.setString(3, member.getPassword().getValue());
                    ps.setString(4, member.getMemberRole().getName());
                    return ps;
                },
                keyHolder
        );
        long id = keyHolder.getKey().longValue();
        return new Member(id, member.getName(), member.getEmail(), member.getPassword(), member.getMemberRole());
    }

    @Override
    public boolean existByEmail(MemberEmail memberEmail) {
        String sql = """
                SELECT
                CASE 
                    WHEN EXISTS (SELECT 1 FROM member WHERE email = ?)
                    THEN TRUE
                    ELSE FALSE
                END;
                """;
        return jdbcTemplate.queryForObject(sql, boolean.class, memberEmail.getValue());
    }

    @Override
    public boolean existByEmailAndMemberPassword(MemberEmail memberEmail, MemberPassword memberPassword) {
        String sql = """
                SELECT
                CASE 
                    WHEN EXISTS (SELECT 1 FROM member WHERE email = ? AND password = ?)
                    THEN TRUE
                    ELSE FALSE
                END;
                """;
        return jdbcTemplate.queryForObject(
                sql,
                boolean.class,
                memberEmail.getValue(),
                memberPassword.getValue()
        );
    }

    private Member getMember(ResultSet resultSet) throws SQLException {
        return new Member(
                resultSet.getLong("id"),
                new MemberName(resultSet.getString("name")),
                new MemberEmail(resultSet.getString("email")),
                new MemberPassword(resultSet.getString("password")),
                MemberRole.from(resultSet.getString("role"))
        );
    }
}
