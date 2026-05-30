package mth.service;

import mth.dto.TokenResponseDTO;
import mth.dto.UserResponseDTO;
import mth.entity.Users;
import mth.exception.ResourceNotFoundException;
import mth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServices {

    private final UserRepository userRepository;
    private final JwtServices jwtServices;

    @Autowired
    public UserServices(UserRepository userRepository, JwtServices jwtServices) {
        this.userRepository = userRepository;
        this.jwtServices = jwtServices;
    }

    // ─────────────────────────────────────────────────────────
    //  EXAM TASK: Fetch Single User by ID
    //  ● Uses findById() which returns Optional<Users>
    //  ● Handles not found with 404 ResourceNotFoundException
    // ─────────────────────────────────────────────────────────

    /**
     * Fetch a single user by their ID.
     *
     * findById() returns an Optional<Users>.
     * - Optional is a container that may or may not contain a value.
     * - If user exists → Optional contains the Users object.
     * - If user does NOT exist → Optional is empty.
     *
     * We use .orElseThrow() to either unwrap the user
     * or throw a ResourceNotFoundException (HTTP 404).
     *
     * VIVA ANSWERS:
     *   Q: What is Optional?
     *   A: Optional<T> is a container introduced in Java 8 that may hold
     *      a non-null value or be empty. It avoids NullPointerException
     *      by forcing explicit handling of the "value absent" case.
     *
     *   Q: Which status for not found?
     *   A: HTTP 404 (Not Found) — indicates the server cannot find
     *      the requested resource (user with the given ID).
     */
    public UserResponseDTO getUserById(Long id) {
        // findById() returns Optional<Users>
        Optional<Users> optionalUser = userRepository.findById(id);

        // Unwrap or throw 404
        Users user = optionalUser.orElseThrow(
                () -> new ResourceNotFoundException("User not found with id " + id)
        );

        return toUserResponse(user);
    }

    // ─────────────────────────────────────────────────────────
    //  Registration (Signup)
    // ─────────────────────────────────────────────────────────

    /**
     * Register a new user and return a JWT token.
     */
    public TokenResponseDTO signup(String username, String email, String password, String role) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already registered: " + email);
        }
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already taken: " + username);
        }

        Users user = new Users();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role != null ? role : "user");
        user.setIsActive(1);

        Users savedUser = userRepository.save(user);

        String token = jwtServices.generateJWT(savedUser.getEmail(), savedUser.getId(), savedUser.getRole());

        TokenResponseDTO response = new TokenResponseDTO();
        response.setAccessToken(token);
        response.setTokenType("bearer");
        response.setUser(toUserResponse(savedUser));
        return response;
    }

    // ─────────────────────────────────────────────────────────
    //  Authentication (Signin)
    // ─────────────────────────────────────────────────────────

    /**
     * Authenticate user with email/password and return JWT token.
     */
    public TokenResponseDTO signin(String email, String password) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid email or password"));

        if (!password.equals(user.getPassword())) {
            throw new ResourceNotFoundException("Invalid email or password");
        }

        if (user.getIsActive() == 0) {
            throw new ResourceNotFoundException("Account is deactivated");
        }

        String token = jwtServices.generateJWT(user.getEmail(), user.getId(), user.getRole());

        TokenResponseDTO response = new TokenResponseDTO();
        response.setAccessToken(token);
        response.setTokenType("bearer");
        response.setUser(toUserResponse(user));
        return response;
    }

    // ─────────────────────────────────────────────────────────
    //  Get All Users (Paginated)
    // ─────────────────────────────────────────────────────────

    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::toUserResponse);
    }

    // ─────────────────────────────────────────────────────────
    //  Get Current User by Email
    // ─────────────────────────────────────────────────────────

    public UserResponseDTO getCurrentUser(String email) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return toUserResponse(user);
    }

    // ─────────────────────────────────────────────────────────
    //  Helper: Entity → DTO
    // ─────────────────────────────────────────────────────────

    private UserResponseDTO toUserResponse(Users user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setIsActive(user.getIsActive());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}
