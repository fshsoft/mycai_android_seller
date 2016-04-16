package com.fanwe.seallibrary.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 地区-本地库
 * Created by tu on 15/11/23.
 */
public class RegionLocal {
    public String i;
    public String n;
    public List<RegionLocal> child;
    public List<RegionLocal> group;

    public RegionLocal(String i, String n, List<RegionLocal> child, List< RegionLocal> group) {
        this.i = i;
        this.n = n;
        this.child = child;
        this.group = group;
    }

    public RegionLocal() {
        i="0";
        n="";
        child=new ArrayList<>();
        group=new ArrayList<>();

    }
}
