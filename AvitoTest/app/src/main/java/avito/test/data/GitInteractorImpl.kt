package avito.test.data

import android.content.Context
import avito.test.BuildConfig
import avito.test.R
import avito.test.data.model.GitData
import avito.test.data.model.GitResponse
import avito.test.data.model.Repo
import avito.test.data.model.User
import avito.test.data.rest.RestService
import java.util.concurrent.TimeUnit
import retrofit.RestAdapter
import retrofit.RetrofitError
import retrofit.client.OkClient
import rx.Observable
import rx.Subscriber
import rx.functions.Func2
import rx.schedulers.Schedulers
import rx.subjects.BehaviorSubject

public class GitInteractorImpl(private val context: Context) {
    private val service: RestService

    private val userSubject: BehaviorSubject<List<User>>
    private val repoSubject: BehaviorSubject<List<Repo>>
    private val searchSubject: BehaviorSubject<String>
    private val errorSubject: BehaviorSubject<Int>

    init {
        val adapter = RestAdapter.Builder()
                .setLogLevel(
                    if (BuildConfig.DEBUG) RestAdapter.LogLevel.FULL
                    else RestAdapter.LogLevel.NONE).setEndpoint(MyEndPoint(context.getResources().getString(R.string.git_url)))
                .setClient(OkClient())
                .build()
        service = adapter.create<RestService>(javaClass<RestService>())

        userSubject = BehaviorSubject.create<List<User>>()
        repoSubject = BehaviorSubject.create<List<Repo>>()
        searchSubject = BehaviorSubject.create<String>()
        errorSubject = BehaviorSubject.create<Int>()

        initSubject()
    }

    public fun getErrorSubject(): Observable<Int> = errorSubject

    public fun getUserSubject(): Observable<GitData> = userSubject.zipWith(repoSubject,
            { user, repo ->
                val data = GitData(user, repo);
                data
            })

    private fun initSubject() {
        searchSubject.debounce(500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .subscribe({s -> searchUsers(s); searchRepos(s)},
                    {e -> handleError(e)})
    }

    private fun searchRepos(s: String) {
        service.getRepos(s).subscribe({r -> repoSubject.onNext(r.items)},
                {e -> handleError(e)})
    }

    private fun searchUsers(s: String) {
        service.getUsers(s).subscribe({r -> userSubject.onNext(r.items)},
                {e -> handleError(e)})
    }

    private fun handleError(e: Throwable) {
        when(e) {
            is RetrofitError -> {
                if (e.getResponse() != null) errorSubject.onNext(e.getResponse().getStatus())
                else errorSubject.onNext(-2)
            }
            else -> errorSubject.onNext(-1)
        }
    }

    public fun search(s: String) {
        searchSubject.onNext(s)
    }

    companion object {

        private var instance: GitInteractorImpl? = null

        public fun getInstance(context: Context): GitInteractorImpl? {
            if (instance == null) {
                instance = GitInteractorImpl(context)
            }
            return instance
        }
    }
}
