package roomescape.domain.reservation;

import java.util.Objects;

public record ReserverName(String name) {
    private static final int MAX_NAME_LENGTH = 5;

    public ReserverName(String name) {
        this.name = validate(name);
    }

    private String validate(String reserverName) {
        String validated = Objects.requireNonNull(reserverName, "예약자 이름은 null일 수 없습니다.");
        if (validated.isBlank()) {
            throw new IllegalStateException("예약자 이름은 공백일 수 없습니다.");
        }
        if (validated.length() > MAX_NAME_LENGTH) {
            throw new IllegalStateException("예약자 이름은 " + MAX_NAME_LENGTH + "자 이내여야 합니다.");
        }
        return validated;
    }

    public String getName() {
        return name;
    }
}
