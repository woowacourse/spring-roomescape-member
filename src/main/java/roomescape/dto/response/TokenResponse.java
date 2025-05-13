package roomescape.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record TokenResponse(
        
        @Schema(description = "토큰", example = "32rino2nogfi132---")
        String accessToken
) {
}
