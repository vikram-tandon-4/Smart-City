package helpout.vikramtandonapps.com.helpout.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by vikra on 9/30/2017.
 */

public class CategoriesModel implements Serializable {

    private ArrayList<String> categories;

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }
}
