package roomescape.common.exception;

import org.springframework.http.HttpStatus;
import roomescape.common.domain.DomainTerm;
import roomescape.common.exception.util.ExceptionMessageFormatter;

public class NotFoundException extends BusinessException {

    public NotFoundException(final DomainTerm term, final Object... params) {
        super(
                buildLogMessage(term, params),
                buildUserMessage(term)
        );
    }

    private static String buildLogMessage(final DomainTerm term, final Object... params) {
        return ExceptionMessageFormatter.format("Tried to delete [%s] that does not exist.".formatted(term.name()), params);
    }

    private static String buildUserMessage(final DomainTerm term) {
        return "삭제할 %s이(가) 존재하지 않습니다.".formatted(term.label());
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
