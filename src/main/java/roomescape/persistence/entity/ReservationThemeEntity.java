package roomescape.persistence.entity;

import java.util.Objects;
import roomescape.business.domain.reservation.ReservationTheme;

public final class ReservationThemeEntity {

    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public ReservationThemeEntity(Long id, String name, String description, String thumbnail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    private ReservationThemeEntity(String name, String description, String thumbnail) {
        this(null, name, description, thumbnail);
    }

    public static ReservationThemeEntity fromDomain(ReservationTheme reservationTheme) {
        if (reservationTheme.getId() != null) {
            return new ReservationThemeEntity(
                    reservationTheme.getId(),
                    reservationTheme.getName(),
                    reservationTheme.getDescription(),
                    reservationTheme.getThumbnail());
        }
        return new ReservationThemeEntity(
                reservationTheme.getName(),
                reservationTheme.getDescription(),
                reservationTheme.getThumbnail()
        );
    }

    public ReservationThemeEntity copyWithId(Long id) {
        return new ReservationThemeEntity(id, name, description, thumbnail);
    }

    public ReservationTheme toDomain() {
        if (id == null) {
            throw new IllegalArgumentException("예약 테마 엔티티의 ID가 null이어서 도메인 객체로 변환할 수 없습니다.");
        }
        return new ReservationTheme(id, name, description, thumbnail);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (id == null || ((ReservationThemeEntity) o).id == null) {
            return false;
        }
        ReservationThemeEntity that = (ReservationThemeEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
