package roomescape.theme.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.theme.service.dto.AvailableTimesResult;
import roomescape.theme.service.dto.ThemeCommand;
import roomescape.theme.service.dto.ThemeResult;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Transactional
    public ThemeResult save(ThemeCommand command) {
        if (themeRepository.existsByName(command.name())) {
            throw new IllegalArgumentException("이미 존재하는 테마 이름입니다.");
        }

        Theme theme = Theme.create(command.name(), command.description(), command.thumbnailUrl());
        return ThemeResult.from(themeRepository.save(theme));
    }

    @Transactional
    public void deleteById(Long id) {
        try {
            themeRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("예약에 사용 중인 테마는 삭제할 수 없습니다.");
        }
    }

    public List<ThemeResult> findAll() {
        return themeRepository.findAll()
                .stream()
                .map(ThemeResult::from)
                .toList();
    }

    public AvailableTimesResult findAvailableTimes(Long themeId, LocalDate date) {
        return new AvailableTimesResult(themeRepository.findAvailableTimes(themeId, date));
    }

    public Theme getById(Long id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 테마가 존재하지 않습니다."));
    }
}
