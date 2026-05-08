package roomescape.fixture.fake;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.NoArgsConstructor;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.repository.ReservationDetail;
import roomescape.reservation.domain.repository.ReservationRepository;

@NoArgsConstructor
public class FakeReservationRepository implements ReservationRepository {

    private final Map<Long, Reservation> reservations = new LinkedHashMap<>();
    private Long idHolder = 1L;

    @Override
    public List<ReservationDetail> findAll() {
        return List.of();
    }

    @Override
    public Reservation save(Reservation reservation) {
        Reservation savedReservation = reservation.withId(idHolder);
        reservations.put(idHolder++, savedReservation);
        return savedReservation;
    }

    @Override
    public Integer delete(Long id) {
        int beforeSize = reservations.size();
        reservations.remove(id);
        int afterSize = reservations.size();

        if (beforeSize != afterSize) {
            return 1;
        }

        return 0;
    }

    @Override
    public Boolean existsByDateAndThemeAndTime(LocalDate date, Long themeId, Long timeId) {
        return reservations.values().stream()
                .anyMatch(savedReservation -> (savedReservation.getThemeId().equals(themeId) &&
                        savedReservation.getTimeId().equals(timeId) &&
                        savedReservation.getDate().equals(date)));
    }
}
