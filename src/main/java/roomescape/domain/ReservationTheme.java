package roomescape.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ReservationTheme {

    @EqualsAndHashCode.Include
    private Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public ReservationTheme(final String name, final String description, final String thumbnail) {
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public ReservationTheme assignId(final Long id) {
        this.id = id;
        return this;
    }
}
