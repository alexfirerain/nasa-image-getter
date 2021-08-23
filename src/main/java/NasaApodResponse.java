import com.fasterxml.jackson.annotation.JsonProperty;

public class NasaApodResponse {
    String  date,
            explanation,
            hdUrl,
            mediaType,
            serviceVersion,
            title,
            url;

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
                Ответ от NASA для APOD:
                \tдата: %s
                \tпояснение: %s
                \tURL картинки с высоким разрешением: %s
                \tтип медиа: %s
                \tверсия сервиса: %s
                \tназвание: %s
                \tURL картинки с низким разрешением: %s
                """)
                .formatted(date, explanation, hdUrl, mediaType, serviceVersion, title, url);
    }
}
