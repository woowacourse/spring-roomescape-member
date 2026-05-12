package roomescape.domain;

import com.github.f4b6a3.uuid.UuidCreator;
import java.nio.ByteBuffer;
import java.util.UUID;

public final class EntityId {

    private final UUID uuid;

    private EntityId(UUID uuid) {
        this.uuid = uuid;
    }

    public static EntityId random() {
        return new EntityId(UuidCreator.getTimeOrderedEpoch());
    }

    public static EntityId fromString(String value) {
        return new EntityId(UUID.fromString(value));
    }

    public static EntityId fromBytes(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        UUID uuid = new UUID(buffer.getLong(), buffer.getLong());

        return new EntityId(uuid);
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(16);

        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());

        return buffer.array();
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
