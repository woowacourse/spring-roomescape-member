package roomescape.common.exception;

public abstract class BusinessException extends RuntimeException {

    private final String clientMessage;
    private final String logMessage;

    protected BusinessException(String clientMessage, String logMessage) {
        super(clientMessage);
        this.clientMessage = clientMessage;
        this.logMessage = logMessage;
    }

    public String getClientMessage() {
        return clientMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }

}
