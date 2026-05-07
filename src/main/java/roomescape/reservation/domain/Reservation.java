package roomescape.reservation.domain;

import java.time.LocalDate;
import java.util.Objects;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

public class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final Theme theme;
    private final ReservationTime time;

    private Reservation(final Long id, final String name, final LocalDate date, final Theme theme, final ReservationTime time) {
        validate(name, date, theme, time);
        this.id = id;
        this.name = name;
        this.date = date;
        this.theme = theme;
        this.time = time;
    }

    public static Reservation createNew(final String name, final LocalDate date, final Theme theme, final ReservationTime time) {
        return new Reservation(null, name, date, theme, time);
    }

    public static Reservation of(final Long id, final String name, final LocalDate date, final Theme theme, final ReservationTime time) {
        validateId(id);
        return new Reservation(id, name, date, theme, time);
    }

    public Reservation withId(final Long id) {
        validateId(id);
        return new Reservation(id, this.name, this.date, this.theme, this.time);
    }

    private static void validateId(final Long id){
        if(id == null) {
            throw new IllegalArgumentException("[ERROR] Id는 비어있을 수 없습니다.");
        }
    }

    private void validate(final String name, final LocalDate date, final Theme theme, final ReservationTime time) {
        if (name == null || name.length() >= 10 || name.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 잘못된 이름 입력입니다.");
        }

        if(date == null) {
            throw new IllegalArgumentException("[ERROR] 날짜는 비어있을 수 없습니다.");
        }

        if(theme == null) {
            throw new IllegalArgumentException("[ERROR] 테마는 비어있으면 안됩니다.");
        }

        if(time == null) {
            throw new IllegalArgumentException("[ERROR] 시간은 비어있으면 안됩니다.");
        }
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Theme getTheme() {
        return this.theme;
    }

    public ReservationTime getTime() {
        return this.time;
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Reservation)) {
            return false;
        }
        Reservation r = (Reservation) o;
        return Objects.equals(id, r.getId());
    }

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }
}
