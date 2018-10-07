package helpout.vikramtandonapps.com.helpout.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by vikra on 9/30/2017.
 */

public class UserResponseModel implements Serializable {

    private ArrayList<UsersModel> users;

    public ArrayList<UsersModel> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<UsersModel> users) {
        this.users = users;
    }
}
