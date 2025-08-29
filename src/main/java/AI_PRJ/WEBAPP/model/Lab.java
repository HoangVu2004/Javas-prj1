package AI_PRJ.WEBAPP.model;

import jakarta.persistence.Column;
import com.fasterxml.jackson.annotation.JsonIgnore; // Added import
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "labs")
public class Lab {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String content;
    private int durationMinutes;
    private String difficultyLevel;
    private int maxSupportCount;
    private Long createdBy;

    @ManyToOne
    @JoinColumn(name = "kit_id")
    @JsonIgnore // Added annotation
    private Kit kit;

    public enum LabStatus {
        ACTIVE,
        INACTIVE
    }

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE'")
    private LabStatus status = LabStatus.ACTIVE;

    // ======= GETTERS ======= //
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getContent() {
        return content;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public int getMaxSupportCount() {
        return maxSupportCount;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public Kit getKit() {
        return kit;
    }

    public LabStatus getStatus() {
        return status;
    }

    // ======= SETTERS ======= //
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public void setMaxSupportCount(int maxSupportCount) {
        this.maxSupportCount = maxSupportCount;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public void setKit(Kit kit) {
        this.kit = kit;
    }

    public void setStatus(LabStatus status) {
        this.status = status;
    }
}
