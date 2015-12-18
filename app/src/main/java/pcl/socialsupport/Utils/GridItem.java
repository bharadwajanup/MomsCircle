package pcl.socialsupport.Utils;

/**
 * Created by Anup on 11/9/2015.
 */
public class GridItem {
    private String image;
    private String title;
    private String id;
    private int phone;
    private int email;

    public GridItem() {
        super();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getID()
    {
        return id;
    }
    public void setId(String Id)
    {
        id = Id;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public int getEmail() {
        return email;
    }

    public void setEmail(int email) {
        this.email = email;
    }
}
