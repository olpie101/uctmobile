package za.ac.myuct.klmedu001.uctmobile.api.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Created by eduardokolomajr on 2014/08/04.
 */

@Entity
public class Route {
    @Id
    Long id;              //Id
    @Index
    String name;            //Route name
    String displayCode;     //actual code to display (code seen on jammie timetables publically)
    String code;            //internal code (code used to differentiate between the different types and day times)
    String operatingDays;   //days set for operation
    String availability;    // Periods it is available (term, vac, etc)

    public Route(){

    }

    public Route(String availability, String name, String displayCode, String code, String operatingDays) {
        this.name = name;
        this.displayCode = displayCode;
        this.code = code;
        this.operatingDays = operatingDays;
        this.availability = availability.replaceAll(" ", "");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayCode() { return displayCode; }

    public void setDisplayCode(String displayCode) { this.displayCode = displayCode; }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOperatingDays() {
        return operatingDays;
    }

    public void setOperatingDays(String operatingDays) {
        this.operatingDays = operatingDays;
    }

    public String getAvailability() { return availability; }

    public void setAvailability(String availability) { this.availability = availability; }
}
