package br.com.hrick.estoquepessoal.api;

import br.com.hrick.estoquepessoal.entity.User;
import retrofit2.http.GET;
import rx.Observable;


/**
 * Created by henrique.pereira on 12/09/2017.
 */

public interface UserApi {

    @GET("/v2/58b9b1740f0000b614f09d2f")
    Observable<User> getUser();

}
