package roomescape.persistence.entity;

import java.util.Objects;
import roomescape.business.domain.Reservation;
import roomescape.business.domain.ReservationDateTimeFormatter;

public final class ReservationEntity {

    private final Long id;
    private final String name;
    private final String date;
    private final ReservationTimeEntity timeEntity;
    private final ReservationThemeEntity themeEntity;

    public ReservationEntity(Long id,
                             String name,
                             String date,
                             ReservationTimeEntity timeEntity,
                             ReservationThemeEntity themeEntity) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.timeEntity = timeEntity;
        this.themeEntity = themeEntity;
    }

    private ReservationEntity(String name, String date, ReservationTimeEntity timeEntity,
                              ReservationThemeEntity themeEntity) {
        this(null, name, date, timeEntity, themeEntity);
    }

    public ReservationEntity copyWithId(Long id) {
        return new ReservationEntity(id, name, date, timeEntity, themeEntity);
    }

    public boolean isSameReservation(ReservationEntity otherReservationEntity) {
        return this.date.equals(otherReservationEntity.date)
                && this.timeEntity.getId().equals(otherReservationEntity.timeEntity.getId())
                && this.themeEntity.getId().equals(otherReservationEntity.themeEntity.getId());
    }

    public static ReservationEntity fromDomain(Reservation reservation) {
        String reservationDate = reservation.getDate().toString();
        ReservationTimeEntity timeEntity = ReservationTimeEntity.fromDomain(reservation.getTime());
        ReservationThemeEntity themeEntity = ReservationThemeEntity.fromDomain(reservation.getTheme());
        if (reservation.getId() != null) {
            return new ReservationEntity(
                    reservation.getId(),
                    reservation.getName(),
                    reservationDate,
                    timeEntity,
                    themeEntity);
        }
        return new ReservationEntity(reservation.getName(), reservationDate, timeEntity, themeEntity);
    }

    public Reservation toDomain() {
        if (id == null) {
            throw new IllegalArgumentException("예약 엔티티의 ID가 null이어서 도메인 객체로 변환할 수 없습니다.");
        }
        if (timeEntity.getId() == null) {
            throw new IllegalArgumentException("예약 가능 시간 엔티티의 ID가 null이어서 도메인 객체로 변환할 수 없습니다.");
        }
        return new Reservation(
                id,
                name,
                ReservationDateTimeFormatter.parseDate(date),
                timeEntity.toDomain(),
                themeEntity.toDomain()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public ReservationTimeEntity getTimeEntity() {
        return timeEntity;
    }

    public ReservationThemeEntity getThemeEntity() {
        return themeEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReservationEntity that = (ReservationEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
