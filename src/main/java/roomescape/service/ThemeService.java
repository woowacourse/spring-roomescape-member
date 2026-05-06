package roomescape.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.controller.dto.ThemeCreateRequest;
import roomescape.domain.Duration;
import roomescape.domain.Theme;
import roomescape.exception.EntityNotFoundException;
import roomescape.repository.ThemeRepository;
import roomescape.repository.dto.ReservedTheme;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ThemeService {

    private final ThemeRepository themeRepository;

    @Transactional(readOnly = true)
    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    public Theme create(
            ThemeCreateRequest createRequest
    ) {
        Theme theme = Theme.create(
                createRequest.name(),
                createRequest.description(),
                createRequest.imageUrl()
        );

        return themeRepository.persist(theme);
    }

    @Transactional(readOnly = true)
    public List<ReservedTheme> findMostReserved(
            long limit,
            Duration duration
    ) {
        return themeRepository.findMostReserved(limit, duration);
    }

    public void delete(long id) {
        boolean deleted = themeRepository.delete(id);

        if (!deleted) {
            throw new EntityNotFoundException(
                    "삭제할 테마를 조회하지 못했습니다.",
                    "themeId = " + id
            );
        }
    }
}
