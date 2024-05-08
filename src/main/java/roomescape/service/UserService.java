package roomescape.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import roomescape.domain.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


}
