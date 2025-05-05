package roomescape.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class Reservation {

    private static final int NAME_LENGTH = 10;

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    private Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static Builder builder() {
        return new Builder();
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

    public Long getTimeId() {
        if (time == null) {
            return null;
        }
        return time.id();
    }

    public Long getThemeId() {
        if (theme == null) {
            return null;
        }
        return theme.id();
    }

    public static class Builder {

        private Long id;
        private String name;
        private LocalDate date;
        private ReservationTime time;
        private Theme theme;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder time(ReservationTime time) {
            this.time = time;
            return this;
        }

        public Builder theme(Theme theme) {
            this.theme = theme;
            return this;
        }

        public Reservation build() {
            validateAll();
            return new Reservation(id, name, date, time, theme);
        }

        public Reservation buildWithDateTimeValidation() {
            validateAll();
            validateDateTime();
            return new Reservation(id, name, date, time, theme);
        }

        private void validateAll() {
            validateBasic();
            validateTheme();
        }

        private void validateBasic() {
            validateName();
            validateDate();
            validateTime();
        }

        private void validateName() {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("[ERROR] 유효하지 않은 예약자명입니다.");
            }
            if (name.length() > NAME_LENGTH) {
                throw new IllegalArgumentException("[ERROR] 예약자명의 길이가 " + NAME_LENGTH + "자를 초과할 수 없습니다.");
            }
        }

        private void validateDate() {
            if (date == null) {
                throw new IllegalArgumentException("[ERROR] 유효하지 않은 예약 날짜입니다.");
            }
        }

        private void validateTime() {
            if (time == null) {
                throw new IllegalArgumentException("[ERROR] 유효하지 않은 예약 시간입니다.");
            }
        }

        private void validateTheme() {
            if (theme == null) {
                throw new IllegalArgumentException("[ERROR] 유효하지 않은 테마입니다.");
            }
        }

        private void validateDateTime() {
            validateDate();
            validateTime();
            LocalDateTime dateTime = LocalDateTime.of(date, time.startAt());
            if (dateTime.isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("[ERROR] 예약이 불가능한 시간입니다: " + date);
            }
        }
    }

    public static Reservation createIfDateTimeValid(String name, LocalDate date, ReservationTime time, Theme theme) {
        return builder()
                .id(null)
                .name(name)
                .date(date)
                .time(time)
                .theme(theme)
                .buildWithDateTimeValidation();
    }
}
