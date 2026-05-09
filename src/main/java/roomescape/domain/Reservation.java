package roomescape.domain;

import org.springframework.cglib.core.Local;
import roomescape.common.exception.DomainException;
import roomescape.domain.vo.Name;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Reservation {
    private final Long id;
    private final Name name;
    private final LocalDate date;
    private final Time time;
    private final Theme theme;

    public Reservation(Long id, Name name, LocalDate date, Time time, Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static Reservation create(Name name, LocalDate date, Time time, Theme theme, LocalDateTime now) {
        validate(date, time, now);
        return new Reservation(null, name, date, time, theme);
    }

    private static void validate(LocalDate date, Time time, LocalDateTime now) {
        LocalDateTime reservationDateTime = LocalDateTime.of(date, time.getStartAt());
        if (reservationDateTime.isBefore(now)) {
            throw new DomainException("이미 지난 시각으로는 예약할 수 없습니다.");
        }

        LocalDate maxAvailableDate = now.toLocalDate().plusDays(14);
        if (date.isAfter(maxAvailableDate)) {
            throw new DomainException("예약은 현재로부터 최대 14일 이내만 가능합니다.");
        }
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Reservation that)) return false;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }
}
