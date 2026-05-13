package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.global.exception.DuplicateEntityException;
import roomescape.global.exception.EntityNotFoundException;
import roomescape.global.exception.ForbiddenException;
import roomescape.global.exception.InactiveException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.web.dto.reservation.ReservationCancelRequest;
import roomescape.web.dto.reservation.ReservationRequest;
import roomescape.web.dto.reservation.ReservationResponse;

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

        validateDuplicateReservation(request.date(), time, theme);
        validateInactiveTheme(theme);

        Reservation reservation = Reservation.of(request.name(), request.date(), theme, time);
        return ReservationResponse.from(reservationRepository.save(reservation));
    }

    @Transactional
    public void cancel(Long id) {
        reservationRepository.deleteById(id);
    }

    @Transactional
    public void cancel(Long id, ReservationCancelRequest request) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 예약입니다."));

        if (!request.name().equals(reservation.getName())) {
            throw new ForbiddenException("예약자 명이 일치하지 않습니다.");
        }

        reservation.cancel();
        reservationRepository.update(reservation);
    }

    public List<ReservationResponse> getAllReservationsByPaging(int page, int size) {
        return reservationRepository.findAllByPaging(page, size)
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public List<ReservationResponse> getReservationsByUser(String name) {
        return reservationRepository.findAllByUserName(name)
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    private void validateDuplicateReservation(LocalDate date, ReservationTime time, Theme theme) {
        if (reservationRepository.existByDateAndTimeIdAndThemeId(date, time.getId(), theme.getId())) {
            throw new DuplicateEntityException("이미 예약 된 날짜입니다. (%s-%s)", date, time.getStartAt());
        }
    }

    private Theme findThemeOrThrow(Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 테마 정보입니다."));
    }

    private ReservationTime findTimeOrThrow(Long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 시간 정보입니다."));
    }

    private void validateInactiveTheme(Theme theme) {
        if (!theme.isActive()) {
            throw new InactiveException("비활성화 된 테마는 예약할 수 없습니다.");
        }
    }
}
