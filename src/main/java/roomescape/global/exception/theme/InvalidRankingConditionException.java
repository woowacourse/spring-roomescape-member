package roomescape.global.exception.theme;

import roomescape.global.exception.status.BadRequestException;

public class InvalidRankingConditionException extends BadRequestException {

    public InvalidRankingConditionException(String message) {
        super(message);
    }
}
