package roomescape.date.domain;

import java.time.LocalDate;

/*
*
* 날짜 등록 (관리자)
* 날짜 등록 조회 (관리자)
* 날짜 삭제 (관리자)
*
* 날짜 등록 조회 (사용자)
* */
public class ReservationDate {

    private Long id;
    private LocalDate date;

    private ReservationDate(Long id, LocalDate date) {
        this.id = id;
        this.date = date;
    }

    public static ReservationDate create(LocalDate date) {
        validateDate(date);
        return new ReservationDate(null, date);
    }

    public static ReservationDate load(Long id, LocalDate date) {
        validateId(id);
        validateDate(date);
        return new ReservationDate(id, date);
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public static void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("예약날짜 ID는 필수입니다.");
        }
    }

    public static void validateDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("예약 날짜는 필수입니다.");
        }

        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("과거 날짜는 등록할 수 없습니다.");
        }
    }

}
