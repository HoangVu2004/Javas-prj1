package AI_PRJ.WEBAPP.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@IdClass(Help.HelpId.class)
@Table(name = "help")
public class Help {

    @Id
    @Column(name = "user_id")
    private String user;

    @Id
    @Column(name = "lab_id")
    private String lab;

    @Column(name = "support_count")
    private int supportCount;

    @Column(name = "content", length = 1000)
    private String content;

    @Column(name = "reply_content", length = 1000)
    private String replyContent;

    @Column(name = "reply_created_at")
    private LocalDateTime replyCreatedAt;

    @Column(name = "staff_id")
    private String staffId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Help() {
    }

    public Help(String user, String lab, int supportCount) {
        this.user = user;
        this.lab = lab;
        this.supportCount = supportCount;
    }

    public Help(String user, String lab, int supportCount, String content) {
        this.user = user;
        this.lab = lab;
        this.supportCount = supportCount;
        this.content = content;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getLab() {
        return lab;
    }

    public void setLab(String lab) {
        this.lab = lab;
    }

    public int getSupportCount() {
        return supportCount;
    }

    public void setSupportCount(int supportCount) {
        this.supportCount = supportCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public LocalDateTime getReplyCreatedAt() {
        return replyCreatedAt;
    }

    public void setReplyCreatedAt(LocalDateTime replyCreatedAt) {
        this.replyCreatedAt = replyCreatedAt;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Help)) return false;
        Help help = (Help) o;
        return Objects.equals(user, help.user) &&
                Objects.equals(lab, help.lab);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, lab);
    }

    public static class HelpId implements Serializable {
        private String user;
        private String lab;

        public HelpId() {
        }

        public HelpId(String user, String lab) {
            this.user = user;
            this.lab = lab;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getLab() {
            return lab;
        }

        public void setLab(String lab) {
            this.lab = lab;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof HelpId)) return false;
            HelpId helpId = (HelpId) o;
            return Objects.equals(user, helpId.user) &&
                    Objects.equals(lab, helpId.lab);
        }

        @Override
        public int hashCode() {
            return Objects.hash(user, lab);
        }
    }
}
