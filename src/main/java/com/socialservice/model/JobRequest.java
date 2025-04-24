package com.socialservice.model;

public class JobRequest {
    private Long id;
    private Long jobId;
    private Long freelancerId;
    private String status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }
    public Long getFreelancerId() { return freelancerId; }
    public void setFreelancerId(Long freelancerId) { this.freelancerId = freelancerId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}