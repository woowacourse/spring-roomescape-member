package roomescape.common.exception;

public class AccessDeniedException extends BusinessException {

    public AccessDeniedException(DomainType domainType, Long id) {
        super(clientMessage(domainType.displayName()),
                logMessage(domainType, id)
        );
    }

    public static String clientMessage(String domainName) {
        return "%s을 처리할 권한이 없습니다.".formatted(domainName);
    }

    private static String logMessage(DomainType domainType, Long id) {
        return "AccessDenied domain=%s, id=%d".formatted(domainType.name(), id);
    }

}
