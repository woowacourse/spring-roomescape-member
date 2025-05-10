package roomescape.repository.member;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.Member;
import roomescape.exceptions.EntityNotFoundException;
import roomescape.repository.member.mapper.MemberRowMapper;

@Repository
public class MemberJdbcDao implements MemberRepository {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public MemberJdbcDao(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    @Override
    public Member findById(Long id) {
        String sql = "select * from member where id = :id";
        SqlParameterSource params = new MapSqlParameterSource("id", id);
        try {
            return namedJdbcTemplate.queryForObject(sql, params, MemberRowMapper.INSTANCE);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("사용자 데이터를 찾을 수 없습니다: " + id);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "select exists(select 1 from member where email = :email)";
        SqlParameterSource params = new MapSqlParameterSource("email", email);
        return Boolean.TRUE.equals(namedJdbcTemplate.queryForObject(sql, params, Boolean.class));
    }

    @Override
    public Member findByEmail(String email) {
        String sql = "select * from member where email = :email";
        SqlParameterSource params = new MapSqlParameterSource("email", email);
        try {
            return namedJdbcTemplate.queryForObject(sql, params, MemberRowMapper.INSTANCE);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("사용자 정보를 찾을 수 없습니다: " + email);
        }
    }
}
