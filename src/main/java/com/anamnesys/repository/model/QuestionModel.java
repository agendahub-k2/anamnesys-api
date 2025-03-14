package com.anamnesys.repository.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "QUESTION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "question", nullable = false, length = 500)
    private String question;

    @Column(name = "section", nullable = false)
    private Long section;

    @Column(name = "description_section")
    private String descriptionSection;

    @Column(name = "is_required", nullable = false)
    private Boolean isRequired;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false)
    private QuestionType questionType;

    @ManyToOne
    @JoinColumn(name = "record_id")
//    @JsonManagedReference
    private RecordModel record;

    @ManyToOne
    @JoinColumn(name = "template_id")
    private TemplateModel template;

    @Column(name = "options")
    private String options;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "update_at", nullable = false)
    private LocalDateTime updateAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updateAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateAt = LocalDateTime.now();
    }

    public enum QuestionType {
        TEXT, NUMBER, BOOLEAN, SELECT, DATE
    }
}
