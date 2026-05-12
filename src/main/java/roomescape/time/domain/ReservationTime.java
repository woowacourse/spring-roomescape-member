package roomescape.time.domain;

import java.time.LocalTime;
import java.util.Objects;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ReservationTime {

    @EqualsAndHashCode.Include
    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        validateId(id);
        this.id = id;
        this.startAt = startAt;
    }

    private void validateId(Long id) {
        if(Objects.isNull(id) || id <= 0) {
            throw new IllegalArgumentException("ID 비어있거나 음수일 수 없습니다.");
        }
    }
}
