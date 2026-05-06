package roomescape.reservation.service;

import static roomescape.reservation.domain.ReservationStatus.RESERVED;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.NotFoundException;
import roomescape.date.domain.ReservationDate;
import roomescape.date.repository.ReservationDateRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.dto.ReservationSaveDto;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.response.ThemeDetailDto;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationDateRepository reservationDateRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              ReservationDateRepository reservationDateRepository, ThemeRepository themeRepository) {
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

        ReservationDate reservationDate = reservationDateRepository.findById(dto.dateId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 예약 날짜입니다."));

        Theme theme = themeRepository.findById(dto.themeId())
                .orElseThrow(() -> new IllegalArgumentException("해당 테마가 존재하지 않습니다."));

        //TODO: 예약 생성시 동일인이 동일 날짜/시간을 예약할 수 없도록 검증
        validateDuplicateReservation(reservationDate.date(), reservationTime.startAt());
        Long id = reservationRepository.save(
                Reservation.create(dto.name(), reservationDate.date(), reservationTime.startAt(), theme));

        return new ReservationResponse(
                id,
                dto.name(),
                reservationDate.date(),
                reservationTime.startAt(),
                ThemeDetailDto.from(theme),
                RESERVED // TODO: save 반환값 Reservation으로 수정
        );
    }

    private void validateDuplicateReservation(LocalDate date, LocalTime time) {
        // TODO: themeId 파라미터 추가
        if (reservationRepository.existsByDateAndTimeId(date, time)) {
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
