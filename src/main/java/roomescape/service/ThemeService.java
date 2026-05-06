package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.DuplicateEntityException;
import roomescape.domain.Theme;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.dto.TimeSlotProjection;
import roomescape.service.command.ThemeRegisterCommand;
import roomescape.service.result.ThemeRegisterResult;
import roomescape.service.result.ThemeResult;
import roomescape.service.result.ThemeTimesResult;

@Service
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ThemeRegisterResult register(ThemeRegisterCommand command) {
        validateDuplicationName(command);

        Theme theme = new Theme(command.name(), command.description(), command.thumbnailImageUrl());

        return ThemeRegisterResult.from(themeRepository.save(theme));
    }

    public void remove(long id) {
        themeRepository.findById(id)
                .ifPresent(existingTheme -> {
                    existingTheme.deactivate();
                    themeRepository.update(existingTheme);
                });
    }

    public List<ThemeTimesResult> getThemeReservationStatus(long id, LocalDate date) {
        return reservationTimeRepository.findTimesByThemeWithReservationStatus(id, date)
                .stream()
                .map(projection -> toResultWithTimeCheck(projection, date))
                .toList();
    }

    public List<ThemeResult> getAllActiveThemes() {
        return themeRepository.findAllActiveThemes()
                .stream().map(ThemeResult::from)
                .toList();
    }

    public List<ThemeResult> getPopularThemes(LocalDate startDate, LocalDate endDate) {
        return themeRepository.findTop10ByReservationCount(startDate, endDate)
                .stream()
                .map(ThemeResult::from)
                .toList();
    }

    private ThemeTimesResult toResultWithTimeCheck(TimeSlotProjection projection, LocalDate date) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startAt = LocalDateTime.of(date, projection.startAt());

        if (now.isAfter(startAt)) {
            return ThemeTimesResult.from(projection.disabled());
        }
        return ThemeTimesResult.from(projection);
    }

    private void validateDuplicationName(ThemeRegisterCommand command) {
        if (themeRepository.isActiveByName(command.name())) {
            throw new DuplicateEntityException("이미 존재하는 테마입니다. 테마 명: %s", command.name());
        }
    }
}
