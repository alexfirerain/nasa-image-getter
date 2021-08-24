import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class NasaApodResponse {
    private String  date,
            explanation,
            hdUrl,
            mediaType,
            serviceVersion,
            title,
            url;

    public String getDate() {
        return date;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getHdUrl() {
        return hdUrl;
    }

    public String getMediaType() {
        return mediaType;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public NasaApodResponse(
           @JsonProperty("date") String date,
           @JsonProperty("explanation") String explanation,
           @JsonProperty("hdurl") String hdUrl,
           @JsonProperty("media_type") String mediaType,
           @JsonProperty("service_version") String serviceVersion,
           @JsonProperty("title") String title,
           @JsonProperty("url") String url) {
        this.date = date;
        this.explanation = explanation;
        this.hdUrl = hdUrl;
        this.mediaType = mediaType;
        this.serviceVersion = serviceVersion;
        this.title = title;
        this.url = url;
    }

    @Override
    public String toString() {
        return ("""
               
               * * * * * NASA Astronomical Picture Of a Day * * * * * *
               *
               *\t<версия сервиса: %5$s; тип медиа: %4$s>
               *
               *\tназвание: %6$s
               *\tдата: %1$s
               *\tпояснение: %2$s
               *\tURL картинки с низким разрешением: %7$s
               *\tURL картинки с высоким разрешением: %3$s
               *
               * * * * * * * * * * * * * * * * * * * * * * * * * * * *
                
                """)
                .formatted(date, explanation, hdUrl, mediaType, serviceVersion, title, url);
    }

    public String namePatternForFiles() {
        return "NASA picture of a day " + date;
    }

    public void saveDescription(String dirToSave) {
        String nameToSave = namePatternForFiles() + " - description.txt";
        String savePath = dirToSave + (dirToSave.isBlank() ? "" : "\\") + nameToSave;
        File save = new File(savePath);

        StringBuilder content = new StringBuilder()
                .append("\t# # # ").append(title).append(" # # #\r\n")
                .append("a picture of ").append(date).append("\r\n\r\n")
                .append(explanation).append("\r\n");

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

    public void saveLowResImage(String dirToSave) {
        String nameToSave = namePatternForFiles() + " - low resolution.jpg";
        String savePath = dirToSave + (dirToSave.isBlank() ? "" : "\\") + nameToSave;
        File save = new File(savePath);
    }

    public void saveHiResImage(String dirToSave) {
        String nameToSave = namePatternForFiles() + " - high resolution.jpg";
        String savePath = dirToSave + (dirToSave.isBlank() ? "" : "\\") + nameToSave;
        File save = new File(savePath);
    }

}
