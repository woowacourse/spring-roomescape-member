package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Theme;
import roomescape.global.exception.DuplicateEntityException;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.dto.TimeSlotProjection;
import roomescape.web.dto.ThemeRequest;
import roomescape.web.dto.ThemeResponse;
import roomescape.web.dto.ThemeTimesResponse;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    @Transactional
    public ThemeResponse register(ThemeRequest request) {
        validateDuplicateName(request.name());

        Theme theme = new Theme(request.name(), request.description(), request.thumbnailImageUrl());
        return ThemeResponse.from(themeRepository.save(theme));
    }

    @Transactional
    public void remove(Long id) {
        themeRepository.findById(id)
                .ifPresent(existingTheme -> {
                    existingTheme.deactivate();
                    themeRepository.update(existingTheme);
                });
    }

    public List<ThemeTimesResponse> getThemeReservationStatus(Long id, LocalDate date) {
        return reservationTimeRepository.findTimesByThemeWithReservationStatus(id, date)
                .stream()
                .map(projection -> toResultWithTimeCheck(projection, date))
                .toList();
    }

    public List<ThemeResponse> getAllActiveThemes() {
        return themeRepository.findAllActiveThemes()
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public List<ThemeResponse> getPopularThemes(LocalDate startDate, LocalDate endDate, int limit) {
        return themeRepository.findTopThemesByReservationCount(startDate, endDate, limit)
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }

    private ThemeTimesResponse toResultWithTimeCheck(TimeSlotProjection projection, LocalDate date) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startAt = LocalDateTime.of(date, projection.startAt());

        if (now.isAfter(startAt)) {
            return ThemeTimesResponse.of(projection, false);
        }
        return ThemeTimesResponse.from(projection);
    }

    private void validateDuplicateName(String name) {
        if (themeRepository.isActiveByName(name)) {
            throw new DuplicateEntityException("이미 존재하는 테마입니다. 테마 명: %s", name);
        }
    }
}
