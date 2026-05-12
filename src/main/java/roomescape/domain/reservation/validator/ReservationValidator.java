package roomescape.domain.reservation.validator;

import java.util.ArrayList;
import java.util.List;
import roomescape.domain.global.exception.BadRequestException;
import roomescape.domain.global.exception.ErrorCode;
import roomescape.domain.global.exception.ErrorDetail;

public final class ReservationValidator {

    private ReservationValidator() {}

    public static void validate(String name) {
        List<ErrorDetail> errors = new ArrayList<>();
        validateName(name, errors);

        if (!errors.isEmpty()) {
            throw new BadRequestException(ErrorCode.RESERVATION_INVALID_REQUEST, errors);
        }
    }

    private static void validateName(String name, List<ErrorDetail> errors) {
        if (name.isBlank()) {
            errors.add(ErrorDetail.of("name", name, "이름은 비어있지 않은 문자열이어야 합니다."));
        }
    }

}
