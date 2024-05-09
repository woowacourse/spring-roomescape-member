package roomescape.repository;

import java.sql.PreparedStatement;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;

@Repository
public class JdbcMemberRepository implements MemberRepository {

    private static final RowMapper<Member> MEMBER_ROW_MAPPER = (rs, rowNum) -> new Member(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("password")
    );
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Optional<Member> findById(long id) {
        try {
            Member findMember = jdbcTemplate.queryForObject(
                    "SELECT id, name, email, password FROM MEMBER WHERE id = ?",
                    MEMBER_ROW_MAPPER,
                    id
            );
            return Optional.of(findMember);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        try {
            Member findMember = jdbcTemplate.queryForObject(
                    "SELECT id, name, email, password FROM MEMBER WHERE email = ?",
                    MEMBER_ROW_MAPPER,
                    email
            );
            return Optional.of(findMember);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Member save(Member member) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update((con -> {
            PreparedStatement preparedStatement = con.prepareStatement(
                    "INSERT INTO MEMBER (NAME, EMAIL, PASSWORD) VALUES (?,?,?)",
                    new String[]{"id"}
            );
            preparedStatement.setString(1, member.getName());
            preparedStatement.setString(2, member.getEmail());
            preparedStatement.setString(3, member.getPassword());
            return preparedStatement;
        }), keyHolder);

        long savedId = keyHolder.getKey().longValue();
        return findById(savedId).get();
    }
}
