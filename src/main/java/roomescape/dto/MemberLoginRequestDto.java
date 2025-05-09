package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MemberLoginRequestDto(@JsonProperty String email, @JsonProperty String password) {
}
