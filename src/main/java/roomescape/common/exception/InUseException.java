package roomescape.common.exception;

public final class InUseException extends BusinessException {

    public InUseException(DomainType domainType, Long id) {
        super(clientMessage(domainType.displayName()),
                logMessage(domainType, id)
        );
    }

    public InUseException(DomainType domainType, Long id, Throwable cause) {
        super(clientMessage(domainType.displayName()),
                logMessage(domainType, id),
                cause
        );
    }

    public static String clientMessage(String domainName) {
        return "예약이 존재하는 %s을 삭제할 수 없습니다.".formatted(domainName);
    }

    private static String logMessage(DomainType domainType, Long id) {
        return "InUse domain=%s, id=%d".formatted(domainType.name(), id);
    }

}
