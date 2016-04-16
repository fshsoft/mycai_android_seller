package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Altas
 * @email Altas.Tutu@gmail.com
 * @time 2015-3-25 下午9:53:10
 */
public class Goods implements Parcelable {

    /**
     *
     */
    private static final long serialVersionUID = -7099504542131914230L;
    public int id;
    public String name;
    public int count;
    public double price;
    public List<String> imgs;
    public int saleCount;
    public int stock;
    public String date;
    public List<Norms> norms;
    public int duration;
    public List<StaffInfo> staff;
    public int status;
    public String brief;
    public int type;
    public GoodsCate cate;

    public ArrayList<Integer> staffIds;
    public int cateId;

    //
    public boolean checked;

    public Map<String, Object> getParams(){
        Map<String, Object> params = new HashMap<>();
        if(id > 0) {
            params.put("id", this.id);
        }
        params.put("name", this.name);
        params.put("imgs", this.imgs);
        params.put("norms", this.norms);
        params.put("brief", this.brief);
        params.put("price", this.price);
        params.put("duration", this.duration);
        params.put("staffs", this.staffIds);
        params.put("stock", this.stock);
        params.put("tradeId", this.cateId);

        return params;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.count);
        dest.writeDouble(this.price);
        dest.writeStringList(this.imgs);
        dest.writeInt(this.saleCount);
        dest.writeInt(this.stock);
        dest.writeString(this.date);
        dest.writeTypedList(norms);
        dest.writeInt(this.duration);
        dest.writeTypedList(staff);
        dest.writeInt(this.status);
        dest.writeString(this.brief);
        dest.writeInt(this.type);
        dest.writeParcelable(this.cate, 0);
        dest.writeList(this.staffIds);
        dest.writeInt(this.cateId);
        dest.writeByte(checked ? (byte) 1 : (byte) 0);
    }

    public Goods() {
    }

    protected Goods(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.count = in.readInt();
        this.price = in.readDouble();
        this.imgs = in.createStringArrayList();
        this.saleCount = in.readInt();
        this.stock = in.readInt();
        this.date = in.readString();
        this.norms = in.createTypedArrayList(Norms.CREATOR);
        this.duration = in.readInt();
        this.staff = in.createTypedArrayList(StaffInfo.CREATOR);
        this.status = in.readInt();
        this.brief = in.readString();
        this.type = in.readInt();
        this.cate = in.readParcelable(GoodsCate.class.getClassLoader());
        this.staffIds = new ArrayList<Integer>();
        in.readList(this.staffIds, List.class.getClassLoader());
        this.cateId = in.readInt();
        this.checked = in.readByte() != 0;
    }

    public static final Creator<Goods> CREATOR = new Creator<Goods>() {
        public Goods createFromParcel(Parcel source) {
            return new Goods(source);
        }

        public Goods[] newArray(int size) {
            return new Goods[size];
        }
    };
}
