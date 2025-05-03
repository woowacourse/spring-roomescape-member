package roomescape.dto.other;

import java.time.LocalTime;

public record TimeWithBookState(
        Long id,
        LocalTime startAt,
        Boolean bookState
) {

}
