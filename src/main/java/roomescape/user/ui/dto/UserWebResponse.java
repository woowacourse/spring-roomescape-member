package roomescape.user.ui.dto;

public record UserWebResponse(Long userId,
                              String name,
                              String email,
                              String password) {

}
