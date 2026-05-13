package roomescape.global.error.exception.dto;

import java.util.List;

public record FieldErrorResponsesDTO(
    String message,
    List<FieldErrorResponseDTO> fieldErrors
) {

}
