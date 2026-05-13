package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.business.BusinessException;
import roomescape.exception.business.PastTimeCancelException;
import roomescape.exception.business.PastTimeReservationException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.dto.ReservationUpdateRequest;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.service.ReservationTimeService;
import roomescape.theme.domain.Theme;
import roomescape.theme.service.ThemeService;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeService reservationTimeService,
            ThemeService themeService
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
    }

    @Transactional
    public ReservationResponse createReservation(ReservationRequest request) {
        ReservationTime time = reservationTimeService.getById(request.timeId());
        Theme theme = themeService.getById(request.themeId());

        if (reservationRepository.existsByDateAndTimeIdAndThemeId(request.date(), request.timeId(), request.themeId())) {
            throw new BusinessException(HttpStatus.CONFLICT, "이미 예약된 시간입니다.");
        }

        Reservation saved = reservationRepository.save(Reservation.of(request.name(), request.date(), time, theme));
        return ReservationResponse.from(saved);
    }

    public List<ReservationResponse> getReservationsByName(String name) {
        return reservationRepository.findByName(name).stream()
                .map(ReservationResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteReservation(Long id) {
        Reservation reservation = getById(id);
        if (reservation.isPast()) {
            throw new PastTimeCancelException("이미 지난 예약은 취소할 수 없습니다.");
        }
        reservationRepository.deleteById(id);
    }

    @Transactional
    public ReservationResponse updateReservation(Long id, ReservationUpdateRequest request) {
        Reservation reservation = getById(id);
        if (reservation.isPast()) {
            throw new PastTimeCancelException("이미 지난 예약은 변경할 수 없습니다.");
        }

        ReservationTime newTime = reservationTimeService.getById(request.timeId());
        LocalDate newDate = request.date();

        if (Reservation.of(reservation.getId(), reservation.getName(), newDate, newTime, reservation.getTheme()).isPast()) {
            throw new PastTimeReservationException("이미 지난 시간으로 변경할 수 없습니다.");
        }
        if (reservationRepository.existsByDateAndTimeIdAndThemeId(newDate, request.timeId(), reservation.getTheme().getId())) {
            throw new BusinessException(HttpStatus.CONFLICT, "이미 예약된 시간입니다.");
        }

        reservationRepository.update(id, newDate, request.timeId());
        return ReservationResponse.from(
                Reservation.of(reservation.getId(), reservation.getName(), newDate, newTime, reservation.getTheme())
        );
    }

    @NonNull
    private Reservation getById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "존재하지 않는 예약입니다."));
    }
}