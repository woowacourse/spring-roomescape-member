package roomescape.domain;

import java.time.LocalDate;
import java.util.Objects;
import org.springframework.util.StringUtils;

public record Reservation(
        EntityId id,
        String name,
        LocalDate date,
        EntityId timeId,
        EntityId themeId
) {

    public Reservation {
        validateId(id);
        validateName(name);
        validateDate(date);
        validateTime(timeId);
        validateTheme(themeId);
    }

    private void validateId(EntityId id) {
        if (id == null) {
            throw new IllegalArgumentException("예약엔 식별자가 존재해야 합니다.");
        }
    }

    private void validateName(String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("예약엔 이름이 존재해야 합니다.");
        }
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("예약엔 날짜가 존재해야 합니다.");
        }
    }

    private void validateTime(EntityId timeId) {
        if (timeId == null) {
            throw new IllegalArgumentException("예약엔 시간이 존재해야 합니다.");
        }
    }

    private void validateTheme(EntityId themeId) {
        if (themeId == null) {
            throw new IllegalArgumentException("예약엔 테마가 존재해야 합니다.");
        }
    }

    public boolean hasDifferentName(String name) {
        return !this.name.equals(name);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
