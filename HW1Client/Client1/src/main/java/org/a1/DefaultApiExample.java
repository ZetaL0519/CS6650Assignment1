package org.a1;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.AlbumInfo;
import io.swagger.client.model.AlbumsProfile;

import java.io.File;

public class DefaultApiExample {

    public static void main(String[] args) {

        DefaultApi apiInstance = new DefaultApi();
        ApiClient client = apiInstance.getApiClient();
        File image = new File("/Users/zoelee/Documents/neu/cs6650/Assignment1/HW1Servlet/textimage.png");
        AlbumsProfile albumsProfile = new AlbumsProfile().artist("www").year("1988").title("huhu");
        client.setBasePath("http://localhost:8080/MusicService_war_exploded");
        String albumID = "albumID_example"; // String | path  parameter is album key to retrieve
        try {
            apiInstance.newAlbum(image, albumsProfile);
            AlbumInfo result = apiInstance.getAlbumByKey(albumID);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DefaultApi#getAlbumByKey");
            e.printStackTrace();
        }
    }
}
