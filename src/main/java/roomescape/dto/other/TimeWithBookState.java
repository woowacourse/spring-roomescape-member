package roomescape.dto.other;

import java.time.LocalTime;

public record TimeWithBookState(
        long id,
        LocalTime startAt,
        boolean bookState
) {

}
