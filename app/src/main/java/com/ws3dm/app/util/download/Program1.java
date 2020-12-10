//package com.ws3dm.app.util.download;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//
//final class Program1 extends Program2 {
//  public static final Parcelable.Creator<Program1> CREATOR = new Parcelable.Creator<Program1>() {
//    @Override
//    public Program1 createFromParcel(Parcel in) {
//      return new Program1(
//          in.readInt(),
//          in.readInt() == 0 ? in.readString() : null,
//          in.readInt() == 0 ? in.readString() : null
//      );
//    }
//    @Override
//    public Program1[] newArray(int size) {
//      return new Program1[size];
//    }
//  };
//
//  Program1(int _id, String downLink, String name) {
//    super(_id, downLink, name);
//  }
//
//  @Override
//  public void writeToParcel(Parcel dest, int flags) {
//    dest.writeInt(_id());
//    if (downLink() == null) {
//      dest.writeInt(1);
//    } else {
//      dest.writeInt(0);
//      dest.writeString(downLink());
//    }
//    if (name() == null) {
//      dest.writeInt(1);
//    } else {
//      dest.writeInt(0);
//      dest.writeString(name());
//    }
//  }
//
//  @Override
//  public int describeContents() {
//    return 0;
//  }
//}
