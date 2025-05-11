package roomescape.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginMemberRequest(@JsonProperty String email, @JsonProperty String password) {
}
