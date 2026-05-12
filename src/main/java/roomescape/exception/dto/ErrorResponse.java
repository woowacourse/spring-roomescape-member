package roomescape.exception.dto;

import java.util.List;

public record ErrorResponse(String errorCode, String errorMessage, List<FieldErrorResponse> fieldErrors) {
}
