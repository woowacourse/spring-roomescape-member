package roomescape.theme.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.controller.dto.CreateThemeRequest;
import roomescape.theme.controller.dto.ThemeRankResponse;
import roomescape.theme.controller.dto.ThemeResponse;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.theme.repository.dto.CreateThemeParams;
import roomescape.theme.repository.dto.GetThemeRankingsInRecentDaysParams;
import roomescape.time.controller.dto.request.GetAvailableTimesRequest;
import roomescape.time.controller.dto.response.AvailableReservationTimeResponse;
import roomescape.time.controller.dto.response.ThemeReservationTimesResponse;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;
import roomescape.time.repository.dto.FindReservedTimeParams;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    @Transactional
    public ThemeResponse addTheme(CreateThemeRequest request) {
        CreateThemeParams params = new CreateThemeParams(
                request.name(),
                request.description(),
                request.imageUrl()
        );
        Theme savedTheme = themeRepository.save(params);

        return ThemeResponse.from(savedTheme);
    }

    public List<ThemeResponse> findAllThemes() {
        return themeRepository.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    @Transactional
    public void removeRegisteredTheme(Long id) {
        themeRepository.deleteById(id);
    }

    public List<ThemeRankResponse> getThemeRankingsInRecentDays(int days, int limit) {
        List<Theme> themes = themeRepository.findThemesOrderedByReservationCount(
                GetThemeRankingsInRecentDaysParams.of(days, limit)
        );

        return IntStream.range(0, themes.size())
                .mapToObj(index -> {
                    int rank = index + 1;
                    ThemeResponse themeResponse = ThemeResponse.from(themes.get(index));
                    return new ThemeRankResponse(rank, themeResponse);
                })
                .toList();
    }

    public ThemeReservationTimesResponse findAllAvailableTimes(GetAvailableTimesRequest request) {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        FindReservedTimeParams params = new FindReservedTimeParams(request.themeId(), request.date());

        Set<Long> reservedTimeIds = new HashSet<>(reservationTimeRepository.findIdByCondition(params));

        List<AvailableReservationTimeResponse> availableTimes = reservationTimes.stream()
                .map(time -> createResponse(time, reservedTimeIds))
                .filter(response -> isMatchCondition(request.available(), response.available()))
                .toList();
        ThemeResponse theme = ThemeResponse.from(themeRepository.findById(request.themeId()));
        return ThemeReservationTimesResponse.from(theme, availableTimes);
    }

    private AvailableReservationTimeResponse createResponse(ReservationTime time, Set<Long> reservedIdSet) {
        boolean isAvailable = !reservedIdSet.contains(time.getId());
        return new AvailableReservationTimeResponse(time.getId(), time.getStartAt(), isAvailable);
    }

    private boolean isMatchCondition(Boolean requestAvailable, boolean isAvailable) {
        return requestAvailable == null || requestAvailable.equals(isAvailable);
    }
}

