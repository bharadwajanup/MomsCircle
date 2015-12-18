package pcl.socialsupport.Utils;

/**
 * Created by Anup on 11/18/2015.
 */
public class AcceptListItem {
    private String support_image="";
    private String support_name="";
    private String support_time="";
    private String support_date="";
    private String chore_name="";
    private int phone;

    public AcceptListItem() {
        super();
    }

    public String getSupport_image() {
        return support_image;
    }

    public void setSupport_image(String image) {
        this.support_image = image;
    }

    public String getSupport_name() {
        return support_name;
    }

    public void setSupport_name(String sname) {
        this.support_name = sname;
    }
    public String getSupport_time()
    {
        return support_time;
    }
    public void setSupport_time(String stime)
    {
        support_time = stime;
    }

    public String getChore_name() {
        return chore_name;
    }

    public void setChore_name(String chore_name) {
        this.chore_name = chore_name;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getSupport_date() {
        return support_date;
    }

    public void setSupport_date(String support_date) {
        this.support_date = support_date;
    }
}
