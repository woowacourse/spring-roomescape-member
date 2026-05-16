package roomescape.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.global.exception.DuplicateEntityException;
import roomescape.global.exception.EntityNotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.web.dto.reservation.ReservationCancelRequest;
import roomescape.web.dto.reservation.ReservationModifyRequest;
import roomescape.web.dto.reservation.ReservationRequest;
import roomescape.web.dto.reservation.ReservationResponse;
import roomescape.web.dto.theme.ReservationTimeStatusResponse;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    @Transactional
    public ReservationResponse reserve(ReservationRequest request) {
        Theme theme = findThemeOrThrow(request.themeId());
        ReservationTime time = findTimeOrThrow(request.timeId());

        theme.validateInactive();
        time.validateInactive();

        validateDuplicateReservation(request.date(), time, theme);

        Reservation reservation = Reservation.create(request.name(), request.date(), theme, time);
        return ReservationResponse.from(reservationRepository.save(reservation));
    }

    @Transactional
    public void cancel(Long id, ReservationCancelRequest request) {
        Reservation reservation = findReservationOrThrow(id);

        reservation.validateOwner(request.name());
        Reservation canceledReservation = reservation.cancel();
        reservationRepository.update(canceledReservation);
    }

    public List<ReservationResponse> getAllReservationsByPaging(int page, int size) {
        return reservationRepository.findAllByPaging(page, size).stream().map(ReservationResponse::from).toList();
    }

    public List<ReservationResponse> getReservationsByUser(String name) {
        return reservationRepository.findAllByUserName(name).stream().map(ReservationResponse::from).toList();
    }

    @Transactional
    public void modify(Long id, ReservationModifyRequest request) {
        Reservation reservation = findReservationOrThrow(id);
        ReservationTime time = findTimeOrThrow(request.timeId());
        LocalDate date = request.date();
        Theme theme = findThemeOrThrow(reservation.getTheme().getId());

        theme.validateInactive();
        time.validateInactive();

        reservation.validateOwner(request.name());
        validateDuplicateReservation(date, time, theme);

        reservationRepository.update(reservation.update(date, time));
    }

    public List<ReservationTimeStatusResponse> getReservationStatusByTheme(Long themeId, LocalDate date) {
        if (themeRepository.findById(themeId).isEmpty()) {
            throw new EntityNotFoundException("존재하지 않는 테마 정보입니다.");
        }

        Set<Long> reservedTimeIds = reservationRepository.findUnavailableTimeIdsByThemeIdAndDate(themeId, date);

        List<ReservationTimeStatusResponse> responses = new ArrayList<>();
        reservationTimeRepository.findTimeSlotsForReservationStatus().forEach(time -> {
            boolean reservable = isReservable(time, date, reservedTimeIds);
            responses.add(ReservationTimeStatusResponse.of(time, reservable));
        });

        return responses;
    }

    private Theme findThemeOrThrow(Long themeId) {
        return themeRepository.findById(themeId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 테마 정보입니다."));
    }

    private void validateDuplicateReservation(LocalDate date, ReservationTime time, Theme theme) {
        if (reservationRepository.existsReservedReservation(date, time.getId(), theme.getId())) {
            throw new DuplicateEntityException("이미 예약 된 날짜입니다. (%s-%s)", date, time.getStartAt());
        }
    }

    private boolean isReservable(ReservationTime time, LocalDate date, Set<Long> reservedTimeIds) {
        return time.isAvailableAt(date) && !reservedTimeIds.contains(time.getId()) && time.isActive();
    }

    private Reservation findReservationOrThrow(Long id) {
        return reservationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 예약입니다."));
    }

    private ReservationTime findTimeOrThrow(Long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 시간 정보입니다."));
    }
}
