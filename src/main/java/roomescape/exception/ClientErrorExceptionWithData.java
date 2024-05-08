package roomescape.exception;

public class ClientErrorExceptionWithData extends RuntimeException {

    private String loggingData;

    public ClientErrorExceptionWithData(String message, String loggingData) {
        super(message);
        this.loggingData = loggingData;
    }

    public ClientErrorExceptionWithData(String message) {
        super(message);
    }

    public String getData() {
        return loggingData;
    }
}
