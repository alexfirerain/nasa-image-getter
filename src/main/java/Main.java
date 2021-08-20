import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

public class Main {
    public static final String NASA_SOURCE = "https://api.nasa.gov/planetary/apod?api_key=";
    public final static String MY_KEY = "A0mxeLsHT8T9su74w00tjMKCtDbGYoBboK4jf3re";


    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setUserAgent("The Cat-Scanner Service")
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

    }
}