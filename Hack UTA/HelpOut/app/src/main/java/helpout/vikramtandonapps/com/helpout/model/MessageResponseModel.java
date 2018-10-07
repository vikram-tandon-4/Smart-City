package helpout.vikramtandonapps.com.helpout.model;

import java.io.Serializable;

/**
 * Created by vikra on 9/30/2017.
 */

public class MessageResponseModel implements Serializable {

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
