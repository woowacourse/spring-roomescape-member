package roomescape.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Duration;
import roomescape.domain.EntityId;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.exception.DataReferencedException;
import roomescape.exception.EntityNotFoundException;
import roomescape.exception.InUseEntityException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.dto.ReservedTheme;
import roomescape.service.dto.ThemeCreateCommand;

@Service
@RequiredArgsConstructor
@Slf4j
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public Theme create(
            ThemeCreateCommand command
    ) {
        EntityId id = EntityId.random();
        Theme theme = new Theme(
                id,
                command.name(),
                command.description(),
                command.imageUrl()
        );

        return themeRepository.persist(theme);
    }

    @Transactional(readOnly = true)
    public List<Theme> findAll() {
        return themeRepository.findAll();
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
        try {
            boolean deleted = themeRepository.delete(themeId);
            validateDeleted(deleted, themeId);
        } catch (DataReferencedException exception) {
            throw new InUseEntityException(
                    "사용 중인 테마는 제거할 수 없습니다.",
                    "themeId = " + themeId,
                    exception
            );
        }
    }

    private void validateDeleted(boolean deleted, EntityId themeId) {
        if (!deleted) {
            throw new EntityNotFoundException(
                    "삭제할 테마를 조회하지 못했습니다.",
                    "themeId = " + themeId
            );
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
            throw new EntityNotFoundException(
                    "테마를 조회할 수 없습니다.",
                    "themeId = " + themeId
            );
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
