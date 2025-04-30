package roomescape.fake;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import roomescape.business.domain.PlayTime;
import roomescape.business.domain.Reservation;
import roomescape.persistence.dao.ReservationDao;
import roomescape.persistence.entity.PlayTimeEntity;
import roomescape.persistence.entity.ReservationEntity;

public class FakeReservationDao implements ReservationDao {

    private final List<ReservationEntity> reservations = new ArrayList<>();
    private final List<PlayTimeEntity> times;

    private int index = 1;

    public FakeReservationDao(final List<PlayTimeEntity> times) {
        this.times = times;
        final ReservationEntity dummy = new ReservationEntity(null, null, null, null);
        reservations.add(dummy);
    }

    @Override
    public Long save(final Reservation reservation) {
        final ReservationEntity temp = ReservationEntity.from(reservation);
        final ReservationEntity reservationEntity = new ReservationEntity(
                (long) index,
                temp.name(), temp.date(), temp.playTimeEntity()
        );
        reservations.add(index, reservationEntity);

        return (long) index++;
    }

    @Override
    public List<Reservation> findAll() {
        return reservations.stream()
                .filter(reservationEntity -> reservationEntity.id() != null)
                .filter(reservationEntity -> times.stream()
                        .filter(timeEntity -> timeEntity.id() != null)
                        .anyMatch(timeEntity -> Objects.equals(reservationEntity.playTimeEntity().id(), timeEntity.id()))
                )
                .map(ReservationEntity::toDomain)
                .toList();
    }

    @Override
    public boolean remove(final Long id) {
        try {
            reservations.remove(reservations.get(Math.toIntExact(id)));
            index--;
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    @Override
    public boolean existsByDateAndTime(final LocalDate date, final PlayTime time) {
        final String formattedDate = ReservationEntity.formatDate(date);
        final Long timeId = time.getId();

        return reservations.stream()
                .filter(reservationEntity -> reservationEntity.id() != null)
                .anyMatch(reservationEntity ->
                        reservationEntity.date().equals(formattedDate) &&
                        reservationEntity.playTimeEntity().id().equals(timeId)
                );
    }
}
