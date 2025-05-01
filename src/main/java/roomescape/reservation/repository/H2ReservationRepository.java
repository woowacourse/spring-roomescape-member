package roomescape.reservation.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.common.utils.JdbcUtils;
import roomescape.reservation.domain.ReserverName;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDate;
import roomescape.reservation.domain.ReservationId;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeDescription;
import roomescape.theme.domain.ThemeId;
import roomescape.theme.domain.ThemeName;
import roomescape.theme.domain.ThemeThumbnail;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationTimeId;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Map.Entry;
import static java.util.Map.entry;

@Repository
@RequiredArgsConstructor
public class H2ReservationRepository implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Reservation> reservationMapper = (resultSet, rowNum) -> {
        ReservationTime time = ReservationTime.withId(
                ReservationTimeId.from(resultSet.getLong("time_id")),
                resultSet.getObject("start_at", LocalTime.class)
        );

        Theme theme = Theme.withId(
                ThemeId.from(resultSet.getLong("theme_id")),
                ThemeName.from(resultSet.getString("theme_name")),
                ThemeDescription.from(resultSet.getString("description")),
                ThemeThumbnail.from(resultSet.getString("thumbnail"))
        );

        return Reservation.withId(
                ReservationId.from(resultSet.getLong("id")),
                ReserverName.from(resultSet.getString("name")),
                ReservationDate.from(resultSet.getObject("date", LocalDate.class)),
                time,
                theme
        );
    };

    @Override
    public boolean existsByParams(final ReservationId id) {
        final String sql = """
                select exists 
                    (select 1 from reservation where id = ?)
                """;

        return Boolean.TRUE.equals(
                jdbcTemplate.queryForObject(sql, Boolean.class, id.getValue()));
    }

    @Override
    public boolean existsByParams(final ReservationTimeId timeId) {
        final String sql = """
                select exists 
                    (select 1 from reservation where time_id = ?)
                """;

        return Boolean.TRUE.equals(
                jdbcTemplate.queryForObject(sql, Boolean.class, timeId.getValue()));
    }

    @Override
    public boolean existsByParams(final ReservationDate date,
                                  final ReservationTimeId timeId,
                                  final ThemeId themeId) {
        final String sql = """
                select exists 
                    (select 1 from reservation where date = ? and time_id = ? and theme_id = ?)
                """;

        return Boolean.TRUE.equals(
                jdbcTemplate.queryForObject(sql, Boolean.class, date.getValue(), timeId.getValue(), themeId.getValue()));

    }

    @Override
    public Optional<Reservation> findById(final ReservationId id) {
        final String sql = """
                select
                    r.id,
                    r.name,
                    r.date,
                    rt.id as time_id,
                    rt.start_at as start_at,
                    t.id as theme_id,
                    t.name as theme_name,
                    t.description as description,
                    t.thumbnail as thumbnail
                from reservation r
                join reservation_time rt
                    on r.time_id = rt.id
                join theme t
                    on r.theme_id = t.id
                where r.id = ?
                """;

        return JdbcUtils.queryForOptional(jdbcTemplate, sql, reservationMapper, id.getValue());
    }

    @Override
    public List<ReservationTimeId> findTimeIdByParams(final ReservationDate date, final ThemeId themeId) {
        final String sql = """
                select
                    r.time_id
                from reservation r
                join reservation_time rt
                    on r.time_id = rt.id
                join theme t
                    on r.theme_id = t.id
                where
                    r.date = ? and t.id = ?
                """;

        return jdbcTemplate.query(
                        sql,
                        (rs, rowNum) -> rs.getLong("time_id"),
                        date.getValue(),
                        themeId.getValue()).stream()
                .map(ReservationTimeId::from)
                .toList();
    }

    @Override
    public List<Reservation> findAll() {
        final String sql = """
                select
                    r.id,
                    r.name,
                    r.date,
                    rt.id as time_id,
                    rt.start_at as start_at,
                    t.id as theme_id,
                    t.name as theme_name,
                    t.description as description,
                    t.thumbnail as thumbnail
                from reservation r
                join reservation_time rt
                    on r.time_id = rt.id
                join theme t
                    on r.theme_id = t.id
                """;

        return jdbcTemplate.query(sql, reservationMapper).stream()
                .toList();
    }

    @Override
    public Reservation save(final Reservation reservation) {
        final String sql = "insert into reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            final PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, reservation.getName().getValue());
            preparedStatement.setDate(2, Date.valueOf(reservation.getDate().getValue()));
            preparedStatement.setLong(3, reservation.getTime().getId().getValue());
            preparedStatement.setLong(4, reservation.getTheme().getId().getValue());

            return preparedStatement;
        }, keyHolder);

        final long generatedId = Objects.requireNonNull(keyHolder.getKey()).longValue();

        return Reservation.withId(
                ReservationId.from(generatedId),
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme());
    }

    @Override
    public void deleteById(final ReservationId id) {
        final String sql = "delete from reservation where id = ?";
        jdbcTemplate.update(sql, id.getValue());
    }

    @Override
    public Map<Theme, Integer> findThemesToBookedCountByParamsOrderByBookedCount(final ReservationDate startDate,
                                                                                 final ReservationDate endDate,
                                                                                 final int count) {
        final String sql = """
                select
                    t.id,
                    t.name,
                    t.description,
                    t.thumbnail,
                    count(*) as booked_count
                from reservation r
                join theme t
                    on r.theme_id = t.id
                where
                    r.date between ? and ?
                group by 
                    t.id, t.name, t.description, t.thumbnail
                order by
                    booked_count desc  
                limit
                    ?
                """;

        return jdbcTemplate.query(
                        sql,
                        (rs, rowNum) -> {
                            final Theme theme = Theme.withId(
                                    ThemeId.from(rs.getLong("id")),
                                    ThemeName.from(rs.getString("name")),
                                    ThemeDescription.from(rs.getString("description")),
                                    ThemeThumbnail.from(rs.getString("thumbnail"))
                            );
                            final int bookedCount = rs.getInt("booked_count");
                            return entry(theme, bookedCount);
                        },
                        startDate.getValue(),
                        endDate.getValue(),
                        count)
                .stream()
                .collect(Collectors.toMap(
                        Entry::getKey,
                        Entry::getValue,
                        (v1, v2) -> v1,
                        LinkedHashMap::new
                ));
    }
}
