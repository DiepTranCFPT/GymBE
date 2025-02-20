package com.gymsystem.cyber.model.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TrainerRequest {
    private String specialization;
    private int experienceYear;
    private boolean availability;
    private String userId; // Chỉ cần ID của User

    // Getters và Setters
    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public int getExperienceYear() {
        return experienceYear;
    }

    public void setExperienceYear(int experienceYear) {
        this.experienceYear = experienceYear;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
