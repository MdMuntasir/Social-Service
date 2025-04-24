package com.socialservice.model;

public class Profile {
    private Long userId;
    private String name;
    private String workField;
    private String experience;
    private boolean isAvailable;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getWorkField() { return workField; }
    public void setWorkField(String workField) { this.workField = workField; }
    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean isAvailable) { this.isAvailable = isAvailable; }
}