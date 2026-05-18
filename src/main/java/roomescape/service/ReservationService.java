package roomescape.service;

import jakarta.annotation.Nonnull;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.DuplicateReservationException;
import roomescape.common.exception.ResourceNotFoundException;
import roomescape.domain.exception.ReservationValidator;
import roomescape.dto.ReservationRequest;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;
import roomescape.entity.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final ReservationValidator reservationValidator;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository timeRepository, ThemeRepository themeRepository,
                              ReservationValidator reservationValidator) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.reservationValidator = reservationValidator;
    }

    public List<Reservation> getReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservation(Long id) {
        return reservationRepository.getById(id);
    }

    @Transactional
    public Reservation addReservation(ReservationRequest request) {
        ReservationTime reservationTime = getReservationTime(request);
        Theme theme = getTheme(request);

        reservationValidator.validateFutureReservationDateTime(request.date(), reservationTime.getStartAt());
        validateDuplicateReservation(request, reservationTime, theme);

        Long id = reservationRepository.save(createReservation(request, reservationTime, theme));
        return getReservation(id);
    }

    private ReservationTime getReservationTime(ReservationRequest request) {
        try {
            return timeRepository.getById(request.timeId());
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("예약할 수 없는 시간입니다.");
        }
    }

    private Theme getTheme(ReservationRequest request) {
        try {
            return themeRepository.getById(request.themeId());
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("예약할 수 없는 테마입니다.");
        }
    }

    private void validateDuplicateReservation(ReservationRequest request, ReservationTime reservationTime,
                                              Theme theme) {
        if (reservationRepository.existsBy(request.date(), reservationTime.getId(),
                theme.getId())) {
            throw new DuplicateReservationException("이미 예약된 시간입니다.");
        }
    }

    @Nonnull
    private Reservation createReservation(ReservationRequest request, ReservationTime reservationTime,
                                          Theme theme) {
        return new Reservation(
                request.name(),
                request.date(),
                reservationTime,
                theme
        );
    }

    @Transactional
    public void deleteReservation(Long id) {
        getReservation(id);
        reservationRepository.deleteById(id);
    }
}
