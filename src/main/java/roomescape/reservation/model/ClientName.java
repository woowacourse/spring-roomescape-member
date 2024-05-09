package roomescape.reservation.model;

public record ClientName(String value) {

    private static final int MAXIMUM_ENABLE_NAME_LENGTH = 8;

    public ClientName {
        validateClientName(value);
    }

    private void validateClientName(final String value) {
        if (value == null) {
            throw new IllegalArgumentException("예약자 이름은 1글자 이상 8글자 이하여야 합니다.");
        }

        final String stripedValue = value.strip();
        if (stripedValue.isEmpty() || stripedValue.length() > MAXIMUM_ENABLE_NAME_LENGTH) {
            throw new IllegalArgumentException("예약자 이름은 1글자 이상 8글자 이하여야 합니다.");
        }
    }
}
