package roomescape.persistence.entity;

import java.util.Objects;
import roomescape.business.domain.reservation.ReservationDateTimeFormatter;
import roomescape.business.domain.reservation.ReservationTime;

public final class ReservationTimeEntity {

    private final Long id;
    private final String startAt;

    public ReservationTimeEntity(Long id, String startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    private ReservationTimeEntity(String startAt) {
        this(null, startAt);
    }

    public static ReservationTimeEntity fromDomain(ReservationTime reservationTime) {
        String formattedStartAt = ReservationDateTimeFormatter.formatTime(reservationTime.getStartAt());
        if (reservationTime.getId() != null) {
            return new ReservationTimeEntity(reservationTime.getId(), formattedStartAt);
        }
        return new ReservationTimeEntity(formattedStartAt);
    }

    public ReservationTimeEntity copyWithId(Long id) {
        return new ReservationTimeEntity(id, startAt);
    }

    public ReservationTime toDomain() {
        if (id == null) {
            throw new IllegalArgumentException("예약 가능 시간 엔티티의 ID가 null이어서 도메인 객체로 변환할 수 없습니다.");
        }
        return new ReservationTime(id, ReservationDateTimeFormatter.parseTime(startAt));
    }

    public Long getId() {
        return id;
    }

    public String getStartAt() {
        return startAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (id == null || ((ReservationTimeEntity) o).id == null) {
            return false;
        }
        ReservationTimeEntity that = (ReservationTimeEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
