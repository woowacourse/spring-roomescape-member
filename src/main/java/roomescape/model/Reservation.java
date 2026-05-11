package roomescape.model;

import java.time.LocalDate;

public class Reservation {
    
    private static final int MIN_RESERVATION_NAME_LENGTH = 2;
    private static final int MAX_RESERVATION_NAME_LENGTH = 20;

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
        validateName();
    }

    private void validateName() {
        if (name.length() < MIN_RESERVATION_NAME_LENGTH || name.length() > MAX_RESERVATION_NAME_LENGTH) {
            throw new IllegalArgumentException("[ERROR] 사용자 이름은 2자 이상 20자 이하입니다.");
        }
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

    public Theme getTheme() {
        return theme;
    }

}
