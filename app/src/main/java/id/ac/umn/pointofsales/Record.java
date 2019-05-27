package id.ac.umn.pointofsales;

import java.util.Date;

public class Record {
    public Date date;
    public int total;
    public String id;

    public Record(Date date, int total, String id) {
        this.date = date;
        this.total = total;
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
