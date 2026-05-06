package roomescape.theme.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.theme.service.dto.AvailableTimesResult;
import roomescape.theme.service.dto.ThemeCommand;

@Service
@Transactional(readOnly = true)
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Transactional
    public Theme save(ThemeCommand command) {
        if (themeRepository.existsByName(command.name())) {
            throw new IllegalArgumentException("이미 존재하는 테마 이름입니다.");
        }

        Theme theme = Theme.create(command.name(), command.description(), command.thumbnailUrl());
        return themeRepository.save(theme);
    }

    @Transactional
    public void deleteById(Long id) {
        try {
            themeRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("예약에 사용 중인 테마는 삭제할 수 없습니다.");
        }
    }

    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    public AvailableTimesResult findAvailableTimes(Long themeId, LocalDate date) {
        return new AvailableTimesResult(themeRepository.findAvailableTimes(themeId, date));
    }
}
