package roomescape.repository;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.MemberRole;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private static final int SUCCESS_COUNT = 1;

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert insert;
    private final RowMapper<Member> memberRowMapper;

    public MemberRepositoryImpl(final DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource).withTableName("member").usingGeneratedKeyColumns("id");
        this.memberRowMapper = (rs, rowNum) -> new Member(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password"),
                MemberRole.valueOf(rs.getString("role")));
    }

    @Override
    public Optional<Member> findById(final Long memberId) {
        String sql = "select * from member where id=:id";
        try {
            SqlParameterSource param = new MapSqlParameterSource("id", memberId);
            Member member = template.queryForObject(sql, param, memberRowMapper);
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findByEmail(final String email) {
        String sql = "select * from member where email=:email";
        try {
            SqlParameterSource param = new MapSqlParameterSource("email", email);
            Member member = template.queryForObject(sql, param, memberRowMapper);
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Member> findAll() {
        String sql = "select * from member";
        return template.query(sql, memberRowMapper);
    }

    @Override
    public Member save(final Member member) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("name", member.getName())
                .addValue("email", member.getEmail())
                .addValue("password", member.getPassword())
                .addValue("role", member.getRole());
        Number key = insert.executeAndReturnKey(param);
        return member.assignId(key.longValue());
    }

    @Override
    public boolean deleteById(final Long id) {
        String sql = "delete from member where id=:id";
        SqlParameterSource param = new MapSqlParameterSource("id", id);
        return template.update(sql, param) == SUCCESS_COUNT;
    }

}
