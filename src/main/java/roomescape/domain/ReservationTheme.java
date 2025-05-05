package roomescape.domain;

import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ReservationTheme {

    @EqualsAndHashCode.Include
    private Long id;
    @Size(max = 20, message = "테마명은 20자 이내로 입력해 주세요.")
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
