package roomescape.persistence.entity;

import java.util.Objects;
import roomescape.business.domain.reservation.Reservation;
import roomescape.business.domain.reservation.ReservationDateTimeFormatter;

public final class ReservationEntity {

    private final Long id;
    private final MemberEntity memberEntity;
    private final String date;
    private final ReservationTimeEntity timeEntity;
    private final ReservationThemeEntity themeEntity;

    public ReservationEntity(Long id,
                             MemberEntity memberEntity,
                             String date,
                             ReservationTimeEntity timeEntity,
                             ReservationThemeEntity themeEntity) {
        this.id = id;
        this.memberEntity = memberEntity;
        this.date = date;
        this.timeEntity = timeEntity;
        this.themeEntity = themeEntity;
    }

    private ReservationEntity(MemberEntity memberEntity,
                              String date,
                              ReservationTimeEntity timeEntity,
                              ReservationThemeEntity themeEntity) {
        this(null, memberEntity, date, timeEntity, themeEntity);
    }

    public static ReservationEntity fromDomain(Reservation reservation) {
        String reservationDate = reservation.getDate().toString();
        ReservationTimeEntity timeEntity = ReservationTimeEntity.fromDomain(reservation.getTime());
        ReservationThemeEntity themeEntity = ReservationThemeEntity.fromDomain(reservation.getTheme());
        MemberEntity memberEntity = MemberEntity.fromDomain(reservation.getMember());
        if (reservation.getId() != null) {
            return new ReservationEntity(
                    reservation.getId(),
                    memberEntity,
                    reservationDate,
                    timeEntity,
                    themeEntity);
        }
        return new ReservationEntity(memberEntity, reservationDate, timeEntity, themeEntity);
    }

    public ReservationEntity copyWithId(Long id) {
        return new ReservationEntity(id, memberEntity, date, timeEntity, themeEntity);
    }

    public boolean isSameReservation(ReservationEntity otherReservationEntity) {
        return this.date.equals(otherReservationEntity.date)
                && this.timeEntity.getId().equals(otherReservationEntity.timeEntity.getId())
                && this.themeEntity.getId().equals(otherReservationEntity.themeEntity.getId());
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
                memberEntity.toDomain(),
                ReservationDateTimeFormatter.parseDate(date),
                timeEntity.toDomain(),
                themeEntity.toDomain()
        );
    }

    public Long getId() {
        return id;
    }

    public MemberEntity getMemberEntity() {
        return memberEntity;
    }

    public String getName() {
        return memberEntity.getName();
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
