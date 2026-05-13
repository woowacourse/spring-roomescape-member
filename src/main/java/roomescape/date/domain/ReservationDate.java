package roomescape.date.domain;

import java.time.LocalDate;

public class ReservationDate {

    private Long id;
    private LocalDate date;
    private boolean isActive;

    private ReservationDate(Long id, LocalDate date, boolean isActive) {
        this.id = id;
        this.date = date;
        this.isActive = isActive;
    }

    public static ReservationDate create(LocalDate date) {
        validateDate(date);
        return new ReservationDate(null, date, false);
    }

    public static ReservationDate load(Long id, LocalDate date, boolean isActive) {
        validateId(id);
        return new ReservationDate(id, date, isActive);
    }

    public Long id() {
        return id;
    }

    public LocalDate date() {
        return date;
    }

    public boolean isActive() {
        return isActive;
    }

    public static void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("예약날짜 ID는 필수입니다.");
        }
    }

    public static void validateDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("예약 날짜는 필수입니다.");
        }

        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("과거 날짜는 등록할 수 없습니다.");
        }
    }

    public void updateStatus(boolean isActive) {
        this.isActive = isActive;
    }

}
