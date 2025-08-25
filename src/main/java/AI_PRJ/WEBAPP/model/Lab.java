package AI_PRJ.WEBAPP.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
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
    private Kit kit;

    private boolean isActive = false;

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

    public boolean isActive() {
        return isActive;
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

    public void setActive(boolean active) {
        isActive = active;
    }
}