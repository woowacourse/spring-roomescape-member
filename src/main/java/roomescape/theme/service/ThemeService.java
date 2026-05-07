package roomescape.theme.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.controller.dto.ThemeRequest;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Transactional
    public Theme save(ThemeRequest request) {
        if (themeRepository.existsByName(request.name())) {
            throw new IllegalArgumentException("이미 존재하는 테마 이름입니다.");
        }

        Theme theme = Theme.create(request.name(), request.description(), request.thumbnailUrl());
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

    public List<Theme> findPopularThemes(int period, int limit) {
        return themeRepository.findPopularThemes(period, limit);
    }

    public Theme getById(Long id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 테마가 존재하지 않습니다."));
    }
}
