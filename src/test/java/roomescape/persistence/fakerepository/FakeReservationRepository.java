package roomescape.persistence.fakerepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;
import roomescape.business.domain.reservation.Reservation;
import roomescape.persistence.ReservationRepository;
import roomescape.persistence.entity.ReservationEntity;
import roomescape.presentation.admin.dto.ReservationQueryCondition;

@Repository
public class FakeReservationRepository implements ReservationRepository, FakeRepository {

    private final List<ReservationEntity> reservations = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<Reservation> findAll() {
        return reservations.stream()
                .map(ReservationEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return reservations.stream()
                .filter(reservation -> Objects.equals(reservation.getId(), id))
                .findFirst()
                .map(ReservationEntity::toDomain);
    }

    @Override
    public Reservation add(Reservation reservation) {
        ReservationEntity newReservationEntity = ReservationEntity.fromDomain(reservation)
                .copyWithId(idGenerator.getAndIncrement());
        reservations.add(newReservationEntity);
        return newReservationEntity.toDomain();
    }

    @Override
    public void deleteById(Long id) {
        reservations.removeIf(reservation -> Objects.equals(reservation.getId(), id));
    }

    @Override
    public boolean existsByReservation(Reservation otherReservation) {
        ReservationEntity otherReservationEntity = ReservationEntity.fromDomain(otherReservation);
        return reservations.stream()
                .anyMatch(reservation -> reservation.isSameReservation(otherReservationEntity));
    }

    @Override
    public boolean existsByTimeId(Long timeId) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.getTimeEntity().getId().equals(timeId));
    }

    @Override
    public boolean existsByThemeId(Long id) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.getThemeEntity().getId().equals(id));
    }

    @Override
    public List<Reservation> findAllByCondition(ReservationQueryCondition condition) {
        return reservations.stream()
                .filter(reservation -> reservation.getDate().equals(condition.dateFrom().toString()))
                .filter(reservation -> reservation.getDate().equals(condition.dateTo().toString()))
                .filter(reservation -> reservation.getMemberEntity().getId().equals(condition.memberId()))
                .filter(reservation -> reservation.getThemeEntity().getId().equals(condition.themeId()))
                .map(ReservationEntity::toDomain)
                .toList();
    }

    @Override
    public void clear() {
        reservations.clear();
        idGenerator.set(1);
    }
}
