package roomescape.persistence.entity;

import org.springframework.jdbc.core.RowMapper;
import roomescape.business.domain.Role;
import roomescape.business.domain.User;

public record UserEntity(
        Long id,
        String name,
        String email,
        String password,
        Role role
) {

    private static final RowMapper<UserEntity> DEFAULT_ROW_MAPPER =
            (rs, rowNum) -> new UserEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    Role.valueOf(rs.getString("role"))
            );

    public User toDomain() {
        return User.createWithId(
                id,
                name,
                email,
                password,
                role
        );
    }

    public static UserEntity from(final User user) {
        return new UserEntity(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getRole()
        );
    }

    public static RowMapper<UserEntity> getDefaultRowMapper() {
        return DEFAULT_ROW_MAPPER;
    }
}
