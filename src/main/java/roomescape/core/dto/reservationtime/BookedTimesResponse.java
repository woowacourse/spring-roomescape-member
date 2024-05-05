package roomescape.core.dto.reservationtime;

import java.util.List;

public class BookedTimesResponse {
    private List<BookedTimeResponse> times;

    public BookedTimesResponse() {
    }

    public BookedTimesResponse(final List<BookedTimeResponse> times) {
        this.times = times;
    }

    public List<BookedTimeResponse> getTimes() {
        return times;
    }
}
