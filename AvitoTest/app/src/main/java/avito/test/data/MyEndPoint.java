package avito.test.data;

import retrofit.Endpoint;

public class MyEndPoint implements Endpoint {

  private String url;

  public MyEndPoint(String serverUrl) {
    url = serverUrl;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Override public String getUrl() {
    if (url == null) throw new IllegalStateException("url not set.");
    return url;
  }

  @Override public String getName() {
    return "default";
  }
}