package roomescape.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record LoginMemberInformation(
    @NotEmpty(message = "사용자 이름은 비어 있을 수 없습니다.") String name,
    @NotNull @Positive Long id
) {
}
