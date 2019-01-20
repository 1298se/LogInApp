package song.tang.edu.loginapp;

public class Upload {
    private String mName;
    private String mImageURL;

    public Upload() {

    }
    public Upload(String name, String ImgURL) {
        if(name.trim().equals("")){
            name = "No Name";
        }
        mName = name;
        mImageURL = ImgURL;
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
}
