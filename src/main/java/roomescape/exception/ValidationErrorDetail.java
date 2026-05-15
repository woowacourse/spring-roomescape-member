package roomescape.exception;

public record ValidationErrorDetail(
        String code,
        String message
) {
}
