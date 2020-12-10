//package com.ws3dm.app.util.download;
//
//import android.os.Parcel;
//import androidx.annotation.Nullable;
//
//abstract class Program2 extends Program {
//
// private final int _id;
// private final String downLink;
// private final String name;
//
// Program2(
//     int _id,
//     @Nullable String downLink,
//     @Nullable String name) {
//   this._id = _id;
//   this.downLink = downLink;
//   this.name = name;
// }
//
// @Override
// public int _id() {
//   return _id;
// }
//
// @Nullable
// @Override
// public String downLink() {
//   return downLink;
// }
//
// @Nullable
// @Override
// public String name() {
//   return name;
// }
//
// @Override
// public String toString() {
//   return "Program{"
//       + "_id=" + _id + ", "
//       + "downLink=" + downLink + ", "
//       + "name=" + name
//       + "}";
// }
//
// @Override
// public boolean equals(Object o) {
//   if (o == this) {
//     return true;
//   }
//   if (o instanceof Program) {
//     Program that = (Program) o;
//     return (this._id == that._id())
//          && ((this.downLink == null) ? (that.downLink() == null) : this.downLink.equals(that.downLink()))
//          && ((this.name == null) ? (that.name() == null) : this.name.equals(that.name()));
//   }
//   return false;
// }
//
// @Override
// public int hashCode() {
//   int h = 1;
//   h *= 1000003;
//   h ^= this._id;
//   h *= 1000003;
//   h ^= (downLink == null) ? 0 : this.downLink.hashCode();
//   h *= 1000003;
//   h ^= (name == null) ? 0 : this.name.hashCode();
//   return h;
// }
//
// static final class Builder extends Program.Builder {
//   private Integer _id;
//   private String downLink;
//   private String name;
//   Builder() {
//   }
//   @Override
//   public Program.Builder _id(int _id) {
//     this._id = _id;
//     return this;
//   }
//   @Override
//   public Program.Builder downLink(@Nullable String downLink) {
//     this.downLink = downLink;
//     return this;
//   }
//   @Override
//   public Program.Builder name(@Nullable String name) {
//     this.name = name;
//     return this;
//   }
//   @Override
//   public Program build() {
//     String missing = "";
//     if (this._id == null) {
//       missing += " _id";
//     }
//     if (!missing.isEmpty()) {
//       throw new IllegalStateException("Missing required properties:" + missing);
//     }
//       return new Program2(
//               this._id,
//               this.downLink,
//               this.name) {
//           @Override
//           public int describeContents() {
//               return 0;
//           }
//
//           @Override
//           public void writeToParcel(Parcel parcel, int i) {
//
//           }
//       };
//   }
// }
//}
