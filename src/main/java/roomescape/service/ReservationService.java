package roomescape.service;

import jakarta.annotation.Nonnull;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.DuplicateReservationException;
import roomescape.common.exception.ForbiddenException;
import roomescape.common.exception.ResourceNotFoundException;
import roomescape.domain.exception.ReservationValidator;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationUpdateRequest;
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

    public List<Reservation> getReservationsByName(String name) {
        return reservationRepository.findByName(name);
    }

    public Reservation getReservation(Long id) {
        return reservationRepository.getById(id);
    }

    @Transactional
    public Reservation addReservation(ReservationRequest request) {
        ReservationTime reservationTime = getReservationTime(request.timeId());
        Theme theme = getTheme(request.themeId());

        reservationValidator.validateFutureReservationDateTime(
                request.date(),
                reservationTime.getStartAt(),
                "지난 날짜와 시간으로는 예약할 수 없습니다.");
        validateDuplicateReservation(request.date(), reservationTime, theme);

        Long id = reservationRepository.save(createReservation(request, reservationTime, theme));
        return getReservation(id);
    }

    private ReservationTime getReservationTime(Long timeId) {
        try {
            return timeRepository.getById(timeId);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("예약할 수 없는 시간입니다.");
        }
    }

    private Theme getTheme(Long themeId) {
        try {
            return themeRepository.getById(themeId);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("예약할 수 없는 테마입니다.");
        }
    }

    private void validateDuplicateReservation(LocalDate date, ReservationTime reservationTime,
                                              Theme theme) {
        if (reservationRepository.existsBy(date, reservationTime.getId(),
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

    @Transactional
    public void cancelMyReservation(Long id, String name) {
        Reservation reservation = getReservation(id);
        validateReservationOwner(name, reservation);
        reservationValidator.validateNotPastReservation(reservation.getDate(),
                reservation.getTime().getStartAt(), "이미 지난 예약은 취소할 수 없습니다.");
        reservationRepository.deleteById(id);
    }

    @Transactional
    public Reservation updateReservation(Long id, String name, ReservationUpdateRequest request) {
        Reservation reservation = getReservation(id);
        validateReservationOwner(name, reservation);

        ReservationTime reservationTime = getReservationTime(request.timeId());

        validateUpdate(request, reservation, reservationTime);

        reservationRepository.updateDateTime(id, request.date(), request.timeId());

        return getReservation(id);
    }

    private void validateReservationOwner(String name, Reservation reservation) {
        if (!reservation.getName().equals(name)) {
            throw new ForbiddenException("선택한 예약과 사용자 이름이 일치하지 않습니다.");
        }
    }

    private void validateUpdate(ReservationUpdateRequest request, Reservation reservation,
                                ReservationTime reservationTime) {
        reservationValidator.validateNotPastReservation(reservation.getDate(),
                reservation.getTime().getStartAt(), "이미 지난 예약은 변경할 수 없습니다.");
        reservationValidator.validateFutureReservationDateTime(
                request.date(),
                reservationTime.getStartAt(),
                "지난 날짜와 시간으로는 예약을 수정할 수 없습니다."
        );
        validateDuplicateReservation(request.date(), reservationTime, reservation.getTheme());
    }
}
