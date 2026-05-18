package roomescape.common.exception;

public final class NotFoundException extends BusinessException {

    public NotFoundException(DomainType domainType, Long id) {
        super(clientMessage(domainType),
                logMessage(domainType, id)
        );
    }

    public static String clientMessage(DomainType domainType) {
        return "존재하지 않는 %s입니다.".formatted(domainType.displayName());
    }

    private static String logMessage(DomainType domainType, Long id) {
        return "NotFound domain=%s, id=%d".formatted(domainType.name(), id);
    }

}
