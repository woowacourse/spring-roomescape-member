package roomescape.domain.theme.validator;

import java.util.ArrayList;
import java.util.List;
import roomescape.domain.global.exception.custom.BadRequestException;
import roomescape.domain.global.exception.error.ErrorCode;
import roomescape.domain.global.exception.error.ErrorDetail;

public final class ThemePopularGetRequestValidator {

    private ThemePopularGetRequestValidator() {}

    public static void validate(Integer limit) {
        List<ErrorDetail> errors = new ArrayList<>();
        validateLimit(limit, errors);

        if (!errors.isEmpty()) {
            throw new BadRequestException(ErrorCode.COMMON_INVALID_REQUEST, errors);
        }
    }

    private static void validateLimit(Integer limit, List<ErrorDetail> errors) {
        if (limit < 0) {
            errors.add(ErrorDetail.of("limit", limit, "limit은 0 또는 양수여야 합니다."));
        }
    }
}
