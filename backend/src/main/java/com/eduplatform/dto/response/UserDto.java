package com.eduplatform.dto.response;

import com.eduplatform.dto.BaseDto;
import com.eduplatform.model.enums.UserRole;
import com.eduplatform.model.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

// import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserDto extends BaseDto {
    private String fullName; // <-- ADD THIS LINE
    private String email;
    private String firstName;
    private String lastName;
    private UserRole role;
    private UserStatus status;
    private String phoneNumber;
    private String profilePictureUrl;
}