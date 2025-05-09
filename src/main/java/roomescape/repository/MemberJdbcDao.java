package roomescape.repository;

import java.util.List;
import java.util.Objects;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.entity.Member;
import roomescape.exceptions.EntityNotFoundException;

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
            return namedJdbcTemplate.queryForObject(sql, params, getThemeRowMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("사용자 데이터를 찾을 수 없습니다: " + id);
        }
    }

    @Override
    public List<Member> findAll() {
        String sql = "select * from member order by id;";
        return namedJdbcTemplate.query(sql, getThemeRowMapper());
    }

    @Override
    public Member save(Member member) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into member (name,email,password) values (:name,:email,:password)";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", member.getName())
                .addValue("email", member.getEmail())
                .addValue("password", member.getPassword());

        namedJdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new Member(id, member.getName(), member.getEmail(), member.getPassword());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "delete from member where id = :id";
        SqlParameterSource params = new MapSqlParameterSource("id", id);
        int result = namedJdbcTemplate.update(sql, params);

        if (result == 0) {
            throw new EntityNotFoundException("사용자 데이터를 찾을 수 없습니다:" + id);
        }
    }

    @Override
    public boolean existsByName(String name) {
        String sql = "select count(*) from member where name = :name";
        SqlParameterSource params = new MapSqlParameterSource("name", name);
        Integer count = namedJdbcTemplate.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }

    private RowMapper<Member> getThemeRowMapper() {
        return (resultSet, rowNum) -> new Member(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("password")
        );
    }
}
