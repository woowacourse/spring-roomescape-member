package roomescape.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberResponse(
        
        @Schema(description = "멤버 이름", example = "체체")
        String name
) {
}
