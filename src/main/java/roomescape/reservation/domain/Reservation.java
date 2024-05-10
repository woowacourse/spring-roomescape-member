package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import roomescape.auth.domain.Member;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

public class Reservation {

  private final Long id;
  private final Member member;
  private final ReservationDate date;
  private final ReservationTime time;
  private final Theme theme;

  private Reservation(
      final Long id,
      final Member member,
      final ReservationDate date,
      final ReservationTime time,
      final Theme theme
  ) {
    this.id = id;
    this.member = member;
    this.date = date;
    this.time = time;
    this.theme = theme;
  }

  public static Reservation createInstanceWithoutId(
      final Member member,
      final LocalDate date,
      final ReservationTime time,
      final Theme theme
  ) {
    final ReservationDate reservationDate = new ReservationDate(date);
    validateReservationDateAndTime(reservationDate, time);

    return new Reservation(
        null,
        member,
        reservationDate,
        time,
        theme
    );
  }

  private static void validateReservationDateAndTime(final ReservationDate date,
      final ReservationTime time) {
    LocalDateTime reservationLocalDateTime = LocalDateTime.of(date.getValue(), time.getStartAt());
    if (reservationLocalDateTime.isBefore(LocalDateTime.now())) {
      throw new IllegalArgumentException("예약 일시는 현재 시간 이후여야 합니다.");
    }
  }

  public static Reservation createInstance(
      final Long id,
      final String name,
      final LocalDate date,
      final ReservationTime time,
      final Theme theme
  ) {
    return new Reservation(
        id,
        Member.of(new Name(name)),
        new ReservationDate(date),
        time,
        theme
    );
  }

  public static Reservation createInstance(
      final Long id,
      final Member member,
      final LocalDate date,
      final ReservationTime time,
      final Theme theme
  ) {
    return new Reservation(
        id,
        member,
        new ReservationDate(date),
        time,
        theme
    );
  }

  public Reservation copyWithId(final Long reservationId) {
    return new Reservation(reservationId, member, date, time, theme);
  }

  public Long getId() {
    return id;
  }

  public Member getMember() {
    return member;
  }

  public ReservationDate getDate() {
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
    if (this == o) {
      return true;
    }
    if (!(o instanceof final Reservation that)) {
      return false;
    }
    return id == that.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
