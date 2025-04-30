package roomescape.persistence.entity;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.springframework.jdbc.core.RowMapper;
import roomescape.business.domain.PlayTime;

public record PlayTimeEntity(Long id, String startAt) {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final RowMapper<PlayTimeEntity> DEFAULT_ROW_MAPPER =
            (rs, rowNum) -> new PlayTimeEntity(
                    rs.getLong(1),
                    rs.getString(2));

    public PlayTime toDomain() {
        return PlayTime.createWithId(
                id,
                LocalTime.parse(startAt, TIME_FORMATTER)
        );
    }

    public static PlayTimeEntity from(final PlayTime playTime) {
        return new PlayTimeEntity(
                playTime.getId(),
                TIME_FORMATTER.format(playTime.getStartAt())
        );
    }

    public static RowMapper<PlayTimeEntity> getDefaultRowMapper() {
        return DEFAULT_ROW_MAPPER;
    }
}
