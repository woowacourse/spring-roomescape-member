package roomescape.reservation.repository;

import roomescape.reservation.entity.Reservation;
import roomescape.reservation.repository.dto.ReservationWithFilterRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FakeReservationRepository implements ReservationRepository {
    private final List<Reservation> entities = new ArrayList<>();

    @Override
    public Reservation save(Reservation entity) {
        if (entity.getId() == null) {
            final long nextId = entities.stream()
                    .mapToLong(Reservation::getId)
                    .max()
                    .orElse(0) + 1;
            entity = new Reservation(
                    nextId,
                    entity.getMemberId(),
                    entity.getDate(),
                    entity.getTime(),
                    entity.getThemeId()
            );
        }
        entities.add(entity);
        return entity;
    }

    @Override
    public boolean deleteById(Long id) {
        return entities.removeIf(entity -> entity.getId().equals(id));
    }

    @Override
    public List<Reservation> findAll() {
        return Collections.unmodifiableList(entities);
    }

    @Override
    public List<Reservation> findAllByFilter(ReservationWithFilterRequest filterRequest) {
        List<Reservation> result = new ArrayList<>(entities);
        if (filterRequest.from() != null) {
             result = result.stream()
                     .filter(reservation -> reservation.getDate().isAfter(filterRequest.from())
                             || reservation.getDate().isEqual(filterRequest.from()))
                     .toList();
        }
        if (filterRequest.to() != null) {
            result = result.stream()
                    .filter(reservation -> reservation.getDate().isBefore(filterRequest.to())
                            || reservation.getDate().isEqual(filterRequest.to()))
                    .toList();
        }
        if (filterRequest.themeId() != null) {
            result = result.stream()
                    .filter(reservation -> reservation.getThemeId().equals(filterRequest.themeId()))
                    .toList();
        }
        if (filterRequest.memberId() != null) {
            result = result.stream()
                    .filter(reservation -> reservation.getMemberId().equals(filterRequest.memberId()))
                    .toList();
        }
        return Collections.unmodifiableList(result);
    }

    @Override
    public List<Reservation> findAllByTimeId(Long id) {
        return entities.stream()
                .filter(reservation -> reservation.getTimeId().equals(id))
                .toList();
    }

    @Override
    public Optional<Reservation> findDuplicatedWith(Reservation other) {
        return entities.stream()
                .filter(entity -> entity.getDate().isEqual(other.getDate())
                        && entity.getStartAt().equals(other.getStartAt()))
                .findFirst();
    }
}
