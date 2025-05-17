package roomescape.persistence.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.jdbc.core.RowMapper;
import roomescape.business.domain.Reservation;
import roomescape.business.domain.Role;

public record ReservationEntity(
        Long id,
        UserEntity userEntity,
        String date,
        PlayTimeEntity playTimeEntity,
        ThemeEntity themeEntity
) {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final RowMapper<ReservationEntity> DEFAULT_ROW_MAPPER =
            (rs, rowNum) -> new ReservationEntity(
                    rs.getLong("reservation_id"),
                    new UserEntity(
                            rs.getLong("user_id"),
                            rs.getString("user_name"),
                            rs.getString("user_email"),
                            rs.getString("user_password"),
                            Role.valueOf(rs.getString("user_role"))
                    ),
                    rs.getString("date"),
                    new PlayTimeEntity(
                            rs.getLong("time_id"),
                            rs.getString("time_value")
                    ),
                    new ThemeEntity(
                            rs.getLong("theme_id"),
                            rs.getString("theme_name"),
                            rs.getString("theme_description"),
                            rs.getString("theme_thumbnail")
                    )
            );

    public Reservation toDomain() {
        return Reservation.createWithId(
                id,
                userEntity.toDomain(),
                LocalDate.parse(date, DATE_FORMATTER),
                playTimeEntity.toDomain(),
                themeEntity.toDomain()
        );
    }

    public static String formatDate(final LocalDate date) {
        return DATE_FORMATTER.format(date);
    }

    public static ReservationEntity from(final Reservation reservation) {
        return new ReservationEntity(
                reservation.getId(),
                UserEntity.from(reservation.getUser()),
                DATE_FORMATTER.format(reservation.getDate()),
                PlayTimeEntity.from(reservation.getPlayTime()),
                ThemeEntity.from(reservation.getTheme())
        );
    }

    public static RowMapper<ReservationEntity> getDefaultRowMapper() {
        return DEFAULT_ROW_MAPPER;
    }
}
