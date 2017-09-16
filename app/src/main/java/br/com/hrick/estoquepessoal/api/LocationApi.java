package br.com.hrick.estoquepessoal.api;

import br.com.hrick.estoquepessoal.entity.User;
import br.com.hrick.estoquepessoal.location.ResultLocation;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;


/**
 * Created by henrique.pereira on 12/09/2017.
 */

public interface LocationApi {

    @GET("/maps/api/geocode/json")
    Observable<ResultLocation> getLocation(@Query("address") String address, @Query("sensor") boolean sensor);
}
