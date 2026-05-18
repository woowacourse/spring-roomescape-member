package roomescape.common.exception;

import java.time.LocalDateTime;

public final class PastDateTimeException extends BusinessException {

    public PastDateTimeException(DomainType domainType, LocalDateTime now, LocalDateTime request) {
        super(clientMessage(domainType.displayName()),
                logMessage(domainType, now, request)
        );
    }

    public static String clientMessage(String domainName) {
        return "과거 %s은 생성, 변경, 취소할 수 없습니다.".formatted(domainName);
    }

    private static String logMessage(DomainType domainType, LocalDateTime now, LocalDateTime request) {
        return "PastDateTime domain=%s, now=%s, request=%s".formatted(domainType.name(), now, request);
    }

}
