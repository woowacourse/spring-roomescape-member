package roomescape.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.controller.dto.AvailableReservationTimeResponse;
import roomescape.controller.dto.CreateResrvationTimeRequest;
import roomescape.controller.dto.GetAvailableTimesRequest;
import roomescape.controller.dto.ReservationTimeResponse;
import roomescape.controller.dto.ThemeResponse;
import roomescape.controller.dto.ThemeReservationTimesResponse;
import roomescape.domain.ReservationTime;
import roomescape.global.exception.DuplicateReservationTimeException;
import roomescape.global.exception.ThemeNotFoundException;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    @Transactional
    public ReservationTimeResponse addReservationTime(CreateResrvationTimeRequest request) {
        if (reservationTimeRepository.existsByStartAt(request.startAt())) {
            throw new DuplicateReservationTimeException();
        }
        ReservationTime savedReservationTime = reservationTimeRepository.save(ReservationTime.createNew(request.startAt()));

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

        Set<Long> reservedTimeIds = new HashSet<>(
                reservationTimeRepository.findIdByCondition(request.themeId(), request.date()));

        List<AvailableReservationTimeResponse> availableTimes = reservationTimes.stream()
                .map(time -> createResponse(time, reservedTimeIds))
                .filter(response -> isMatchCondition(request.available(), response.available()))
                .toList();
        ThemeResponse theme = ThemeResponse.from(themeRepository.findById(request.themeId())
                .orElseThrow(ThemeNotFoundException::new));
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
