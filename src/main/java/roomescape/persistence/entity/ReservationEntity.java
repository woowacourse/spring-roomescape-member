package roomescape.persistence.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.jdbc.core.RowMapper;
import roomescape.business.domain.Reservation;

public record ReservationEntity(
        Long id,
        String name,
        String date,
        PlayTimeEntity playTimeEntity,
        ThemeEntity themeEntity
) {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final RowMapper<ReservationEntity> DEFAULT_ROW_MAPPER =
            (rs, rowNum) -> new ReservationEntity(
                    rs.getLong(1),
                    rs.getString(2),
                    rs.getString(3),
                    new PlayTimeEntity(rs.getLong(4), rs.getString(5)),
                    new ThemeEntity(rs.getLong(6), rs.getString(7), rs.getString(8), rs.getString(9))
            );

    public Reservation toDomain() {
        return Reservation.createWithId(
                id,
                name,
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
                reservation.getName(),
                DATE_FORMATTER.format(reservation.getDate()),
                PlayTimeEntity.from(reservation.getPlayTime()),
                ThemeEntity.from(reservation.getTheme())
        );
    }

    public static RowMapper<ReservationEntity> getDefaultRowMapper() {
        return DEFAULT_ROW_MAPPER;
    }
}
