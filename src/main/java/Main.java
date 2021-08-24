import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static final String NASA_SOURCE = "https://api.nasa.gov/planetary/apod?api_key=";
    public final static String MY_KEY = "A0mxeLsHT8T9su74w00tjMKCtDbGYoBboK4jf3re";
    private static final ObjectMapper apodMapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setUserAgent("NASA Image Hunter")
                .setDefaultRequestConfig(
                        RequestConfig.custom()
                                .setConnectTimeout(5_000)
                                .setSocketTimeout(30_000)
                                .setRedirectsEnabled(false)
                                .build()
                ).build();

        HttpGet request = new HttpGet(NASA_SOURCE + MY_KEY);
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());

        CloseableHttpResponse response = httpClient.execute(request);

//        Arrays.stream(response.getAllHeaders()).forEach(System.out::println);
//        String body = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);

        NasaApodResponse apod = apodMapper.readValue(response.getEntity().getContent(), NasaApodResponse.class);
        System.out.println(apod);

        saveDescription(apod, "");

//        apod.saveDescription("");
//        apod.saveLowResImage("");
//        apod.saveHiResImage("");

    }

    public static void saveDescription(NasaApodResponse apod, String whereToSave) {
        String nameToSave = apod.namePatternForFiles() + " - description.txt";
        String savePath = whereToSave + (whereToSave.isBlank() ? "" : "\\") + nameToSave;   //?
        File save = new File(savePath);

        StringBuilder content = new StringBuilder()
                .append("\t# # # ").append(apod.getTitle()).append(" # # #\r\n")
                .append("a picture of ").append(apod.getDate()).append("\r\n\r\n")
                .append(apod.getExplanation()).append("\r\n");

        if (save.exists()) {
            System.out.println("Файл " + nameToSave + " уже существует.\n" +
                    "Перезаписать? ('+' для подтверждения)");
            Scanner input = new Scanner(System.in);
            if (!input.nextLine().equals("+")) {
                System.out.println("Описание не сохранено.");
                return;
            }
        }
        try (FileWriter writer = new FileWriter(savePath, false)) {
            writer.write(content.toString());
            writer.flush();
        } catch (IOException e) {
            System.out.println("Ошибка сохранения " + nameToSave + ". " + e.getMessage());
        }
    }

    public static void saveLowResImage(NasaApodResponse apod, String whereToSave) {
        String nameToSave = apod.namePatternForFiles() + " - low resolution.jpg";
        String savePath = whereToSave + (whereToSave.isBlank() ? "" : "\\") + nameToSave;
        File save = new File(savePath);
//        try
//        {
//            URL imageURL = new URL(apod.getUrl());
//            HttpURLConnection imageConnection;
//            imageConnection = (HttpURLConnection) imageURL.openConnection();
//            imageConnection.setRequestMethod("GET");
//            imageConnection.connect();
//
//            InputStream in = imageConnection.getInputStream();
//            OutputStream writer = new FileOutputStream(save);
//            byte[] buffer = new byte[buffSize];
//            int c = in.read(buffer);
//            while (c > 0) {
//                writer.write(buffer, 0, c);
//                c = in.read(buffer);
//            }
//            writer.flush();
//            writer.close();
//            in.close();
//        } catch (Exception e) {
//            System.out.println(e);
//        }
    }
}