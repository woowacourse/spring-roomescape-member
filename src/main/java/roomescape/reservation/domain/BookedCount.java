package roomescape.reservation.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import roomescape.common.exception.InternalLogicException;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class BookedCount {

    public static final String domainName = "예약 수";

    private final int value;

    public static BookedCount from(final int value) {
        validateNegative(value);
        return new BookedCount(value);
    }

    private static void validateNegative(final int value) {
        if (value < 0) {
            throw new InternalLogicException("BookedCount must not be negative: " + value);
        }
    }
}
