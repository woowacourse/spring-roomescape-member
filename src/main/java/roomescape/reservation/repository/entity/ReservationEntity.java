package roomescape.reservation.repository.entity;

import java.time.LocalDate;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ReservationEntity {

    @EqualsAndHashCode.Include
    private final Long id;
    private final String name;
    private final LocalDate date;
    private final Long timeId;
    private final Long themeId;

    public ReservationEntity(Long id, String name, LocalDate date, Long timeId, Long themeId) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.timeId = timeId;
        this.themeId = themeId;
    }
}
