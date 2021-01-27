package gr.uom.newsmn;

import android.os.Parcel;
import android.os.Parcelable;

public class InstaPostIds implements Parcelable {

    private String url_id;

    private String media_url;

    protected InstaPostIds(Parcel in) {
        url_id = in.readString();
        media_url = in.readString();
    }

    public static final Creator<InstaPostIds> CREATOR = new Creator<InstaPostIds>() {
        @Override
        public InstaPostIds createFromParcel(Parcel in) {
            return new InstaPostIds(in);
        }

        @Override
        public InstaPostIds[] newArray(int size) {
            return new InstaPostIds[size];
        }
    };

    public InstaPostIds() {

    }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public String getUrl_id() {
        return url_id;
    }

    public void setUrl_id(String url_id) {
        this.url_id = url_id;
    }

    @Override
    public String toString() {
        return "InstaPostIds{" +
                "url_id='" + url_id + '\'' +
                ", media_url='" + media_url + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url_id);
        dest.writeString(media_url);
    }
}
