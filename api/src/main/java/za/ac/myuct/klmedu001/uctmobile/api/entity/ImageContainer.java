package za.ac.myuct.klmedu001.uctmobile.api.entity;

/**
 * Created by eduardokolomajr on 2014/10/24.
 */
public class ImageContainer {
    private byte[] imageData;

    public ImageContainer(byte[] imageData) {
        this.imageData = imageData;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
}
