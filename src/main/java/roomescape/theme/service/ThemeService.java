package roomescape.theme.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import roomescape.exception.AlreadyExistException;
import roomescape.exception.ResourceNotFoundException;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

@RequiredArgsConstructor
@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public Long save(final String name, final String description, final String thumbnail) {
        final long count = themeRepository.countByName(name);
        if (count >= 1) {
            throw new AlreadyExistException("해당 테마명이 이미 존재합니다. name = " + name);
        }

        final Theme theme = new Theme(name, description, thumbnail);

        return themeRepository.save(theme);
    }

    public void deleteById(final Long id) {
        themeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당 테마 데이터가 존재하지 않습니다. id = " + id));

        try {
            themeRepository.deleteById(id);
        } catch (final DataIntegrityViolationException e) {
            throw new AlreadyExistException("해당 테마를 사용하고 있는 예약 정보가 존재합니다. id = " + id);
        }
    }

    public Theme getById(final Long id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당 테마 데이터가 존재하지 않습니다. id = " + id));
    }

    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    public List<Theme> findPopularThemes() {
        return themeRepository.findTop10ThemesByReservationCountWithin7Days();
    }
}
