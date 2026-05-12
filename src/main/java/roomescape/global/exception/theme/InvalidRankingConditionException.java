package roomescape.global.exception.theme;

import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.RoomescapeException;

public class InvalidRankingConditionException extends RoomescapeException {

    public InvalidRankingConditionException(String message) {
        super(ErrorCode.INVALID_RANKING_CONDITION, message);
    }
}
