package roomescape.domain.global.exception;

public record ErrorDetail(String field, String value, String message) {

    public static ErrorDetail of(String field, Object value, String message) {
        return new ErrorDetail(field, value.toString(), message);
    }

    public static ErrorDetail of(String field, String message) {
        return new ErrorDetail(field, null, message);
    }
}
