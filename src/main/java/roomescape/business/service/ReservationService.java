package roomescape.business.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.business.domain.reservation.Reservation;
import roomescape.business.domain.reservation.ReservationTheme;
import roomescape.business.domain.reservation.ReservationTime;
import roomescape.config.LoginMember;
import roomescape.exception.ReservationException;
import roomescape.exception.ReservationThemeException;
import roomescape.exception.ReservationTimeException;
import roomescape.persistence.ReservationRepository;
import roomescape.persistence.ReservationThemeRepository;
import roomescape.persistence.ReservationTimeRepository;
import roomescape.presentation.dto.ReservationRequestDto;
import roomescape.presentation.dto.ReservationResponseDto;
import roomescape.presentation.mapper.ReservationMapper;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationThemeRepository reservationThemeRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              ReservationThemeRepository reservationThemeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationThemeRepository = reservationThemeRepository;
    }

    public List<ReservationResponseDto> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(ReservationMapper::toResponse)
                .toList();
    }

    public ReservationResponseDto getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationException("존재하지 않는 예약입니다."));
        return ReservationMapper.toResponse(reservation);
    }

    @Transactional
    public Long createReservation(ReservationRequestDto reservationDto, LoginMember loginMember) {
        ReservationTime reservationTime = reservationTimeRepository.findById(reservationDto.timeId())
                .orElseThrow(() -> new ReservationTimeException("존재하지 않는 예약 시간입니다."));
        ReservationTheme theme = reservationThemeRepository.findById(reservationDto.themeId())
                .orElseThrow(() -> new ReservationThemeException("존재하지 않는 예약 테마입니다."));
        if (reservationTime.isInThePast(reservationDto.date())) {
            throw new ReservationException("과거 일시로 예약을 생성할 수 없습니다.");
        }
        Reservation reservation = new Reservation(loginMember.name(), reservationDto.date(), reservationTime, theme);
        validateDuplicatedReservation(reservation);
        return reservationRepository.add(reservation);
    }

    private void validateDuplicatedReservation(Reservation reservation) {
        if (reservationRepository.existsByReservation(reservation)) {
            throw new ReservationException("해당 날짜와 시간에 이미 예약이 존재합니다.");
        }
    }

    public void deleteReservationById(Long id) {
        reservationRepository.deleteById(id);
    }
}
