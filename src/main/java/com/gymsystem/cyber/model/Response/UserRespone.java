package com.gymsystem.cyber.model.Response;

import com.gymsystem.cyber.enums.UserRole;
import lombok.Builder;
import lombok.Data;
import org.bytedeco.opencv.presets.opencv_core;
@Data
@Builder
public class UserRespone {
    private String id;
    private String name;
    private String email;
    private String phone;
    private UserRole role;
    private String plan;
    private boolean enable;
}
