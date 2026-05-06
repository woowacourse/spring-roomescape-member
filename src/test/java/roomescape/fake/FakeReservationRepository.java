package roomescape.fake;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.NoArgsConstructor;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;

@NoArgsConstructor
public class FakeReservationRepository implements ReservationRepository {

    private final Map<Long, Reservation> reservations = new HashMap<>();
    private Long idHoler = 1L;

    @Override
    public List<Reservation> findAll() {
        return List.of();
    }

    @Override
    public Reservation save(Reservation reservation) {
        Reservation savedReservation = reservation.withId(idHoler);
        reservations.put(idHoler++, savedReservation);
        return savedReservation;
    }

    @Override
    public void delete(Long id) {
    }

    @Override
    public Boolean existsByDateAndThemeAndTime(LocalDate date, Long themeId, Long timeId) {
        return reservations.values().stream()
                .anyMatch(savedReservation -> (savedReservation.getThemeId().equals(themeId) &&
                        savedReservation.getTimeId().equals(timeId) &&
                        savedReservation.getDate().equals(date)));
    }
}
