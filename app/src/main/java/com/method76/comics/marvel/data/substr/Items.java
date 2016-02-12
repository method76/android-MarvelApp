package com.method76.comics.marvel.data.substr;

import android.os.Parcel;
import android.os.Parcelable;

import com.method76.common.util.Log;

/**
 * Created by Sungjoon Kim on 2016-01-30.
 */
public class Items implements Parcelable {

    // Original properties
    private String title;
    private String link;
    private String displayLink;
    private String mime;
    private Image image;

    // Custom properties
    private String contextLink;
    private int height;
    private int width;
    private long byteSize;
    private String thumbnailLink;
    private int thumbnailHeight;
    private int thumbnailWidth;

    /**
     * Standard basic constructor for non-parcel
     * object creation
     */
    public Items(){};

    /**
     *
     * Constructor to use when re-constructing object
     * from a parcel
     *
     * @param in a parcel from which to read this object
     */
    public Items(Parcel in) {
        readFromParcel(in);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDisplayLink() {
        return displayLink;
    }

    public void setDisplayLink(String displayLink) {
        this.displayLink = displayLink;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public long getByteSize() {
        if(byteSize==0){
            try{
                byteSize = getImage().getByteSize();
            }catch(Exception e){
                Log.w(e);
            }
        }
        return byteSize;
    }

    public void setByteSize(long byteSize) {
        this.byteSize = byteSize;
    }

    public String getContextLink() {
        if(contextLink==null){
            try{
                contextLink = getImage().getContextLink();
            }catch(Exception e){
                Log.w(e);
            }
        }
        return contextLink;
    }

    public void setContextLink(String contextLink) {
        this.contextLink = contextLink;
    }

    public int getHeight() {
        if(height==0){
            try{
                height = getImage().getHeight();
            }catch(Exception e){
                Log.w(e);
            }
        }
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getThumbnailHeight() {
        if(thumbnailHeight==0){
            try{
                thumbnailHeight = getImage().getThumbnailHeight();
            }catch(Exception e){
                Log.w(e);
            }
        }
        return thumbnailHeight;
    }

    public void setThumbnailHeight(int thumbnailHeight) {
        this.thumbnailHeight = thumbnailHeight;
    }

    public String getThumbnailLink() {
        if(thumbnailLink==null){
            try{
                thumbnailLink = getImage().getThumbnailLink();
            }catch(Exception e){
                Log.w(e);
            }
        }
        return thumbnailLink;
    }

    public void setThumbnailLink(String thumbnailLink) {
        this.thumbnailLink = thumbnailLink;
    }

    public int getThumbnailWidth() {
        if(thumbnailWidth==0){
            try{
                thumbnailWidth = getImage().getThumbnailWidth();
            }catch(Exception e){
                Log.w(e);
            }
        }
        return thumbnailWidth;
    }

    public void setThumbnailWidth(int thumbnailWidth) {
        this.thumbnailWidth = thumbnailWidth;
    }

    public int getWidth() {
        if(width==0){
            try{
                width = getImage().getWidth();
            }catch(Exception e){
                Log.w(e);
            }
        }
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // We just need to write each field into the
        // parcel. When we read from parcel, they
        // will come back in the same order
        dest.writeString(title);
        dest.writeString(link);
        dest.writeString(displayLink);
        dest.writeString(mime);
        dest.writeString(contextLink);
        dest.writeInt(height);
        dest.writeInt(width);
        dest.writeLong(byteSize);
        dest.writeString(thumbnailLink);
        dest.writeInt(thumbnailHeight);
        dest.writeInt(thumbnailWidth);
    }


    /**
     *
     * Called from the constructor to create this
     * object from a parcel.
     *
     * @param in parcel from which to re-create object
     */
    private void readFromParcel(Parcel in) {
        // We just need to read back each
        // field in the order that it was
        // written to the parcel
        title           = in.readString();
        link            = in.readString();
        displayLink     = in.readString();
        mime            = in.readString();
        contextLink     = in.readString();
        height          = in.readInt();
        width           = in.readInt();
        byteSize        = in.readLong();
        thumbnailLink   = in.readString();
        thumbnailHeight = in.readInt();
        thumbnailWidth  = in.readInt();
    }


    /**
     *
     * This field is needed for Android to be able to
     * create new objects, individually or as arrays.
     *
     * This also means that you can use use the default
     * constructor to create the object and use another
     * method to hyrdate it as necessary.
     *
     * I just find it easier to use the constructor.
     * It makes sense for the way my brain thinks ;-)
     *
     */
    public static final Creator CREATOR =
        new Creator() {
            public Items createFromParcel(Parcel in) {
                return new Items(in);
            }
            public Items[] newArray(int size) {
                return new Items[size];
            }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("title: " + getTitle());
        buf.append(", size: " + getWidth());
        buf.append("x" + getHeight() + "("+getByteSize()+")");
        buf.append(", thumb: " + getThumbnailLink());
        buf.append(", img: " + getLink());
        buf.append(", ctxLink: " + getContextLink());
        return buf.toString();
    }
}
