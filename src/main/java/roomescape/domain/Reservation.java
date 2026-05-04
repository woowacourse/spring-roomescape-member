package roomescape.domain;

import lombok.Getter;
import roomescape.domain.dto.ReservationCreateCommand;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class Reservation {

    private final Long id;
    private final Name name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Long themeId;

    private Reservation(final Long id, final Name name, final LocalDate date, final ReservationTime time, final Long themeId) {
        validateDateTime(date, time);

        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.themeId = themeId;
    }

    public static Reservation create(ReservationCreateCommand data) {
        return new Reservation(
                null,
                Name.from(data.name()),
                data.date(),
                data.time(),
                data.themeId()
        );
    }

    public Reservation saved(final Long id) {
        return new Reservation(
                id,
                name,
                date,
                time,
                themeId
        );
    }

    public static Reservation restore(
            final Long id,
            final String name,
            final LocalDate date,
            final ReservationTime time,
            final Long themeId) {
        return new Reservation(
                id,
                Name.from(name),
                date,
                time,
                themeId
        );
    }

    public String getName() {
        return name.getName();
    }

    private void validateDateTime(final LocalDate date, final ReservationTime time) {
        if (date == null) {
            throw new IllegalArgumentException("예약일을 입력해야 합니다.");
        }

        validateNotPast(date, time);
    }

    private void validateNotPast(final LocalDate date, final ReservationTime time) {
        final LocalDateTime reservationDateTime = LocalDateTime.of(date, time.getStartAt());

        if (reservationDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("현재 이전 시간으로는 예약할 수 없습니다.");
        }
    }
}
