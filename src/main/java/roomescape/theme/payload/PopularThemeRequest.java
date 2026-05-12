package roomescape.theme.payload;

public record PopularThemeRequest(Integer days, Integer limits) {
    public boolean isEmpty() {
        return days == null && limits == null;
    }

    public boolean isComplete() {
        return days != null && limits != null;
    }
}
