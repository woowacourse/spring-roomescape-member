package roomescape.reservation.application.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.application.dto.ReservationCreateCommand;
import roomescape.reservation.application.dto.ReservationQueryResult;
import roomescape.reservation.application.exception.ReservationException;
import roomescape.reservation.application.query.ReservationDetailDao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.application.query.ReservationDetail;
import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.reservationtime.application.dto.ReservationTimeQueryResult;
import roomescape.reservationtime.application.service.ReservationTimeService;
import roomescape.theme.application.dto.ThemeQueryResult;
import roomescape.theme.application.service.ThemeService;

@RequiredArgsConstructor
@Transactional
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationDetailDao reservationDetailDao;
    private final ThemeService themeService;
    private final ReservationTimeService timeService;

    @Transactional(readOnly = true)
    public List<ReservationQueryResult> findAll() {
        List<ReservationDetail> result = reservationDetailDao.findAll();
        return result.stream()
                .map(ReservationQueryResult::from)
                .toList();
    }

    public ReservationQueryResult save(ReservationCreateCommand request, LocalDateTime currentDateTime) {
        ThemeQueryResult themeQueryResult = themeService.findById(request.themeId());
        ReservationTimeQueryResult timeQueryResult = timeService.findById(request.timeId());

        Reservation reservation = request.toEntity(themeQueryResult.id(), timeQueryResult.id());
        reservation.validateNotPast(timeQueryResult.startAt(), currentDateTime);

        validateDuplicateReservation(reservation);

        return ReservationQueryResult.from(reservationRepository.save(reservation), themeQueryResult, timeQueryResult);
    }

    public int delete(Long id) {
        return reservationRepository.delete(id);
    }

    private void validateDuplicateReservation(Reservation reservation) {
        Boolean existsByDateAndTime = reservationRepository.existsByDateAndThemeAndTime(
                reservation.getDate(),
                reservation.getThemeId(),
                reservation.getTimeId()
        );

        if (existsByDateAndTime) {
            throw new ReservationException("이미 해당 날짜와 시간에 예약이 존재합니다.");
        }
    }
}
