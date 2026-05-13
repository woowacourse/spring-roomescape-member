package roomescape.exception;

public class ResourceDeleteConflicted extends RuntimeException {

    private final String code;

    public ResourceDeleteConflicted(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
