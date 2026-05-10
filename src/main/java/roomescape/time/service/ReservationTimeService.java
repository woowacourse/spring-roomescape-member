package roomescape.time.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.global.exception.DuplicateReservationTimeException;
import roomescape.theme.controller.dto.ThemeResponse;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.controller.dto.AvailableReservationTimeResponse;
import roomescape.time.controller.dto.CreateResrvationTimeRequest;
import roomescape.time.controller.dto.GetAvailableTimesRequest;
import roomescape.time.controller.dto.ReservationTimeResponse;
import roomescape.time.controller.dto.ThemeReservationTimesResponse;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;
import roomescape.time.repository.dto.CreateReservationTimeParams;
import roomescape.time.repository.dto.FindReservedTimeParams;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    @Transactional
    public ReservationTimeResponse addReservationTime(CreateResrvationTimeRequest request) {
        CreateReservationTimeParams params = new CreateReservationTimeParams(request.startAt());
        if (reservationTimeRepository.existsByStartAt(params)) {
            throw new DuplicateReservationTimeException();
        }
        ReservationTime savedReservationTime = reservationTimeRepository.save(params);

        return ReservationTimeResponse.from(savedReservationTime);
    }

    public List<ReservationTimeResponse> findAllReservationTimes() {
        return reservationTimeRepository.findAll().stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    @Transactional
    public void removeRegisteredReservationTime(Long id) {
        reservationTimeRepository.deleteById(id);
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
