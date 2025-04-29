package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import roomescape.exception.BadRequestException;

public final class Reservation {

    private static final long DEFAULT_ID = 0L;
    private static final int MAX_NAME_LENGTH = 255;

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;

    public Reservation(Long id, String name, LocalDate date, ReservationTime time) {
        validateField(id, name, date, time);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public static Reservation createWithoutId(String name, LocalDate date, ReservationTime time) {
        return new Reservation(DEFAULT_ID, name, date, time);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name)
                && Objects.equals(date, that.date) && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, date, time);
    }

    public void validatePastDateTime() {
        LocalDateTime dateTime = LocalDateTime.of(date, time.getStartAt());
        LocalDateTime now = LocalDateTime.now();
        if (dateTime.isBefore(now)) {
            throw new BadRequestException("[ERROR] 이미 과거의 날짜와 시간입니다.");
        }
    }

    private void validateField(Long id, String name, LocalDate date, ReservationTime time) {
        validateNullId(id);
        validateBlankName(name);
        validateNameLength(name);
        validateNullDate(date);
        validateNullTime(time);
    }

    private void validateBlankName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 비어있는 이름으로 예약을 생성할 수 없습니다.");
        }
    }

    private void validateNameLength(String name) {
        if (name.length() > MAX_NAME_LENGTH) {
            String message = String.format("[ERROR] 이름으로 입력된 문자열의 길이가 최대값(%s자)을 초과했습니다.", MAX_NAME_LENGTH);
            throw new IllegalArgumentException(message);
        }
    }

    private void validateNullId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("[ERROR] 비어있는 ID로 예약을 생성할 수 없습니다.");
        }
    }

    private void validateNullDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("[ERROR] 비어있는 예약날짜로 예약을 생성할 수 없습니다.");
        }
    }

    private void validateNullTime(ReservationTime reservationTime) {
        if (reservationTime == null) {
            throw new IllegalArgumentException("[ERROR] 비어있는 예약시간으로는 예약을 생성할 수 없습니다.");
        }
    }
}
