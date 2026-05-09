package roomescape.theme.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.DuplicateResourceException;
import roomescape.exception.ResourceInUseException;
import roomescape.exception.ResourceNotFoundException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.controller.dto.ThemeRequest;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ThemeService {

    private static final int DEFAULT_POPULAR_PERIOD = 7;
    private static final int DEFAULT_POPULAR_LIMIT = 10;

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;
    private final Clock clock;

    public ThemeService(
            ThemeRepository themeRepository,
            ReservationRepository reservationRepository,
            Clock clock
    ) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
        this.clock = clock;
    }

    @Transactional
    public Theme save(ThemeRequest request) {
        validateDuplicateName(request);

        Theme theme = Theme.create(request.name(), request.description(), request.thumbnailUrl());
        return themeRepository.save(theme);
    }

    @Transactional
    public void deleteById(Long id) {
        if (reservationRepository.existsByThemeId(id)) {
            throw new ResourceInUseException("이 테마를 참조하는 예약이 있어 삭제할 수 없습니다. ID: " + id);
        }

        themeRepository.deleteById(id);
    }

    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    public List<Theme> findPopularThemes() {
        LocalDate endDate = LocalDate.now(clock);
        LocalDate startDate = endDate.minusDays(DEFAULT_POPULAR_PERIOD);

        return themeRepository.findPopularThemes(
                startDate,
                endDate,
                DEFAULT_POPULAR_LIMIT
        );
    }

    @Transactional
    public Theme update(Long id, ThemeRequest request) {
        Theme theme = getById(id);

        if (!theme.getName().equals(request.name())) {
            validateDuplicateName(request);
        }

        Theme updatedTheme = theme.update(request.name(), request.description(), request.thumbnailUrl());
        themeRepository.update(updatedTheme);

        return updatedTheme;
    }

    private void validateDuplicateName(ThemeRequest request) {
        if (themeRepository.existsByName(request.name())) {
            throw new DuplicateResourceException("이미 존재하는 테마 이름입니다.");
        }
    }

    public Theme getById(Long id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당 ID의 테마가 존재하지 않습니다. ID: " + id));
    }
}
