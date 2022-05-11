package guc.bttsBtngan.user.data;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

// For postgresql
@Entity
@Table
public class UserUserInteraction {
    @Id
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
//    @Id @GeneratedValue(generator="system-uuid")
//    @GenericGenerator(name="system-uuid", strategy = "uuid")

    private long id;
    private String username;
    private String Password;
    private String email;
    private String photoRef;
    private boolean isModerator;
    private boolean isBanned;

    public UserUserInteraction() {
    }

    public UserUserInteraction(long id, String username, String password, String email, String photoRef, boolean isModerator, boolean isBanned) {
        super();
        this.id = id;
        this.username = username;
        Password = password;
        this.email = email;
        this.photoRef = photoRef;
        this.isModerator = isModerator;
        this.isBanned = isBanned;
    }

    public UserUserInteraction(String username, String password, String email, String photoRef, boolean isModerator, boolean isBanned) {
        super();
        this.username = username;
        Password = password;
        this.email = email;
        this.photoRef = photoRef;
        this.isModerator = isModerator;
        this.isBanned = isBanned;
    }

    public long getid() {
        return id;
    }

    public void setid(long id) {
        this.id = id;
    }

    public String getusername() {
        return username;
    }

    public void setusername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoRef() {
        return photoRef;
    }

    public void setPhotoRef(String photoRef) {
        this.photoRef = photoRef;
    }

    public boolean isModerator() {
        return isModerator;
    }

    public void setModerator(boolean moderator) {
        isModerator = moderator;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    @Override
    public String toString() {
        return "UserUserInteraction{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", Password='" + Password + '\'' +
                ", email='" + email + '\'' +
                ", photoRef='" + photoRef + '\'' +
                ", isModerator=" + isModerator +
                ", isBanned=" + isBanned +
                '}';
    }
}
