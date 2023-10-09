import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

@WebServlet(name = "AlbumServlet", value = "/albums/*")
public class AlbumServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        String urlPath = req.getPathInfo();
        Gson gson = new Gson();

        if (urlPath == null || urlPath.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write(gson.toJson(new ResponseMsg("Missing Parameter")));
            return;
        }
        System.out.println(urlPath);
        String[] urlParts = urlPath.split("/");
        System.out.println(urlParts.length);
        if (urlParts.length != 2) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            ResponseMsg msg = new ResponseMsg("Wrong URL Address");
            res.getWriter().write(gson.toJson(msg));
            return;
        }
        res.setStatus(HttpServletResponse.SC_OK);
        AlbumInfo albumInfo = new AlbumInfo("Sex Pistols", "Never Mind The Bollocks!", "1977");
        res.getWriter().write(gson.toJson(albumInfo));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String urlPath = request.getPathInfo();
        Gson gson = new Gson();

        if (urlPath != null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write(gson.toJson(new ResponseMsg("Wrong URL Address")));
            return;
        }

        // check content type to be multipart/form-data
        if (!request.getContentType().startsWith("multipart/form-data")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(new ResponseMsg("Invalid content type")));
            return;
        }
        // Extract image and profile from the request
        Part imagePart = request.getPart("image");
//        if (imagePart == null) {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            response.getWriter().write(gson.toJson(new ResponseMsg("Image is missing")));
//            return;
//        }

        long imageSize = imagePart == null ? 0: imagePart.getSize(); // get image size
        String albumID = "dummyID12345";

        String profileString = request.getParameter("profile");
        if (profileString == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(new ResponseMsg("Profile is missing")));
            return;
        }
        // Check if the JsonObject contains the expected fields
        if (!profileString.contains("artist") || !profileString.contains("title") || !profileString.contains("year")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(new ResponseMsg("Invalid profile data")));
            return;
        }

        // Return a success response
        ImageMetaData metaData = new ImageMetaData(albumID, String.valueOf(imageSize));
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(gson.toJson(metaData));
    }
}
