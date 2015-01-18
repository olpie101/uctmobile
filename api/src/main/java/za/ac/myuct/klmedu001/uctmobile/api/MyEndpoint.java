/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package za.ac.myuct.klmedu001.uctmobile.api;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.ConflictException;
import com.google.appengine.api.images.Image;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.inject.Named;

import za.ac.myuct.klmedu001.uctmobile.api.entity.ImageContainer;

/** An endpoint class we are exposing */
@Api(name = "myApi", version = "v1", namespace = @ApiNamespace(ownerDomain = "api.uctmobile.klmedu001.myuct.ac.za", ownerName = "api.uctmobile.klmedu001.myuct.ac.za", packagePath=""))
public class MyEndpoint {

    /** A simple endpoint method that takes a name and says Hi back */
    @ApiMethod(name = "sayHi")
    public MyBean sayHi(@Named("name") String name) {
        MyBean response = new MyBean();
        response.setData("Hi, " + name);

        return response;
    }

    @ApiMethod(name = "image")
    public ImageContainer image(@Named("filename") String filename) throws ConflictException{
        File imageFile = new File("assets/"+filename);


        byte [] fileData ;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {

            FileInputStream is = new FileInputStream(imageFile);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            byte[] data = new byte[16384];

            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();

            fileData = buffer.toByteArray();
        }catch (IOException e){
            throw new ConflictException("io exception");
        }

        return new ImageContainer(fileData);
    }
}
