package roomescape.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationRequest;

public class CollectionReservationRepository implements ReservationRepository {
    private final List<Reservation> reservations;
    private final AtomicLong atomicLong;
    private final CollectionReservationTimeRepository timeRepository;

    public CollectionReservationRepository(CollectionReservationTimeRepository timeRepository) {
        this(new ArrayList<>(), new AtomicLong(0), timeRepository);
    }

    public CollectionReservationRepository(List<Reservation> reservations, AtomicLong atomicLong,
                                           CollectionReservationTimeRepository timeRepository) {
        this.reservations = reservations;
        this.atomicLong = atomicLong;
        this.timeRepository = timeRepository;
    }

    public CollectionReservationRepository(List<Reservation> reservations,
                                           CollectionReservationTimeRepository timeRepository) {
        this(reservations, new AtomicLong(0), timeRepository);
    }

    private static Predicate<ReservationTime> sameId(ReservationRequest reservationRequest) {
        return reservationTime -> reservationTime.getId() == reservationRequest.timeId();
    }

    @Override
    public Reservation save(Reservation reservation) {
        //TODO 안해도 되는지 확인하기
        ReservationTime findTime = timeRepository.findAll().stream()
                .filter(reservationTime -> reservationTime.getId() == reservation.getReservationTime().getId())
                .findFirst()
                .get();
        Reservation saved = new Reservation(atomicLong.incrementAndGet(), reservation.getName(), reservation.getDate(),
                findTime, reservation.getTheme());
        reservations.add(saved);
        return saved;
    }

    @Override
    public List<Reservation> findAll() {
        return reservations.stream()
                .sorted()
                .toList();
    }

    @Override
    public void delete(long id) {
        reservations.stream()
                .filter(reservation -> reservation.hasSameId(id))
                .findAny()
                .ifPresent(reservations::remove);
    }
}
