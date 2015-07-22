package avito.test.data

import retrofit.Endpoint

public class MyEndPoint(private var url: String?) : Endpoint {

    public fun setUrl(url: String) {
        this.url = url
    }

    override fun getUrl(): String {
        if (url == null) throw IllegalStateException("url not set.")
        return url
    }

    override fun getName(): String {
        return "default"
    }
}