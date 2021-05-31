package anthony.uteq.apisconsume.models;

import com.google.gson.JsonArray;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IJournalApis {

    @GET("ws/issues.php")
    Call<JsonArray> getdataR(@Query("j_id") String identificador);
}
