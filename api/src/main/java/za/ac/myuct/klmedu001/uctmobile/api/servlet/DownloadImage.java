package za.ac.myuct.klmedu001.uctmobile.api.servlet;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by eduardokolomajr on 2014/10/24.
 * Downloads image that is requested in the URL
 *
 * Due to App Engine's sandboxing restrictions,  couldn't use <code>ImageIO</code> or <code>java.nio</code>,
 * could only use <code>java.io</code>.
 *
 * Solution to this problem:
 * Response protocol found at:
 * http://stackoverflow.com/questions/12990072/how-to-create-a-download-link-from-the-google-app-engine-using-java
 * answer by Olyanren;
 *
 * Converting a File from <code>InputStream</code> to a <code>byte[]</code> found at:
 * http://stackoverflow.com/questions/1264709/convert-inputstream-to-byte-array-in-java
 * answer by Adamski
 *
 *
 */
public class DownloadImage extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        String imageName = uri.substring(uri.lastIndexOf('/')+1);

        File imageFile = new File("assets/"+imageName);
        byte [] fileData = new byte[0];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            resp.setContentType("application/octet-stream");
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
            resp.setContentType("application/octet-stream");
            resp.getWriter().println("io exp");
        }

        OutputStream outputStream = resp.getOutputStream();
        outputStream.write(fileData);
    }
}
