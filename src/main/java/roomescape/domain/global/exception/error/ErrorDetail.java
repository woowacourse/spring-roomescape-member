package roomescape.domain.global.exception.error;

public record ErrorDetail(String field, String value, String message) {

    public static ErrorDetail of(String field, Object value, String message) {
        if (value == null) {
            return ErrorDetail.of(field, message);
        }
        return new ErrorDetail(field, value.toString(), message);
    }

    public static ErrorDetail of(String field, String message) {
        return new ErrorDetail(field, null, message);
    }
}
