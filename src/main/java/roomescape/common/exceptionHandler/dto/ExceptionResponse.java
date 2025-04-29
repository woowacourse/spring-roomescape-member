package roomescape.common.exceptionHandler.dto;

public record ExceptionResponse(int status, String message, String path) {
}
