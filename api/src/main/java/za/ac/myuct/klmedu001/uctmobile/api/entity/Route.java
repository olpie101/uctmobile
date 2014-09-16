package za.ac.myuct.klmedu001.uctmobile.api.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by eduardokolomajr on 2014/08/04.
 */

@Entity
public class Route {
    @Id
    Long id;              //Id
    String name;            //Route name
    String displayCode;     //actual code to display
    String code;            //internal code
    String operatingDays;   //days set for operation

    public Route(){

    }

    public Route(String name, String displayCode, String code, String operatingDays) {
        this.name = name;
        this.displayCode = displayCode;
        this.code = code;
        this.operatingDays = operatingDays;
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
}
