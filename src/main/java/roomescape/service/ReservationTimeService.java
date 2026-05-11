package roomescape.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.controller.dto.reservationtime.AvailableReservationTimeResponse;
import roomescape.controller.dto.reservationtime.AvailableReservationTimesQuery;
import roomescape.controller.dto.reservationtime.AvailableReservationTimesResponse;
import roomescape.controller.dto.reservationtime.ReservationTimeResponse;
import roomescape.controller.dto.theme.ThemeResponse;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservedTimes;
import roomescape.global.exception.DuplicateReservationTimeException;
import roomescape.global.exception.ThemeNotFoundException;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.CreateReservationTimeCommand;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    @Transactional
    public ReservationTimeResponse createReservationTime(CreateReservationTimeCommand command) {
        if (reservationTimeRepository.existsByStartAt(command.startAt())) {
            throw new DuplicateReservationTimeException();
        }
        ReservationTime savedReservationTime = reservationTimeRepository.save(
                ReservationTime.createNew(command.startAt()));

        return ReservationTimeResponse.from(savedReservationTime);
    }

    public List<ReservationTimeResponse> getReservationTimes() {
        return reservationTimeRepository.findAll().stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    @Transactional
    public void deleteReservationTime(Long id) {
        reservationTimeRepository.deleteById(id);
    }

    public AvailableReservationTimesResponse getAvailableReservationTimes(AvailableReservationTimesQuery request) {
        ThemeResponse theme = ThemeResponse.from(themeRepository.findById(request.themeId())
                .orElseThrow(ThemeNotFoundException::new));

        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        ReservedTimes reservedTimes = new ReservedTimes(
                reservationTimeRepository.findReservedTimeIds(request.themeId(), request.date())
        );

        List<AvailableReservationTimeResponse> availableTimes = reservationTimes.stream()
                .map(time -> createResponse(time, reservedTimes))
                .filter(response -> isMatchCondition(request.available(), response.available()))
                .toList();

        return AvailableReservationTimesResponse.from(theme, availableTimes);
    }

    private AvailableReservationTimeResponse createResponse(ReservationTime time, ReservedTimes reservedTimes) {
        return new AvailableReservationTimeResponse(
                time.getId(),
                time.getStartAt(),
                reservedTimes.isAvailable(time.getId())
        );
    }

    private boolean isMatchCondition(Boolean requestAvailable, boolean isAvailable) {
        return requestAvailable == null || requestAvailable.equals(isAvailable);
    }
}
