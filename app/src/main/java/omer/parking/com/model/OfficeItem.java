package omer.parking.com.model;

/**
 * Created by Kazimir on 6/29/2017.
 */

public class OfficeItem {
    private int officeID;
    private String officeName;
    private String officeAddress;
    private double latitude;
    private double longitude;

    public OfficeItem() {

    }

    public OfficeItem(int arg0, String arg1, String arg2, double arg3, double arg4) {
        this.officeID = arg0;
        this.officeName = arg1;
        this.officeAddress = arg2;
        this.latitude = arg3;
        this.longitude = arg4;
    }

    public void setOfficeID(int value) {
        this.officeID = value;
    }

    public int getOfficeID() {
        return officeID;
    }

    public void setOfficeName(String value) {
        this.officeName = value;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeAddress(String value) {
        this.officeAddress = value;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setLatitude(double value) {
        this.latitude = value;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLongitude(double value) {
        this.longitude = value;
    }

    public double getLongitude() {
        return longitude;
    }
}
