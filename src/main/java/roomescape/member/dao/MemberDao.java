package roomescape.member.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

// TODO: Test 작성
@Repository
public class MemberDao {

    private final static RowMapper<Member> ROW_MAPPER = (resultSet, rowNum) -> new Member(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("email"),
            resultSet.getString("password")
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public MemberDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    public Member insert(final Member member) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", member.getName())
                .addValue("email", member.getEmail())
                .addValue("password", member.getPassword());
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return new Member(id, member);
    }

    public List<Member> findAll() {
        String sql = "SELECT * FROM member";

        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    public Optional<Member> findById(final Long id) {
        String sql = "SELECT * FROM member WHERE id = ?";

        try {
            Member member = jdbcTemplate.queryForObject(sql, ROW_MAPPER, id);
            return Optional.of(member);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    // WHY: 데이터 조회 시, 데이터가 존재하지 않는 부분에 대한 예외를 DAO에서 처리해주게 된다면, DAO가 여러 종류일 때 DB 벤더에 따른 예외처리를 각 DAO에 중복 작성해주어야 하기 때문에
    // Optional로 return하고 Service 레이어에서 관련 처리를 수행하도록 작성하여
    public Optional<Member> findByEmailAndPassword(final String email, final String password) {
        String sql = "SELECT * FROM member WHERE email = ? AND password = ?";

        try {
            Member member = jdbcTemplate.queryForObject(sql, ROW_MAPPER, email, password);
            return Optional.of(member);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}
