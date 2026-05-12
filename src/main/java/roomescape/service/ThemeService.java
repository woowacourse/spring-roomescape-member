package roomescape.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.controller.dto.ThemeResponse;
import roomescape.domain.Duration;
import roomescape.domain.EntityId;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.exception.EntityNotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.dto.ReservedTheme;
import roomescape.service.dto.ThemeCreateCommand;
import roomescape.service.mapper.ThemeResponseMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;
    private final ThemeResponseMapper themeResponseMapper;

    @Transactional
    public ThemeResponse create(
            ThemeCreateCommand command
    ) {
        EntityId id = EntityId.random();
        Theme theme = new Theme(
                id,
                command.name(),
                command.description(),
                command.imageUrl()
        );

        Theme persisted = themeRepository.persist(theme);
        return themeResponseMapper.map(persisted);
    }

    @Transactional(readOnly = true)
    public List<ThemeResponse> findAll() {
        return themeRepository.findAll()
                .stream()
                .map(themeResponseMapper::map)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReservedTheme> findMostReserved(
            long limit,
            Duration duration
    ) {
        List<Reservation> reservations = reservationRepository.findBetweenDuration(duration);
        Map<EntityId, Long> themeReservedCounts = collectCountByThemeId(reservations);
        Map<EntityId, Theme> themes = themeRepository.findByIds(themeReservedCounts.keySet());

        return themeReservedCounts.entrySet()
                .stream()
                .sorted(Map.Entry.<EntityId, Long>comparingByValue().reversed())
                .limit(limit)
                .map(countEntry -> mapToReservedTheme(countEntry, themes))
                .toList();
    }

    @Transactional
    public void delete(EntityId themeId) {
        validateThemeNotUsed(themeId);

        boolean deleted = themeRepository.delete(themeId);
        validateDeleted(deleted, themeId);
    }

    private void validateThemeNotUsed(EntityId themeId) {
        if (reservationRepository.existByThemeId(themeId)) {
            throw new IllegalStateException("사용되지 않는 테마만 제거할 수 있습니다. themeId = " + themeId.getValueAsString());
        }
    }

    private void validateDeleted(boolean deleted, EntityId themeId) {
        if (!deleted) {
            throw new EntityNotFoundException("삭제할 테마를 조회하지 못했습니다. themeId = " + themeId);
        }
    }

    private Map<EntityId, Long> collectCountByThemeId(List<Reservation> reservations) {
        return reservations.stream()
                .collect(Collectors.groupingBy(
                        Reservation::themeId,
                        Collectors.counting()
                ));
    }

    private ReservedTheme mapToReservedTheme(
            Map.Entry<EntityId, Long> themeReservedCount,
            Map<EntityId, Theme> themes
    ) {
        EntityId themeId = themeReservedCount.getKey();
        Theme theme = themes.get(themeId);

        if (theme == null) {
            throw new EntityNotFoundException("테마를 조회할 수 없습니다. themeId = " + themeId);
        }

        return new ReservedTheme(
                theme.id().getValueAsString(),
                theme.name(),
                theme.description(),
                theme.imageUrl(),
                themeReservedCount.getValue()
        );
    }
}
