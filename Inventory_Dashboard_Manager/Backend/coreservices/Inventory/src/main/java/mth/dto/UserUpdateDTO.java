package mth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserUpdateDTO {

    @NotBlank(message = "Role is required")
    private String role;

    @NotNull(message = "Active status is required")
    @JsonProperty("isActive")
    private Integer isActive;

    private String username;

    private String email;

    public UserUpdateDTO() {
    }

    public UserUpdateDTO(String role, Integer isActive, String username, String email) {
        this.role = role;
        this.isActive = isActive;
        this.username = username;
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserUpdateDTO [role=" + role + ", isActive=" + isActive + ", username=" + username + ", email=" + email + "]";
    }
}

