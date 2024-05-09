package roomescape.exception;

public class ClientErrorExceptionWithLog extends RuntimeException {

    private String loggingData;

    public ClientErrorExceptionWithLog(String clientMessage, String logMessage) {
        super(clientMessage);
        this.loggingData = logMessage;
    }

    public ClientErrorExceptionWithLog(String message) {
        super(message);
    }

    public String getData() {
        return loggingData;
    }
}
