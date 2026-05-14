package roomescape.global.error.exception.dto;

import java.util.List;

public record ParameterErrorResponsesDto(
    String message,
    List<ParameterErrorResponseDto> parameterErrors
) {

}
