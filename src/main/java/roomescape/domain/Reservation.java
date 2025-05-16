package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import roomescape.exception.BadRequestException;

public final class Reservation {

    private static final long DEFAULT_ID = 0L;
    private static final int MAX_NAME_LENGTH = 255;

    private final Long id;
    private final Member member;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(final Long id, final Member member, final LocalDate date, final ReservationTime time,
                       final Theme theme) {
        validateField(id, member, date, time, theme);
        this.id = id;
        this.member = member;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static Reservation createWithoutId(Member member, LocalDate date, ReservationTime time, Theme theme) {
        return new Reservation(DEFAULT_ID, member, date, time, theme);
    }

    public long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public void validatePastDateTime() {
        LocalDateTime dateTime = LocalDateTime.of(date, time.getStartAt());
        LocalDateTime now = LocalDateTime.now();
        if (dateTime.isBefore(now)) {
            throw new BadRequestException("[ERROR] 이미 과거의 날짜와 시간입니다.");
        }
    }

    private void validateField(Long id, Member member, LocalDate date, ReservationTime time, Theme theme) {
        validateNullId(id);
        validateMember(member);
        validateNullDate(date);
        validateNullTime(time);
        validateNullTheme(theme);
    }

    private void validateMember(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("[ERROR] 비어있는 멤버로 예약을 생성할 수 없습니다.");
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

    private void validateNullTheme(Theme theme) {
        if (theme == null) {
            throw new IllegalArgumentException("[ERROR] 비어있는 테마로는 예약을 생성할 수 없습니다.");
        }
    }
}
