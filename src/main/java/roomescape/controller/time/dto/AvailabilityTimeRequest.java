package roomescape.controller.time.dto;

import jakarta.validation.constraints.NotNull;

//TODO string 말고 localdate로 변경하기
public record AvailabilityTimeRequest(String date,
                                      @NotNull
                                      Long themeId) { //TODO id에 문자열 오면 어케됨?
}
