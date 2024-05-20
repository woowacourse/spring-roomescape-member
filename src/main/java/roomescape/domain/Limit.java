package roomescape.domain;

import roomescape.exception.CustomBadRequest;

public record Limit(int value) {

    public Limit {
        validatePositive(value);
    }

    private void validatePositive(final int value) {
        if (value <= 0) {
            throw new CustomBadRequest(String.format("limit(%s)이 유효하지 않습니다.", value));
        }
    }

    public static Limit from(final int value) {
        return new Limit(value);
    }

}
