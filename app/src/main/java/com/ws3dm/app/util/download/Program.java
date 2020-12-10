//package com.ws3dm.app.util.download;
//
//import android.os.Parcelable;
//import androidx.annotation.Nullable;
//
//import com.squareup.sqldelight.RowMapper;
//import com.ws3dm.app.ProgramModel;
//
///**
// * 要下载的数据bean
// * Created by litp on 2017/4/18.
// */
//
////@AutoValue
//public abstract class Program implements Parcelable,ProgramModel{
//
//
//
//
//    public static final Factory<Program> FACTORY = new Factory<>(new ProgramModel.Creator<Program>() {
//        @Override
//        public Program create(int _id, @Nullable String downLink, @Nullable String name) {
//            return new Program1(_id,downLink,name);
//        }
//    });
//
//
//    public static Program create(String downLink, String name) {
//        return builder()
//                ._id(0)  //id自动增长的
//                .downLink(downLink)
//                .name(name)
//                .build();
//    }
//
//
//    public static Builder builder() {
//        return new Program1.Builder();
//    }
//
//
////    @AutoValue.Builder
//    public abstract static class Builder {
//        public abstract Builder _id(int _id);
//
//        public abstract Builder downLink(String downLink);
//
//        public abstract Builder name(String name);
//
//        public abstract Program build();
//    }
//
//
//    //查询title映射
//    public final static RowMapper<String> ROW_NAMW_MAPPER = FACTORY.selectDownNameMapper();
//
//
//}
