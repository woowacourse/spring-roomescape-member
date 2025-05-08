package roomescape.exception;

public class DomainException extends RuntimeException {
    private final String title;
    private final String code;

    protected DomainException(String message, String title, String code) {
        super(message);
        this.title = title;
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public String getCode() {
        return code;
    }
}
