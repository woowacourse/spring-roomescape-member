package roomescape.domain.time.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;

public record TimeResponseDTO(Long id, @JsonFormat(pattern = "HH:mm") LocalTime startAt) {

}
