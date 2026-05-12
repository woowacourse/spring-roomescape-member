package roomescape.dao.row;

import roomescape.domain.Time;

import java.time.LocalTime;

public record TimeRow(Long id,
                      LocalTime startAt) implements DomainConvertible<Time> {

    public TimeRow(LocalTime startAt) {
        this(null, startAt);
    }

    public static TimeRow from(Time time) {
        return new TimeRow(time.getId(), time.getStartAt());
    }

    @Override
    public Time toDomain() {
        return new Time(id, startAt);
    }
}
