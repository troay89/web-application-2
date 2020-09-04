package application.entities;



import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "singer")
public class Singer {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(name = "version")
    private int version;

    @NotBlank(message="{validation.firstname.NotBlank.message}")
    @Size(min = 3, max = 60, message = "{validation.firstname.Size.message}")
    @Column(name = "first_name")
    private String firstName;

    @NotBlank(message="{validation.lastname.NotBlank.message}")
    @Size(min = 1, max = 40, message = "{validation.lastname.Size.message}")
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Column(name = "birth_date")
    @Temporal(TemporalType.DATE)
    private Date birthDate;

    @Column(name = "description")
    private String description;

    @Basic(fetch = FetchType.LAZY)
    @Lob
    @Column(name = "photo")
    private byte[] photo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    @Transient
    public String getBirthDateString(){
        String birthDateString = "";
        if(birthDate != null){
            birthDateString = new SimpleDateFormat("yyyy-MM-dd").format(birthDate);
        }
        return birthDateString;
    }

    @Override
    public String toString() {
        return "Singer - id: " + id + ", firstName: " + firstName + ", lastName: " + lastName +
                ", birthDate: " + birthDate + ", description: " + description;
    }
}
