package com.rtucare.backend.DTO.response;

import com.rtucare.backend.entity.PhysicalStatus;
import com.rtucare.backend.entity.UserProfile;
import com.rtucare.backend.enums.HeightUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponseDTO {

    private Long id;
    private Long userId;
    private Double height;
    private HeightUnit unit;
    private Double weightKg;
    private Integer age;
    private String location;

    public static UserProfileResponseDTO from(UserProfile profile) {
        PhysicalStatus ps = profile.getPhysicalStatus();
        HeightUnit displayUnit = ps.getHeightUnit();
        return new UserProfileResponseDTO(profile.getId(), profile.getUser().getId(),
                fromCm(ps.getHeightCm(), displayUnit), displayUnit, ps.getWeightKg(),
                ps.getAge(), ps.getLocation());
    }

    private static double fromCm(double heightCm, HeightUnit unit) {
        return unit == HeightUnit.FT ? heightCm / 30.48 : heightCm;
    }
}
