package mth.controller;

import jakarta.validation.Valid;
import mth.dto.*;
import mth.service.JwtServices;
import mth.service.UserServices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserServices userServices;
    private final JwtServices jwtServices;

    public UserController(UserServices userServices, JwtServices jwtServices) {
        this.userServices = userServices;
        this.jwtServices = jwtServices;
    }

    // ─────────────────────────────────────────────────────────
    //  Authentication (Signup & Signin)
    // ─────────────────────────────────────────────────────────

    @PostMapping("/auth/register")
    public ResponseEntity<ApiResponseDTO<TokenResponseDTO>> register(@Valid @RequestBody UserCreateDTO dto) {
        TokenResponseDTO result = userServices.signup(
                dto.getUsername(), dto.getEmail(), dto.getPassword(), dto.getRole()
        );
        return ResponseEntity.ok(ApiResponseDTO.success(result, "User registered successfully"));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponseDTO<TokenResponseDTO>> login(@Valid @RequestBody UserLoginDTO dto) {
        TokenResponseDTO result = userServices.signin(dto.getEmail(), dto.getPassword());
        return ResponseEntity.ok(ApiResponseDTO.success(result, "Login successful"));
    }

    // ─────────────────────────────────────────────────────────
    //  EXAM TASK: Fetch Single User by ID
    //  ● Manual validation of header 'Token'
    //  ● Fetch using findById() → returns Optional<Users>
    //  ● Handle not found → 404 ResourceNotFoundException
    // ─────────────────────────────────────────────────────────

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> getUserById(
            @PathVariable Long id,
            @RequestHeader(value = "Token", required = false) String token) {

        if (token == null || jwtServices.validateJWT(token) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponseDTO.error("Invalid or missing Token header", HttpStatus.UNAUTHORIZED.value()));
        }

        UserResponseDTO user = userServices.getUserById(id);
        return ResponseEntity.ok(ApiResponseDTO.success(user, "User fetched successfully"));
    }

    // ─────────────────────────────────────────────────────────
    //  Get All Users (Paginated)
    // ─────────────────────────────────────────────────────────

    @GetMapping("/users")
    public ResponseEntity<ApiResponseDTO<Page<UserResponseDTO>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader(value = "Token", required = false) String token) {

        if (token == null || jwtServices.validateJWT(token) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponseDTO.error("Invalid or missing Token header", HttpStatus.UNAUTHORIZED.value()));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<UserResponseDTO> users = userServices.getAllUsers(pageable);
        return ResponseEntity.ok(ApiResponseDTO.success(users, "Users retrieved successfully"));
    }
}
