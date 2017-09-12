package br.com.hrick.estoquepessoal.utils;

import br.com.hrick.estoquepessoal.api.RetrofitClient;
import br.com.hrick.estoquepessoal.api.UserApi;

/**
 * Created by henrique.pereira on 12/09/2017.
 */

public class ApiUtil {

    private ApiUtil() {
    }

    public static final String URL_MOCK = "http://www.mocky.io";

    public static UserApi getUserAPIService() {
        return RetrofitClient.getClient(URL_MOCK).create(UserApi.class);
    }
}
