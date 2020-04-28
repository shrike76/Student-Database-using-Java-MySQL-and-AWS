package sample;

import java.util.ArrayList;
import java.util.List;

public class TypeofUser { //this class is for global modifiers
    public static String usertype = "";
    public static int itemlimit;
    public static int medialimit;
    private static List<Integer> cartArray = new ArrayList<Integer>();
    private static List<Integer> checkedOutArray = new ArrayList<Integer>();
    private static String username;

    public String getUserType() {
        return usertype;
    }

    public void setUserType(String user) {
        this.usertype = user;
    }

    public int getitemlimit() {
        return itemlimit;
    }

    public void setItemlimit(int limit) {
        this.itemlimit = limit;
    }

    public int getmedialimit() {
        return medialimit;
    }

    public void setmedialimit(int mlimit) {
        this.medialimit = mlimit;
    }

    public List<Integer> getcartarray() {
        return cartArray;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public List<Integer> getCheckedOutArray() {
        return checkedOutArray;
    }
}



