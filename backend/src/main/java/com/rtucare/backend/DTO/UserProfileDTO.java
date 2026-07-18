package com.rtucare.backend.DTO;

import com.rtucare.backend.enums.HeightUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {

    private Long id;
    private Long userId;
    private Double height;
    private HeightUnit unit;
    private Double weightKg;
    private Integer age;
    private String location;
}
