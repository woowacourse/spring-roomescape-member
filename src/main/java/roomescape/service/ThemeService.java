package roomescape.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.global.exception.DuplicateEntityException;
import roomescape.global.exception.EntityNotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.web.dto.theme.ThemeRequest;
import roomescape.web.dto.theme.ThemeResponse;
import roomescape.web.dto.theme.ThemeTimesResponse;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    @Transactional
    public ThemeResponse register(ThemeRequest request) {
        validateDuplicateName(request.name());

        Theme theme = new Theme(request.name(), request.description(), request.thumbnailImageUrl());
        return ThemeResponse.from(themeRepository.save(theme));
    }

    @Transactional
    public void remove(Long id) {
        Theme theme = themeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 테마 정보입니다."));

        theme.deactivate();
        themeRepository.update(theme);
    }

    public List<ThemeTimesResponse> getThemeReservationStatus(Long id, LocalDate date) {
        if (themeRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("존재하지 않는 테마 정보입니다.");
        }

        Set<Long> reservedTimeIds = reservationRepository.findUnavailableTimeIdsByThemeIdAndDate(id, date);

        List<ThemeTimesResponse> responses = new ArrayList<>();
        reservationTimeRepository.findTimeSlotsForReservationStatus().forEach(
                time -> {
                    boolean reservable = isReservable(time, date, reservedTimeIds);
                    responses.add(ThemeTimesResponse.of(time, reservable));
                });

        return responses;
    }

    public List<ThemeResponse> getAllActiveThemesByPaging(int page, int size) {
        return themeRepository.findAllActiveThemesByPaging(page, size)
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

    private boolean isReservable(ReservationTime time, LocalDate date, Set<Long> reservedTimeIds) {
        return time.isAvailableAt(date) && !reservedTimeIds.contains(time.getId());
    }

    private void validateDuplicateName(String name) {
        if (themeRepository.isActiveByName(name)) {
            throw new DuplicateEntityException("이미 존재하는 테마입니다. 테마 명: %s", name);
        }
    }
}
