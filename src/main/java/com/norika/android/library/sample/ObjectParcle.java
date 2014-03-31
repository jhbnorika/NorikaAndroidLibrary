
package com.norika.android.library.sample;

import android.os.Parcel;
import android.os.Parcelable;

public class ObjectParcle implements Parcelable {

    private String name;
    private int num;

    public ObjectParcle() {

    }

    private ObjectParcle(Parcel in) {
        name = in.readString();
        num = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(num);
    }

    public static final Parcelable.Creator<ObjectParcle> CREATOR = new Parcelable.Creator<ObjectParcle>() {
        @Override
        public ObjectParcle createFromParcel(Parcel source) {
            return new ObjectParcle(source);
        }

        @Override
        public ObjectParcle[] newArray(int size) {
            return new ObjectParcle[size];
        }
    };

}
