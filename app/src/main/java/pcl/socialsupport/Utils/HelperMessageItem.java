package pcl.socialsupport.Utils;

/**
 * Created by Anup on 11/27/2015.
 */
public class HelperMessageItem {
    private String name;
    private String options;
    private int assigned_to;
    private String message;
    private int choreActivityTrackId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public int getAssigned_to() {
        return assigned_to;
    }

    public void setAssigned_to(int assigned_to) {
        this.assigned_to = assigned_to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getChoreActivityTrackId() {
        return choreActivityTrackId;
    }

    public void setChoreActivityTrackId(int choreActivityTrackId) {
        this.choreActivityTrackId = choreActivityTrackId;
    }
}
