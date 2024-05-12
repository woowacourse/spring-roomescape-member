package roomescape.auth.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.auth.domain.Member;

@Repository
public class H2MemberRepository implements MemberRepository {

  private final NamedParameterJdbcTemplate template;

  public H2MemberRepository(final NamedParameterJdbcTemplate template) {
    this.template = template;
  }

  private RowMapper<Member> itemRowMapper() {
    return ((rs, rowNum) -> Member.createInstance(
        rs.getLong("id"),
        rs.getString("name"),
        rs.getString("email"),
        rs.getString("password"),
        rs.getString("role_name")
    ));
  }

  @Override
  public Optional<Member> findByEmail(String email) {
    final String sql = """
        SELECT m.id, m.name, m.email, m.password, r.name as role_name
        FROM member as m 
        INNER JOIN role as r 
        ON m.role_id = r.id
        WHERE email = :email
        """;

    try {
      final MapSqlParameterSource param = new MapSqlParameterSource()
          .addValue("email", email);
      final Member member = template.queryForObject(sql, param, itemRowMapper());

      return Optional.of(member);
    } catch (final EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public List<Member> findAll() {
    final String sql = """
        SELECT m.id, m.name, m.email, m.password, r.name as role_name
        FROM member as m 
        INNER JOIN role as r 
        ON m.role_id = r.id
        """;

    return template.query(sql, itemRowMapper());
  }
}
