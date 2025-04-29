package roomescape.entity;

import java.time.LocalDate;
import java.time.Period;
import roomescape.exception.impl.NameContainsNumberException;
import roomescape.exception.impl.OverMaxNameLengthException;
import roomescape.exception.impl.PastDateException;
import roomescape.exception.impl.ReservationBeforeStartException;
import roomescape.exception.impl.ReservationTimeNotFoundException;

public class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;

    private Reservation(Long id, String name, LocalDate date, ReservationTime time) {
        if (name.length() > 10) {
            throw new OverMaxNameLengthException();
        }
        for (char c : name.toCharArray()) {
            if (Character.isDigit(c)) {
                throw new NameContainsNumberException();
            }
        }
        if (time == null) {
            throw new ReservationTimeNotFoundException();
        }
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public static Reservation beforeSave(final String name, final LocalDate date, final ReservationTime time) {
        if (date.isBefore(LocalDate.now())) {
            throw new PastDateException();
        }
        long minusDays = Period.between(date, LocalDate.now()).getDays();
        if (minusDays > 7) {
            throw new ReservationBeforeStartException();
        }
        return new Reservation(null, name, date, time);
    }

    public static Reservation afterSave(Long id, String name, LocalDate date, ReservationTime time) {
        return new Reservation(id, name, date, time);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }
}
