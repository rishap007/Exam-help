package com.eduplatform.dto.response;

import com.eduplatform.dto.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class BaseResponse extends BaseDto {
    // Common response fields can be added here
}