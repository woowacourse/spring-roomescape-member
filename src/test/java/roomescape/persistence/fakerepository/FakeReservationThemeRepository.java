package roomescape.persistence.fakerepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;
import roomescape.business.domain.reservation.ReservationTheme;
import roomescape.persistence.ReservationThemeRepository;
import roomescape.persistence.entity.ReservationThemeEntity;

@Repository
public class FakeReservationThemeRepository implements ReservationThemeRepository, FakeRepository {

    private final List<ReservationThemeEntity> themes = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<ReservationTheme> findAll() {
        return themes.stream()
                .map(ReservationThemeEntity::toDomain)
                .toList();
    }

    @Override
    public ReservationTheme add(ReservationTheme reservationTheme) {
        ReservationThemeEntity newThemeEntity = ReservationThemeEntity.fromDomain(reservationTheme)
                .copyWithId(idGenerator.getAndIncrement());
        themes.add(newThemeEntity);
        return newThemeEntity.toDomain();
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
    public Optional<ReservationTheme> findById(Long id) {
        return themes.stream()
                .filter(theme -> theme.getId().equals(id))
                .findFirst()
                .map(ReservationThemeEntity::toDomain);
    }

    @Override
    public List<ReservationTheme> findByStartDateAndEndDateOrderByReservedDesc(LocalDate start,
                                                                               LocalDate end,
                                                                               int limit) {
        return themes.stream()
                .map(ReservationThemeEntity::toDomain)
                .toList();
    }

    @Override
    public void clear() {
        themes.clear();
        idGenerator.set(1);
    }
}
