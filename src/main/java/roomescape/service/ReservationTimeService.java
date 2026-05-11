package roomescape.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservedTimes;
import roomescape.global.exception.DuplicateReservationTimeException;
import roomescape.global.exception.ThemeNotFoundException;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.reservationtime.AvailableReservationTimesCondition;
import roomescape.service.dto.reservationtime.AvailableReservationTimeResult;
import roomescape.service.dto.reservationtime.AvailableReservationTimesResult;
import roomescape.service.dto.reservationtime.CreateReservationTimeCommand;
import roomescape.service.dto.reservationtime.ReservationTimeResult;
import roomescape.service.dto.theme.ThemeResult;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    @Transactional
    public ReservationTimeResult createReservationTime(CreateReservationTimeCommand command) {
        if (reservationTimeRepository.existsByStartAt(command.startAt())) {
            throw new DuplicateReservationTimeException();
        }
        ReservationTime savedReservationTime = reservationTimeRepository.save(
                ReservationTime.createNew(command.startAt()));

        return ReservationTimeResult.from(savedReservationTime);
    }

    public List<ReservationTimeResult> getReservationTimes() {
        return reservationTimeRepository.findAll().stream()
                .map(ReservationTimeResult::from)
                .toList();
    }

    @Transactional
    public void deleteReservationTime(Long id) {
        reservationTimeRepository.deleteById(id);
    }

    public AvailableReservationTimesResult getAvailableReservationTimes(AvailableReservationTimesCondition condition) {
        ThemeResult theme = ThemeResult.from(themeRepository.findById(condition.themeId())
                .orElseThrow(ThemeNotFoundException::new));

        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        ReservedTimes reservedTimes = new ReservedTimes(
                reservationTimeRepository.findReservedTimeIds(condition.themeId(), condition.date())
        );

        List<AvailableReservationTimeResult> availableTimes = reservationTimes.stream()
                .map(time -> createResponse(time, reservedTimes))
                .filter(response -> isMatchCondition(condition.available(), response.available()))
                .toList();

        return new AvailableReservationTimesResult(theme, availableTimes);
    }

    private AvailableReservationTimeResult createResponse(ReservationTime time, ReservedTimes reservedTimes) {
        return new AvailableReservationTimeResult(
                time.getId(),
                time.getStartAt(),
                reservedTimes.isAvailable(time.getId())
        );
    }

    private boolean isMatchCondition(Boolean requestAvailable, boolean isAvailable) {
        return requestAvailable == null || requestAvailable.equals(isAvailable);
    }
}
