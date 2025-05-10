package roomescape.theme.applcation;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import roomescape.exception.resource.AlreadyExistException;
import roomescape.exception.resource.ResourceNotFoundException;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeRepository;
import roomescape.theme.ui.dto.CreateThemeRequest;
import roomescape.theme.ui.dto.ThemeResponse;

@Service
@RequiredArgsConstructor
public class ThemeService {

    // TODO: ThemeRepository도 Command, Query 인터페이스 분리할 것
    private final ThemeRepository themeRepository;

    public ThemeResponse create(final CreateThemeRequest request) {
        if (themeRepository.existsByName(request.name())) {
            throw new AlreadyExistException("해당 테마명이 이미 존재합니다. name = " + request.name());
        }

        final Theme theme = new Theme(request.name(), request.description(), request.thumbnail());
        final Long id = themeRepository.save(theme);
        final Theme found = themeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당 테마 데이터가 존재하지 않습니다. id = " + id));

        return ThemeResponse.from(found);
    }

    public void delete(final Long id) {
        themeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당 테마 데이터가 존재하지 않습니다. id = " + id));

        try {
            themeRepository.deleteById(id);
        } catch (final DataIntegrityViolationException e) {
            throw new AlreadyExistException("해당 테마를 사용하고 있는 예약 정보가 존재합니다. id = " + id);
        }
    }

    public List<ThemeResponse> findAll() {
        return themeRepository.findAll()
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public List<ThemeResponse> findPopularThemes() {
        return themeRepository.findTop10ThemesByReservationCountWithin7Days()
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
