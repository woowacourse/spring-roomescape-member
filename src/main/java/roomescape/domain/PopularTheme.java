package roomescape.domain;

import lombok.Getter;

@Getter
public class PopularTheme {

    private final Theme theme;
    private final int reservationCount;

    public PopularTheme(Theme theme, int reservationCount) {
        this.theme = theme;
        this.reservationCount = reservationCount;
    }
}
