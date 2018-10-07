package helpout.vikramtandonapps.com.helpout.model;

import java.io.Serializable;

/**
 * Created by vikra on 9/30/2017.
 */

public class UsersModel implements Serializable{

    private String imageUrl;
    private String firstName;
    private String lastName;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
