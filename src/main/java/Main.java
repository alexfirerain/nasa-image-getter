import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static final String NASA_SOURCE = "https://api.nasa.gov/planetary/apod?api_key=";
    public final static String MY_KEY = "A0mxeLsHT8T9su74w00tjMKCtDbGYoBboK4jf3re";
    private static ObjectMapper apodMapper = new ObjectMapper();

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

        Arrays.stream(response.getAllHeaders()).forEach(System.out::println);
//        String body = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);

        NasaApodResponse apod = apodMapper.readValue(response.getEntity().getContent(), NasaApodResponse.class);
        System.out.println(apod);



    }
}