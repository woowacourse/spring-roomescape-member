package roomescape.common.exception;

import java.time.LocalDate;
import java.time.LocalTime;

public class DuplicatedException extends BusinessException {

    public DuplicatedException(DomainType domainType, LocalDate date, Long timeId, Long themeId) {
        super(clientMessage(domainType),
                logMessage(domainType, date, timeId, themeId)
        );
    }

    public DuplicatedException(DomainType domainType, LocalTime time) {
        super(clientMessage(domainType),
                logMessage(domainType, time)
        );
    }

    public static String clientMessage(DomainType domainType) {
        return "이미 존재하는 %s입니다.".formatted(domainType.displayName());
    }

    private static String logMessage(DomainType domainType, LocalDate date, Long timeId, Long themeId) {
        return "Duplicated domain=%s, date=%s, timeId=%d, themeId=%d"
                .formatted(domainType.name(), date, timeId, themeId);
    }

    private static String logMessage(DomainType domainType, LocalTime time) {
        return "Duplicated domain=%s, time=%s".formatted(domainType.name(), time);
    }

}
