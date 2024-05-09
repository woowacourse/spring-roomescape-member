package roomescape.service.dto.response;

import jakarta.validation.constraints.NotNull;

public record MemberNameResponse(@NotNull(message = "이름을 입력해주세요") String name) {
}
