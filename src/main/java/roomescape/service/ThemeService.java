package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.controller.dto.ThemeCreateRequest;
import roomescape.domain.Theme;
import roomescape.exception.EntityNotFoundException;
import roomescape.repository.ThemeRepository;
import roomescape.repository.dto.ReservedTheme;

@Service
@Transactional
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Transactional(readOnly = true)
    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    public Theme create(
            ThemeCreateRequest themeCreateRequest
    ) {
        Theme theme = Theme.create(
                themeCreateRequest.name(),
                themeCreateRequest.description(),
                themeCreateRequest.imageUrl()
        );

        return themeRepository.persist(theme);
    }

    @Transactional(readOnly = true)
    public List<ReservedTheme> findMostReserved(
            long limit,
            LocalDate startDate,
            LocalDate endDate
    ) {
        LocalDate notNullEndDate = handleDefaultEndDate(endDate);

        return themeRepository.findMostReserved(limit, startDate, notNullEndDate);
    }

    private LocalDate handleDefaultEndDate(LocalDate endDate) {
        if (endDate == null) {
            return LocalDate.now()
                    .minusDays(1);
        }

        return endDate;
    }

    public void delete(long id) {
        boolean deleted = themeRepository.delete(id);

        if (!deleted) {
            throw new EntityNotFoundException("삭제할 테마를 조회하지 못했습니다. timeId = " + id);
        }
    }
}
