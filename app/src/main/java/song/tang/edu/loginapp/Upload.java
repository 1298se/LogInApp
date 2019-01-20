package song.tang.edu.loginapp;

public class Upload {
    private String mName;
    private String mImageURL;
    private String mKey;

    public Upload() {

    }
    public Upload(String name, String ImgURL, String Key) {
        if(name.trim().equals("")){
            name = "No Name";
        }
        mName = name;
        mImageURL = ImgURL;
        mKey = Key;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImageURL() {
        return mImageURL;
    }

    public void setImageURL(String imageURL) {
        mImageURL = imageURL;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }
}
