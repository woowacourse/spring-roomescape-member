package roomescape.common.exception;

public abstract class BusinessException extends RuntimeException {

    private final String clientMessage;
    private final String logMessage;

    protected BusinessException(String clientMessage, String logMessage) {
        super(clientMessage);
        this.clientMessage = clientMessage;
        this.logMessage = logMessage;
    }

    protected BusinessException(String clientMessage, String logMessage, Throwable cause) {
        super(clientMessage, cause);
        this.clientMessage = clientMessage;
        this.logMessage = logMessage;
    }

    public final String getClientMessage() {
        return clientMessage;
    }

    public final String getLogMessage() {
        return logMessage;
    }

}
