package roomescape.repository.member;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.Email;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberName;
import roomescape.domain.member.Password;

@Repository
public class MemberH2Repository implements MemberRepository {

    private static final String TABLE_NAME = "MEMBER";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public MemberH2Repository(JdbcTemplate jdbcTemplate, DataSource source) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(source)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Member save(Member member) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(member);

        Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return new Member(id, member);
    }

    @Override
    public boolean doesEmailExist(Email email) {
        String sql = "SELECT * FROM member WHERE email = ?";
        return !jdbcTemplate.query(sql, (rs, rowNum) -> 0, email.email()).isEmpty();
    }

    @Override
    public Optional<Member> findById(Long id) {
        try {
            Member member = jdbcTemplate.queryForObject(
                    "SELECT * FROM member WHERE id = ?",
                    getMemberRowMapper(),
                    id
            );
            return Optional.ofNullable(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private RowMapper<Member> getMemberRowMapper() {
        return (resultSet, rowNum) -> new Member(
                resultSet.getLong("id"),
                new MemberName(resultSet.getString("name")),
                new Email(resultSet.getString("email")),
                new Password(resultSet.getString("password"))
        );
    }

    @Override
    public Optional<Member> findByEmailAndPassword(Email email, Password password) {
        try {
            Member member = jdbcTemplate.queryForObject(
                    "SELECT * FROM member WHERE email = ? AND password = ?",
                    getMemberRowMapper(),
                    email.email(),
                    password.password()
            );
            return Optional.ofNullable(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Member> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM member",
                getMemberRowMapper()
        );
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM member WHERE id = ?", id);
    }
}
