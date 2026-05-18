package roomescape.domain.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.exception.ErrorCode;
import roomescape.exception.InvalidInputException;

public class Reservation {
    private static final String INVALID_RESERVATION_ID = "INVALID_RESERVATION_ID";
    private static final String INVALID_RESERVATION_DATE = "INVALID_RESERVATION_DATE";
    private static final String INVALID_RESERVATION_THEME = "INVALID_RESERVATION_THEME";
    private static final String INVALID_RESERVATION_TIME = "INVALID_RESERVATION_TIME";

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

    public Reservation withDateAndTime(final LocalDate date, final ReservationTime time) {
        return new Reservation(this.id, this.name, date, this.theme, time);
    }

    public boolean hasName(final String name) {
        return this.name.equals(name);
    }

    public boolean isPastAt(final LocalDateTime standardDateTime) {
        return getReservationDateTime().isBefore(standardDateTime);
    }

    private static void validateId(final Long id){
        if(id == null) {
            throw new InvalidInputException(INVALID_RESERVATION_ID, "Id는 비어있을 수 없습니다.");
        }
    }

    private void validate(final String name, final LocalDate date, final Theme theme, final ReservationTime time) {
        if (name == null || name.isBlank()) {
            throw new InvalidInputException(ErrorCode.RESERVATION_NAME_REQUIRED, "예약자 이름은 비어 있을 수 없습니다.");
        }

        if (name.length() >= 10) {
            throw new InvalidInputException(ErrorCode.RESERVATION_NAME_TOO_LONG, "예약자 이름은 10자 미만이어야 합니다.");
        }

        if(date == null) {
            throw new InvalidInputException(INVALID_RESERVATION_DATE, "날짜는 비어있을 수 없습니다.");
        }

        if(theme == null) {
            throw new InvalidInputException(INVALID_RESERVATION_THEME, "테마는 비어있으면 안됩니다.");
        }

        if(time == null) {
            throw new InvalidInputException(INVALID_RESERVATION_TIME, "시간은 비어있으면 안됩니다.");
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

    private LocalDateTime getReservationDateTime() {
        return LocalDateTime.of(date, time.getStartAt());
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
