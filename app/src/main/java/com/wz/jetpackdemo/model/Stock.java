package com.wz.jetpackdemo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.wz.jetpackdemo.annotation.Range;

public class Stock implements Parcelable {
    @Range(min = 1,max = 20)
    public String name;
    @Range(min = 1,max = 10)
    public String stock_num;
    @Range(min = 1,max = 10)
    public int stock_id;

    public Stock(String name, String stock_num, int stock_id) {
        this.name = name;
        this.stock_num = stock_num;
        this.stock_id = stock_id;
    }

    protected Stock(Parcel in) {
        name = in.readString();
        stock_num = in.readString();
        stock_id = in.readInt();
    }

    public static final Creator<Stock> CREATOR = new Creator<Stock>() {
        @Override
        public Stock createFromParcel(Parcel in) {
            return new Stock(in);
        }

        @Override
        public Stock[] newArray(int size) {
            return new Stock[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(stock_num);
        dest.writeInt(stock_id);
    }
}
