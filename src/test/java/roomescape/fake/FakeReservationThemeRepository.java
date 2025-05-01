package roomescape.fake;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import roomescape.domain.ReservationTheme;
import roomescape.repository.ReservationThemeRepository;

public class FakeReservationThemeRepository implements ReservationThemeRepository {

    private final List<ReservationTheme> repository = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public ReservationTheme save(ReservationTheme reservationTheme) {
        return null;
    }

    @Override
    public List<ReservationTheme> getAll() {
        return List.of();
    }

    @Override
    public void remove(ReservationTheme reservation) {

    }

    @Override
    public Optional<ReservationTheme> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public ReservationTheme getById(Long id) {
        return null;
    }

    @Override
    public List<ReservationTheme> orderByThemeBookedCountWithLimit(int limit) {
        return List.of();
    }
}
