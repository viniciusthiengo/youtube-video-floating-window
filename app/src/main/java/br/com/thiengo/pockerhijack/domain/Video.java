package br.com.thiengo.pockerhijack.domain;

import android.os.Parcel;
import android.os.Parcelable;


public class Video implements Parcelable {
    public static final String KEY = "video_key";

    private String youTubeId;

    public String getYouTubeId() {
        return youTubeId;
    }

    public void setYouTubeId(String youTubeId) {
        this.youTubeId = youTubeId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.youTubeId);
    }

    public Video() {}

    protected Video(Parcel in) {
        this.youTubeId = in.readString();
    }

    public static final Parcelable.Creator<Video> CREATOR = new Parcelable.Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel source) {
            return new Video(source);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
}
