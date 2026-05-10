package roomescape.global.exception;

public class InvalidRankingConditionException extends RoomescapeException {

    public InvalidRankingConditionException(String message) {
        super(ErrorCode.INVALID_RANKING_CONDITION, message);
    }
}
