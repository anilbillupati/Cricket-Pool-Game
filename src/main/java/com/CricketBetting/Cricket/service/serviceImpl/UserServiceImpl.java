package com.CricketBetting.Cricket.service.serviceImpl;

import com.CricketBetting.Cricket.domain.entity.User;
import com.CricketBetting.Cricket.domain.enums.Role;
import com.CricketBetting.Cricket.domain.request.UserRequestDto;
import com.CricketBetting.Cricket.domain.response.UserResponseDto;
import com.CricketBetting.Cricket.mapper.UserMapper;
import com.CricketBetting.Cricket.repository.UserRepository;
import com.CricketBetting.Cricket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto createUser(UserRequestDto dto) {
        userRepository.findByEmail(dto.getEmail()).ifPresent(user -> {
            throw new IllegalArgumentException("Email already exists");
        });

        if (dto.getRole() == null) {
            dto.setRole(Role.PLAYER);
        }

        User user = userMapper.toEntity(dto);

        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public List<UserResponseDto> getUserListOnly(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> players = userRepository.findByRole(Role.PLAYER, pageable);
        return players.map(userMapper::toDto).getContent();
    }

    @Override
    public UserResponseDto getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        return userMapper.toDto(user);
    }

}