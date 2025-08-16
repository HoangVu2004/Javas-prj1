package AI_PRJ.WEBAPP.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
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

    public Help() {
    }

    public Help(String user, String lab, int supportCount) {
        this.user = user;
        this.lab = lab;
        this.supportCount = supportCount;
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
