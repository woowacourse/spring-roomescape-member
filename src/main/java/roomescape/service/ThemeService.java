package roomescape.service;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Duration;
import roomescape.domain.Theme;
import roomescape.exception.EntityNotFoundException;
import roomescape.repository.ThemeRepository;
import roomescape.repository.dto.ReservedTheme;
import roomescape.service.dto.ThemeCreateCommand;

@Service
@RequiredArgsConstructor
@Slf4j
public class ThemeService {

    private final ThemeRepository themeRepository;

    @Transactional
    public Theme create(
            ThemeCreateCommand command
    ) {
        UUID id = UUID.randomUUID();
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
        return themeRepository.findMostReserved(limit, duration);
    }

    @Transactional
    public void delete(UUID themeId) {
        boolean deleted = themeRepository.delete(themeId);

        if (!deleted) {
            throw new EntityNotFoundException(
                    "삭제할 테마를 조회하지 못했습니다.",
                    "themeId = " + themeId
            );
        }
    }
}
