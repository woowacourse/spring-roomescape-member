package roomescape.exception;

public class DuplicationException extends RuntimeException {
    private String action = "";

    public DuplicationException(String message) {
        super(message);
    }

    public DuplicationException(String message, String action) {
        super(message);
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
