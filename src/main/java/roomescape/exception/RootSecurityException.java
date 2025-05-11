package roomescape.exception;

public class RootSecurityException extends RuntimeException {

    private final SecurityErrorCode code;

    public RootSecurityException(SecurityErrorCode code) {
        super(code.clientMessage());
        this.code = code;
    }

    public SecurityErrorCode code() {
        return code;
    }
}
