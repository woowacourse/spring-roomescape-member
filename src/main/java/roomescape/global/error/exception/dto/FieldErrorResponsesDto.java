package roomescape.global.error.exception.dto;

import java.util.List;

public record FieldErrorResponsesDto(
    String message,
    List<FieldErrorResponseDto> fieldErrors
) {

}
