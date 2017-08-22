package com.match.android.Utils;

/**
 * Created by Brant on 2017/8/6 1:08.
 */

import java.util.List;

public class CityModel {
    private String name;
    private List<DistrictModel> districtList;

    public CityModel() {
        super();
    }

    public CityModel(String name, List<DistrictModel> districtList) {
        super();
        this.name = name;
        this.districtList = districtList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<com.match.android.Utils.DistrictModel> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(List<DistrictModel> districtList) {
        this.districtList = districtList;
    }

    @Override
    public String toString() {
        return "CityModel [name=" + name + ", districtList=" + districtList
                + "]";
    }

}
