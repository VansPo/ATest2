package avito.test.data.rest;

import avito.test.data.model.GitResponse;
import avito.test.data.model.Repo;
import avito.test.data.model.User;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface RestService {

  @GET("/search/users") Observable<GitResponse<User>> getUsers(@Query("q") String s);
  @GET("/search/repositories") Observable<GitResponse<Repo>> getRepos(@Query("q") String s);
}
