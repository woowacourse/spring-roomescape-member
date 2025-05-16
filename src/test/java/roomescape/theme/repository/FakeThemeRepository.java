package roomescape.theme.repository;

import roomescape.reservation.entity.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.entity.Theme;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class FakeThemeRepository implements ThemeRepository {

    private final List<Theme> entities = new ArrayList<>();
    private final ReservationRepository reservationRepository;

    public FakeThemeRepository(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Theme save(Theme entity) {
        if (entity.getId() == null) {
            final long nextId = entities.stream()
                    .mapToLong(Theme::getId)
                    .max()
                    .orElse(0) + 1;
            entity = new Theme(
                    nextId,
                    entity.getName(),
                    entity.getDescription(),
                    entity.getThumbnail()
            );
        }
        entities.add(entity);
        return entity;
    }

    @Override
    public List<Theme> findAll() {
        return Collections.unmodifiableList(entities);
    }

    @Override
    public boolean deleteById(Long id) {
        return entities.removeIf(entity -> entity.getId().equals(id));
    }

    @Override
    public Optional<Theme> findById(Long id) {
        return entities.stream()
                .filter(entity -> entity.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Theme> findByName(String name) {
        return entities.stream()
                .filter(entity -> entity.getName().equals(name))
                .findFirst();
    }

    @Override
    public List<Theme> findPopularThemesByDateRangeAndLimit(LocalDate startDate, LocalDate endDate, final int limit) {
        Set<Long> themeIds = reservationRepository.findAll()
                .stream()
                .filter(reservation -> isBetween(startDate, endDate, reservation))
                .map(Reservation::getThemeId)
                .collect(Collectors.groupingBy(themeId -> themeId, Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        List<Theme> result = new ArrayList<>();
        for (Long themeId : themeIds) {
            Theme theme = entities.stream()
                    .filter(entity -> entity.getId().equals(themeId))
                    .findFirst()
                    .get();
            result.add(theme);
        }
        for (Theme entity : entities) {
            if (result.stream().noneMatch(theme -> theme.getId().equals(entity.getId()))) {
                result.add(entity);
            }
        }
        return Collections.unmodifiableList(result);
    }

    private boolean isBetween(LocalDate startDate, LocalDate endDate, Reservation reservation) {
        return (reservation.getDate().isAfter(startDate) || reservation.getDate().isEqual(startDate))
                && (reservation.getDate().isBefore(endDate) || reservation.getDate().isEqual(endDate));
    }
}
