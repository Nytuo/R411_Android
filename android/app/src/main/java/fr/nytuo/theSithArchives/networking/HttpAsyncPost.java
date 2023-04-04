package fr.nytuo.theSithArchives.networking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;

public class HttpAsyncPost {

    public HttpAsyncPost(String url, Object object, PostExecutePost postExecuteActivity) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = null;
        try {
            jsonStr = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String finalJsonStr = jsonStr;
        Runnable runnable = () -> {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);
                connection.getOutputStream().write(finalJsonStr.getBytes());
                connection.getOutputStream().flush();
                connection.getOutputStream().close();
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    postExecuteActivity.runOnUiThread(() -> postExecuteActivity.onSucces());
                } else {
                    postExecuteActivity.runOnUiThread(() -> postExecuteActivity.onError(responseCode));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        Executors.newSingleThreadExecutor().execute(runnable);


    }
}
