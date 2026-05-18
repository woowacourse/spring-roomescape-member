package roomescape.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationRequestDTO;
import roomescape.dto.ReservationResponseDTO;
import roomescape.dto.ReservationUpdateDtoDateAndTimeIdOnly;
import roomescape.exception.DuplicatedReservationException;
import roomescape.exception.PastDateReservationException;
import roomescape.exception.PastDateCancellationException;
import roomescape.exception.PastDateModificationException;
import roomescape.exception.ReservationNotFoundException;
import roomescape.exception.ReservationTimeNotFoundException;
import roomescape.exception.ThemeNotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Service
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository,
            ThemeRepository themeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<ReservationResponseDTO> readAllReservation() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponseDTO::from)
                .toList();
    }

    public List<ReservationResponseDTO> findAllByUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("사용자 이름은 비어있거나 공백일 수 없습니다.");
        }
        return reservationRepository.findAllByUsername(username)
                .stream()
                .map(ReservationResponseDTO::from)
                .toList();
    }

    public ReservationResponseDTO findById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("ID로 예약 조회 실패: " + id));
        return ReservationResponseDTO.from(reservation);
    }

    public ReservationResponseDTO reserve(ReservationRequestDTO reservationRequestDTO) {
        ReservationTime time = reservationTimeRepository.findById(reservationRequestDTO.timeId())
                .orElseThrow(() ->
                        new ReservationTimeNotFoundException("ID로 예약 시간 조회 실패: " + reservationRequestDTO.timeId())
                );
        Theme theme = themeRepository.findById(reservationRequestDTO.themeId())
                .orElseThrow(() ->
                        new ThemeNotFoundException("ID로 테마 조회 실패: " + reservationRequestDTO.themeId())
                );

        Reservation reservation = Reservation.withoutId(
                reservationRequestDTO.name(),
                reservationRequestDTO.date(),
                time,
                theme
        );

        validateReservationPolicy(reservation);

        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponseDTO.from(savedReservation);
    }

    public int update(Long reservationId, ReservationUpdateDtoDateAndTimeIdOnly updateDto) {
        Reservation reservation = getReservation(reservationId);
        validateModifiable(reservation);

        updateState(reservation, updateDto);
        validateReservationPolicy(reservation);

        return reservationRepository.update(reservation);
    }

    public int cancelReservation(ReservationRequestDTO requestDTO) {
        ReservationTime time = reservationTimeRepository.findById(requestDTO.timeId())
                .orElseThrow(() -> new ReservationTimeNotFoundException("취소 대상을 위한 시간 조회 실패: " + requestDTO.timeId()));

        if (isPast(LocalDateTime.of(requestDTO.date(), time.getStartAt()))) {
            throw new PastDateCancellationException("이미 지난 예약 취소 시도");
        }

        int deletedRows = reservationRepository.deleteReservationWith(
                requestDTO.name(),
                requestDTO.date(),
                requestDTO.timeId(),
                requestDTO.themeId()
        );
        if (deletedRows == 0) {
            throw new ReservationNotFoundException("취소할 예약 정보를 찾을 수 없음: " + requestDTO.name());
        }

        return deletedRows;
    }

    private boolean isPast(LocalDateTime targetDateTime) {
        return targetDateTime.isBefore(LocalDateTime.now());
    }

    private void validateReservationPolicy(Reservation reservation) {
        if (isPast(reservation.getDateTime())) {
            throw new PastDateReservationException("과거 시간으로 예약/변경 시도: " + reservation.getDateTime());
        }
        validateNotDuplicated(reservation);
    }

    private void validateModifiable(Reservation reservation) {
        if (isPast(reservation.getDateTime())) {
            throw new PastDateModificationException("이미 지난 예약 수정 시도: " + reservation.getDateTime());
        }
    }

    private void validateNotDuplicated(Reservation reservation) {
        Optional<Long> found = reservationRepository.findReservationIdWith(
                reservation.getDate(),
                reservation.getTimeId(),
                reservation.getThemeId()
        );

        found.ifPresent(foundId -> {
            if (reservation.getId() == null || !foundId.equals(reservation.getId())) {
                throw new DuplicatedReservationException(
                        reservation.getDate(),
                        reservation.getTime().getStartAt(),
                        reservation.getTheme().getName()
                );
            }
        });
    }

    private Reservation getReservation(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("수정 대상을 찾을 수 없음. ID: " + reservationId));
    }

    private void updateState(Reservation reservation, ReservationUpdateDtoDateAndTimeIdOnly updateDto) {
        ReservationTime newTime = findReservationTime(updateDto.timeId());
        reservation.changeDateAndTime(updateDto.date(), newTime);
    }

    private ReservationTime findReservationTime(Long timeId) {
        if (timeId == null) {
            return null;
        }
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new ReservationTimeNotFoundException("수정할 예약 시간을 찾을 수 없음. ID: " + timeId));
    }
}
