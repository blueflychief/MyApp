package com.infinite.myapp.utils.networkutil;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface ApiService {

    /**
     * Server api host.
     */
    String BASE_URL = "";

    /**
     * Download file.
     *
     * @param fileUrl
     * @param range
     * @return
     */
    @Streaming
    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl, @Header("Range") String range);
}
