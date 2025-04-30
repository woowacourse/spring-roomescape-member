package roomescape.business.fakerepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.business.ReservationTheme;
import roomescape.persistence.ReservationThemeRepository;

public final class FakeReservationThemeRepository implements ReservationThemeRepository {

    private final List<ReservationTheme> themes = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<ReservationTheme> findAll() {
        return themes;
    }

    // TODO: 매개변수로 받은 객체의 상태를 바꾸며 사용해도 되나?
    @Override
    public Long add(ReservationTheme reservationTheme) {
        long id = idGenerator.incrementAndGet();
        reservationTheme.setId(id);
        themes.add(reservationTheme);
        return id;
    }

    @Override
    public boolean existByName(String name) {
        return themes.stream()
                .anyMatch(theme -> theme.getName().equals(name));
    }

    @Override
    public void deleteById(Long id) {
        themes.removeIf(theme -> theme.getId().equals(id));
    }

    @Override
    public ReservationTheme findById(Long id) {
        return themes.stream()
                .filter(theme -> theme.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));
    }
}
