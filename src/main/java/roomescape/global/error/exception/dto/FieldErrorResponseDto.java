package roomescape.global.error.exception.dto;

public record FieldErrorResponseDto(
    String field,
    String message
) {

}
