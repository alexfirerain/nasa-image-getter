import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
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
        NasaApodResponse apod = apodMapper.readValue(response.getEntity().getContent(), NasaApodResponse.class);

//        NasaApodResponse apod = new GsonBuilder().create().fromJson(
//                response.getEntity().getContent().toString(), NasaApodResponse.class);


        System.out.println(apod);
        obtainDescription(apod, "");
        saveImage(httpClient, apod.getUrl(), "");
        saveImage(httpClient, apod.getHdUrl(), "");

    }

    public static void obtainDescription(NasaApodResponse apod, String whereToSave) {
        String nameToSave = "NASA picture of a day " + apod.getDate() + " - description.txt";
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
                System.out.println("Пояснение повторно не скачено.");
                return;
            }
        }
        try (FileWriter writer = new FileWriter(savePath, false)) {
            writer.write(content.toString());
            writer.flush();
        } catch (IOException e) {
            System.out.println("Ошибка сохранения " + nameToSave + ". " + e.getMessage());
        }
        System.out.println("Файл " + nameToSave + " сохранён в " +
                (whereToSave.isBlank() ? " папке проекта." : whereToSave));
        System.out.println();
    }

    public static void saveImage(CloseableHttpClient connection, String fromWhereToSave, String whereToSave) throws IOException {
        String nameToSave = fromWhereToSave.substring(fromWhereToSave.lastIndexOf("/") + 1);
        String savePath = whereToSave + (whereToSave.isBlank() ? "" : "\\") + nameToSave;
        File save = new File(savePath);
        if (save.exists()) {
            System.out.println("Файл " + nameToSave + " уже существует.\n" +
                    "Перезаписать? ('+' для подтверждения)");
            Scanner input = new Scanner(System.in);
            if (!input.nextLine().equals("+")) {
                System.out.println("Повторное скачивание отменено.");
                return;
            }
            if (save.delete())
                System.out.println("Прежний файл " + nameToSave + " удалён.");
        }

        HttpEntity entity = connection.execute(new HttpGet(fromWhereToSave)).getEntity();
        if (entity != null) {
            FileOutputStream fos = new FileOutputStream(save);
            entity.writeTo(fos);
            fos.close();
        } else {
            System.out.println("Данных для сохранения не получено :(");
            return;
        }
        System.out.println("Файл " + nameToSave + " сохранён в " +
                (whereToSave.isBlank() ? " папке проекта." : whereToSave));
        System.out.println();
    }

}