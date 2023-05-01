package entity;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@NamedQuery(name = "threeLastEnteries",query = "select a from AttemptsEntity a where a.userId = ?1 and a.allowed = false order by a.date desc")
@Table(name = "attempts", schema = "public", catalog = "loginProject")
public class AttemptsEntity {
    private int attemptId;
    private int userId;
    private String passwordEntered;
    private Boolean allowed;
    private Timestamp date;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "attempt_id", nullable = false)
    public int getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(int attemptId) {
        this.attemptId = attemptId;
    }

    @Basic
    @Column(name = "user_id", nullable = false)
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "password_entered", nullable = false, length = -1)
    public String getPasswordEntered() {
        return passwordEntered;
    }

    public void setPasswordEntered(String passwordEntered) {
        this.passwordEntered = passwordEntered;
    }

    @Basic
    @Column(name = "allowed", nullable = true)
    public Boolean getAllowed() {
        return allowed;
    }

    public void setAllowed(Boolean allowed) {
        this.allowed = allowed;
    }

    @Basic
    @Column(name = "date", nullable = false)
    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AttemptsEntity that = (AttemptsEntity) o;

        if (attemptId != that.attemptId) return false;
        if (userId != that.userId) return false;
        if (passwordEntered != null ? !passwordEntered.equals(that.passwordEntered) : that.passwordEntered != null)
            return false;
        if (allowed != null ? !allowed.equals(that.allowed) : that.allowed != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = attemptId;
        result = 31 * result + userId;
        result = 31 * result + (passwordEntered != null ? passwordEntered.hashCode() : 0);
        result = 31 * result + (allowed != null ? allowed.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }
}
