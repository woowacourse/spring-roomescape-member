//package roomescape.fixture;
//
//import java.time.LocalDate;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import roomescape.domain.Reservation;
//import roomescape.repository.reservation.ReservationRepository;
//
//public class ReservationRepositoryStub implements ReservationRepository {
//    private final Map<Long, Reservation> reservations = new HashMap<>();
//
//    private Long id = 1L;
//
//    @Override
//    public Reservation add(Reservation reservation) {
//        Reservation newReservation = reservation.withId(id);
//        reservations.put(id++, newReservation);
//        return newReservation;
//    }
//
//    @Override
//    public int deleteById(Long id) {
//        Reservation removedReservation = reservations.remove(id);
//        if (removedReservation == null) {
//            return 0;
//        }
//        return 1;
//    }
//
//    @Override
//    public List<Reservation> findAll() {
//        return reservations.values().stream().toList();
//    }
//
//    @Override
//    public boolean existsByDateAndTime(LocalDate date, Long id) {
//        return reservations.values().stream()
//                .anyMatch(reservation -> reservation.getId() == id && reservation.getDate() == date);
//    }
//
//    @Override
//    public boolean existsByTimeId(Long id) {
//        return reservations.values().stream().anyMatch(reservation -> reservation.getId() == id);
//    }
//}
