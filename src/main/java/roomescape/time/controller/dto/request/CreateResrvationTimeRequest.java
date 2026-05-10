package roomescape.time.controller.dto.request;

import java.time.LocalTime;

public record CreateResrvationTimeRequest(LocalTime startAt) {
}
