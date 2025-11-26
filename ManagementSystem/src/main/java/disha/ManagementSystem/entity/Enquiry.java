package disha.ManagementSystem.entity;

import jakarta.persistence.*;

@Entity
public class Enquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String student_name;
    private String email;
    private String class_mode;
    private String subject;
    private String status;
    private String urgency;


    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    @ManyToOne
    @JoinColumn(name = "counsellor_id")
    private User counsellor;

    public User getCounsellor() {
        return counsellor;
    }

    public void setCounsellor(User counsellor) {
        this.counsellor = counsellor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClass_mode() {
        return class_mode;
    }

    public void setClass_mode(String class_mode) {
        this.class_mode = class_mode;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
