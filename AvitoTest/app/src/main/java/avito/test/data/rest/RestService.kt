package avito.test.data.rest

import avito.test.data.model.GitResponse
import avito.test.data.model.Repo
import avito.test.data.model.User
import retrofit.http.GET
import retrofit.http.Query
import rx.Observable

public interface RestService {

    GET("/search/users") public fun getUsers(Query("q") s: String): Observable<GitResponse<User>>
    GET("/search/repositories") public fun getRepos(Query("q") s: String): Observable<GitResponse<Repo>>
}
