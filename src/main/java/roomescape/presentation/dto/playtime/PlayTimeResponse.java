package roomescape.presentation.dto.playtime;

import java.time.LocalTime;
import roomescape.business.domain.PlayTime;

public record PlayTimeResponse(Long id, LocalTime startAt) {

    public static PlayTimeResponse from(final PlayTime playTime) {
        return new PlayTimeResponse(playTime.getId(), playTime.getStartAt());
    }

    public static PlayTimeResponse withId(final Long id, final PlayTime playTime) {
        return new PlayTimeResponse(id, playTime.getStartAt());
    }
}
