package roomescape.core.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import roomescape.web.exception.BadRequestException;

public class Reservation {
    private final Long id;
    private final Member member;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(
            final Member member,
            final String date,
            final ReservationTime time,
            final Theme theme
    ) {
        this(null, member, date, time, theme);
    }

    public Reservation(
            final Long id,
            final Member member,
            final String date,
            final ReservationTime time,
            final Theme theme
    ) {
        this(id, member, parseDate(date), time, theme);
    }

    public Reservation(
            final Long id,
            final Member member,
            final LocalDate date,
            final ReservationTime time,
            final Theme theme
    ) {
        validateEmpty(member, date, time, theme);
        this.id = id;
        this.member = member;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    private static LocalDate parseDate(final String date) {
        try {
            return LocalDate.parse(date);
        } catch (final NullPointerException e) {
            throw new BadRequestException("예약 날짜는 null일 수 없습니다.");
        } catch (final DateTimeParseException e) {
            throw new BadRequestException("예약 날짜 형식이 잘못되었습니다.");
        }
    }

    private void validateEmpty(
            final Member member,
            final LocalDate date,
            final ReservationTime time,
            final Theme theme
    ) {
        if (member == null) {
            throw new BadRequestException("예약자는 null일 수 없습니다.");
        }
        if (date == null) {
            throw new BadRequestException("예약 날짜는 null일 수 없습니다.");
        }
        if (time == null) {
            throw new BadRequestException("예약 시간은 null일 수 없습니다.");
        }
        if (theme == null) {
            throw new BadRequestException("예약 테마는 null일 수 없습니다.");
        }
    }

    public boolean isDatePast() {
        return date.isBefore(LocalDate.now());
    }

    public boolean isDateToday() {
        return date.isEqual(LocalDate.now());
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Long getMemberId() {
        return member.getId();
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDateString() {
        return date.format(DateTimeFormatter.ISO_DATE);
    }

    public Long getTimeId() {
        return time.getId();
    }

    public ReservationTime getTime() {
        return time;
    }

    public Long getThemeId() {
        return theme.getId();
    }

    public Theme getTheme() {
        return theme;
    }
}
