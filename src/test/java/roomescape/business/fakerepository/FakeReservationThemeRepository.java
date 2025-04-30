package roomescape.business.fakerepository;

import java.util.ArrayList;
import java.util.List;
import roomescape.business.ReservationTheme;
import roomescape.persistence.ReservationThemeRepository;

public final class FakeReservationThemeRepository implements ReservationThemeRepository {

    private final List<ReservationTheme> themes = new ArrayList<>();

    @Override
    public List<ReservationTheme> findAll() {
        return themes;
    }
}
