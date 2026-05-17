package roomescape.theme.payload;

import jakarta.validation.constraints.Positive;

public record PopularThemeRequest(
        @Positive
        Integer days,
        @Positive
        Integer limits
) {
    public boolean isEmpty() {
        return days == null && limits == null;
    }

    public boolean isComplete() {
        return days != null && limits != null;
    }
}
