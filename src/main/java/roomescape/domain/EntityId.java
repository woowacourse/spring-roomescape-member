package roomescape.domain;

import com.github.f4b6a3.uuid.UuidCreator;
import java.util.UUID;

public final class EntityId {

    private final UUID uuid;

    private EntityId(UUID uuid) {
        validateValueExist(uuid);

        this.uuid = uuid;
    }

    public static EntityId random() {
        return new EntityId(UuidCreator.getTimeOrderedEpoch());
    }

    public static EntityId fromString(String value) {
        validateValueExist(value);

        return new EntityId(UUID.fromString(value));
    }

    private static void validateValueExist(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("EntityId는 빈 값을 지닐 수 없습니다.");
        }
    }

    public UUID getValueAsUuid() {
        return uuid;
    }

    public String getValueAsString() {
        return uuid.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        EntityId entityId = (EntityId) other;

        return uuid.equals(entityId.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public String toString() {
        return getValueAsString();
    }
}
