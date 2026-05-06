package roomescape.reservation.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import roomescape.common.exception.NotFoundException;
import roomescape.reservation.domain.Reservation;

public class FakeReservationRepository implements ReservationRepository {

    private final Map<Long, Reservation> store = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<Reservation> findAll() {
        return store.values().stream().toList();
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Long save(Reservation reservation) {
        Long id = idGenerator.getAndIncrement();
        Reservation saved = Reservation.of(id, reservation.name(), reservation.date(), reservation.time(), reservation.theme(), reservation.status());
        store.put(id, saved);
        return id;
    }

    public void saveAll(List<Reservation> reservations) {
        for (Reservation reservation : reservations) {
            save(reservation);
        }
    }

    @Override
    public void delete(Long id) {
        Reservation removed = store.remove(id);
        if (removed == null) {
            throw new NotFoundException("예약을 삭제할 수 없습니다.");
        }
    }

    //TODO: themeId 파라미터 추가
    @Override
    public boolean existsByDateAndTimeId(LocalDate date, LocalTime time) {
        return store.values().stream()
                .anyMatch(reservation -> reservation.date().equals(date) && reservation.time().equals(time));
    }


}
