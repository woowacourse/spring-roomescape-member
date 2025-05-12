package roomescape.theme.applcation;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import roomescape.exception.resource.AlreadyExistException;
import roomescape.exception.resource.ResourceNotFoundException;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeCommandRepository;
import roomescape.theme.domain.ThemeQueryRepository;
import roomescape.theme.ui.dto.CreateThemeRequest;
import roomescape.theme.ui.dto.ThemeResponse;

@Service
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeCommandRepository themeCommandRepository;
    private final ThemeQueryRepository themeQueryRepository;

    public ThemeResponse create(final CreateThemeRequest request) {
        if (themeQueryRepository.existsByName(request.name())) {
            throw new AlreadyExistException("해당 테마명이 이미 존재합니다. name = " + request.name());
        }

        final Theme theme = new Theme(request.name(), request.description(), request.thumbnail());
        final Long id = themeCommandRepository.save(theme);
        final Theme found = themeQueryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당 테마 데이터가 존재하지 않습니다. id = " + id));

        return ThemeResponse.from(found);
    }

    public void delete(final Long id) {
        themeQueryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당 테마 데이터가 존재하지 않습니다. id = " + id));

        try {
            themeCommandRepository.deleteById(id);
        } catch (final DataIntegrityViolationException e) {
            throw new AlreadyExistException("해당 테마를 사용하고 있는 예약 정보가 존재합니다. id = " + id);
        }
    }

    public List<ThemeResponse> findAll() {
        return themeQueryRepository.findAll()
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public List<ThemeResponse> findPopularThemes() {
        final LocalDate dateTo = LocalDate.now();
        final LocalDate dateFrom = dateTo.minusDays(7);
        final int limit = 10;

        return themeQueryRepository.findTopNThemesByReservationCountInDateRange(dateFrom, dateTo, limit)
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
