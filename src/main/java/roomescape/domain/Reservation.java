package roomescape.domain;

import java.time.LocalDate;
import java.util.Objects;

public record Reservation(Long id, String name, LocalDate date, ReservationTime time) {
    public Reservation {
        validateName(name);
        validateDate(date);
        validateTime(time);
    }

    private void validateTime(ReservationTime time) {
        if (Objects.isNull(time)) {
            throw new IllegalArgumentException("유효하지 않은 시간입니다.");
        }
    }

    private void validateName(String name) {
        if (Objects.isNull(name)) {
            throw new IllegalArgumentException("유효하지 않은 이름입니다.");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("이름은 공백일 수 없습니다.");
        }
    }

    private void validateDate(LocalDate date) {
        if (Objects.isNull(date)) {
            throw new IllegalArgumentException("유효하지 않은 날짜입니다");
        }
    }

    public long timeId() {
        return time.id();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Reservation that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
