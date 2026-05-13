package roomescape.exception.response;

import lombok.Builder;

@Builder
public record ErrorResponse(String code,
                            String message) {
}
