package roomescape.reservation.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.NotFoundException;
import roomescape.date.repository.ReservationDateRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationSaveDto;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.response.ThemeDetailDto;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.dto.ReservationTimeResponse;
import roomescape.time.repository.ReservationTimeRepository;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationDateRepository reservationDateRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository, ReservationDateRepository reservationDateRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationDateRepository = reservationDateRepository;
        this.themeRepository = themeRepository;
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional
    public ReservationResponse create(ReservationSaveDto dto) {
        ReservationTime reservationTime = reservationTimeRepository.findById(dto.timeId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 예약 시간입니다."));

        Theme theme = themeRepository.findById(dto.themeId())
                .orElseThrow(() -> new IllegalArgumentException("해당 테마가 존재하지 않습니다."));

        validateDuplicateReservation(dto);
        Long id = reservationRepository.save(Reservation.create(dto.name(), dto.date(), reservationTime, theme));

        return new ReservationResponse(
                id,
                dto.name(),
                dto.date(),
                ReservationTimeResponse.from(reservationTime),
                ThemeDetailDto.from(theme)
        );
    }

    private void validateDuplicateReservation(ReservationSaveDto reservationSaveDto) {
        if (reservationRepository.existsByDateAndTimeId(reservationSaveDto.date(),
                reservationSaveDto.timeId())) {
            throw new ConflictException("이미 존재하는 예약 날짜/시간 입니다.");
        }
    }

    @Transactional
    public ReservationResponse delete(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 예약입니다."));
        reservationRepository.delete(id);
        return ReservationResponse.from(reservation);
    }
}
