package roomescape.common.domain;

import java.util.Objects;
import java.util.UUID;

public class Id {

    private final UUID objectId;
    private final Long databaseId;

    public Id(final UUID objectId, final Long databaseId) {
        this.objectId = validateNotNull(objectId);
        this.databaseId = databaseId;
    }

    public static Id unassigned() {
        return new Id(UUID.randomUUID(), null);
    }

    public static Id assignDatabaseId(Long databaseId) {
        validateNotNull(databaseId);
        return new Id(UUID.randomUUID(), databaseId);
    }

    private static <T> T validateNotNull(T value) {
        if (value == null) {
            throw new IllegalArgumentException("null이 될 수 없습니다.");
        }
        return value;
    }

    @Override
    public boolean equals(final Object object) {
        if (!(object instanceof final Id id)) {
            return false;
        }
        return Objects.equals(objectId, id.objectId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(objectId);
    }

    public Long getDatabaseId() {
        if (isNotPersisted()) {
            throw new IllegalStateException("할당되지 않은 ID에 접근할 수 없습니다.");
        }
        return databaseId;
    }

    private boolean isNotPersisted() {
        return databaseId == null;
    }
}
