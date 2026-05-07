package roomescape.time.controller.dto;

import java.time.LocalTime;

public record CreateResrvationTimeRequest(LocalTime startAt) {
}
