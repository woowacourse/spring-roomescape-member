package roomescape.domain;

public class ClientName {
    private static final int MAXIMUM_ENABLE_NAME_LENGTH = 5;

    private final String value;

    public ClientName(final String value) {
        validateClientName(value);
        this.value = value;
    }

    private void validateClientName(final String value) {
        if (value == null || value.isEmpty() || value.length() > MAXIMUM_ENABLE_NAME_LENGTH) {
            throw new IllegalArgumentException("예약자 이름은 1글자 이상 5글자 이하여야 합니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
