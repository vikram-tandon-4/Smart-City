package helpout.vikramtandonapps.com.helpout.model;


import java.io.Serializable;

public class LoginResponseModel implements Serializable {

    private boolean status;
    private String firstName;
    private String lastName;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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

}
