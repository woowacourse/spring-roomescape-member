package roomescape.domain;

import java.time.LocalDate;

public class Reservation {

    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 10;
    private static final String NAME_PATTERN = "^[a-zA-Z가-힣]+$";

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Long themeId;

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, Long themeId) {
        validateName(name);
        validateDate(date);
        validateTime(time);
        validateThemeId(themeId);

        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.themeId = themeId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ReservationTime getTime() {
        return time;
    }

    public LocalDate getDate() {
        return date;
    }

    public Long getThemeId() {
        return themeId;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("예약자 이름은 비어 있을 수 없습니다.");
        }

        if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("예약자 이름은 2자 이상 10자 이하여야 합니다.");
        }

        if (!name.matches(NAME_PATTERN)) {
            throw new IllegalArgumentException("예약자 이름은 한글과 영문만 입력할 수 있습니다.");
        }
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("날짜는 비어 있을 수 없습니다.");
        }
    }

    private void validateTime(ReservationTime time) {
        if (time == null) {
            throw new IllegalArgumentException("예약시간은 비어 있을 수 없습니다.");
        }
    }

    private void validateThemeId(Long themeId) {
        if (themeId == null) {
            throw new IllegalArgumentException("테마 ID는 비어 있을 수 없습니다.");
        }

        if (themeId <= 0) {
            throw new IllegalArgumentException("테마 ID는 양수여야 합니다.");
        }
    }
}
