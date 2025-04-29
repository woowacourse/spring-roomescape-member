package roomescape.common.domain;

import java.util.Objects;
import java.util.UUID;

public class Id {
    private boolean unassigned;
    private Long value;

    private Id(final Long value, final boolean unassigned) {
        this.value = value;
        this.unassigned = unassigned;
    }

    public static Id from(Long value) {
        Id id = new Id(value, false);
        id.setValue(value);
        return id;
    }

    public static Id unassigned() {
        return new Id(UUID.randomUUID().getMostSignificantBits(), true);
    }

    public Long getValue() {
        if (unassigned) {
            throw new IllegalStateException("할당되지 않은 ID에 접근할 수 없습니다.");
        }
        return value;
    }

    public void setValue(final Long value) {
        if (value == null) {
            throw new IllegalArgumentException("ID 값은 null일 수 없습니다.");
        }
        this.value = value;
        this.unassigned = false;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Id id = (Id) o;
        return unassigned == id.unassigned && Objects.equals(value, id.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, unassigned);
    }
}