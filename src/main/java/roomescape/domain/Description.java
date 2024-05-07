package roomescape.domain;

import java.util.Objects;
import roomescape.exception.InvalidReservationException;

public class Description {
    private static final int MINIMUM_DESCRIPTION_LENGTH = 1;
    private static final int MAXIMUM_DESCRIPTION_LENGTH = 125;
    private static final String INVALID_DESCRIPTION_LENGTH = String.format("설명은 %d자 이상, %d자 이하여야 합니다.",
            MINIMUM_DESCRIPTION_LENGTH,
            MAXIMUM_DESCRIPTION_LENGTH);

    private final String value;

    public Description(String value) {
        validateDescription(value);
        this.value = value;
    }

    private void validateDescription(final String description) {
        if (description.isEmpty() || description.length() > MAXIMUM_DESCRIPTION_LENGTH) {
            throw new InvalidReservationException(INVALID_DESCRIPTION_LENGTH);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Description that = (Description) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public String getValue() {
        return value;
    }
}
