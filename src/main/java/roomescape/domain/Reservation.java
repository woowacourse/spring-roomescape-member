package roomescape.domain;

import java.time.LocalDate;
import java.util.Objects;
import lombok.Getter;
import org.springframework.util.StringUtils;
import roomescape.exception.ErrorCode;
import roomescape.exception.InvalidInputException;

@Getter
public class Reservation {

    private final EntityId id;
    private final String name;
    private final LocalDate date;
    private final boolean canceled;
    private final EntityId timeId;
    private final EntityId themeId;

    private Reservation(
            EntityId id,
            String name,
            LocalDate date,
            boolean canceled,
            EntityId timeId,
            EntityId themeId
    ) {
        validateId(id);
        validateName(name);
        validateDate(date);
        validateTime(timeId);
        validateTheme(themeId);

        this.id = id;
        this.name = name;
        this.date = date;
        this.canceled = canceled;
        this.timeId = timeId;
        this.themeId = themeId;
    }

    public static Reservation create(
            EntityId id,
            String name,
            LocalDate date,
            EntityId timeId,
            EntityId themeId
    ) {
        boolean defaultCanceled = false;

        return new Reservation(
                id,
                name,
                date,
                defaultCanceled,
                timeId,
                themeId
        );
    }

    public static Reservation retrieve(
            EntityId id,
            String name,
            LocalDate date,
            boolean canceled,
            EntityId timeId,
            EntityId themeId
    ) {
        return new Reservation(
                id,
                name,
                date,
                canceled,
                timeId,
                themeId
        );
    }

    public Reservation updateCanceled(boolean canceled) {
        return new Reservation(
                this.id,
                this.name,
                this.date,
                canceled,
                this.timeId,
                this.themeId
        );
    }

    private void validateId(EntityId id) {
        if (id == null) {
            throw new InvalidInputException(
                    ErrorCode.INVALID_RESERVATION,
                    "예약엔 식별자가 존재해야 합니다."
            );
        }
    }

    private void validateName(String name) {
        if (!StringUtils.hasText(name)) {
            throw new InvalidInputException(
                    ErrorCode.INVALID_RESERVATION,
                    "예약엔 이름이 존재해야 합니다."
            );
        }
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new InvalidInputException(
                    ErrorCode.INVALID_RESERVATION,
                    "예약엔 날짜가 존재해야 합니다."
            );
        }
    }

    private void validateTime(EntityId timeId) {
        if (timeId == null) {
            throw new InvalidInputException(
                    ErrorCode.INVALID_RESERVATION,
                    "예약엔 시간이 존재해야 합니다."
            );
        }
    }

    private void validateTheme(EntityId themeId) {
        if (themeId == null) {
            throw new InvalidInputException(
                    ErrorCode.INVALID_RESERVATION,
                    "예약엔 테마가 존재해야 합니다."
            );
        }
    }

    public boolean hasDifferentName(String name) {
        return !this.name.equals(name);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
