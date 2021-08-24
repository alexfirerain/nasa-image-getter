import com.fasterxml.jackson.annotation.JsonProperty;

public class NasaApodResponse {
    final private String  date,
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
               
               * * * * * * * * * * * * * * * * * * * * * * * * * * * *
                 * * * * NASA Astronomical Picture Of a Day * * * * *
               * * * * * * * * * * * * * * * * * * * * * * * * * * * *
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

}
