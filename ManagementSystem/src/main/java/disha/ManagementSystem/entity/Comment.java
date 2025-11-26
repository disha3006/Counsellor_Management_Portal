package disha.ManagementSystem.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    @Column(name = "enquiry_id")
    private Long enquiryId;

    @Column(name = "student_name")
    private String studentName;

    @ManyToOne
    @JoinColumn(name = "counsellor_id")   // column inside comments table
    private User counsellor;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getEnquiryId() {
        return enquiryId;
    }

    public void setEnquiryId(Long enquiryId) {
        this.enquiryId = enquiryId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public User getCounsellor() {
        return counsellor;
    }

    public void setCounsellor(User counsellor) {
        this.counsellor = counsellor;
    }
}
