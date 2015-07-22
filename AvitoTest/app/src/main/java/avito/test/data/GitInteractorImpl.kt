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
        val adapter = RestAdapter.Builder().setLogLevel(if (BuildConfig.DEBUG) RestAdapter.LogLevel.FULL else RestAdapter.LogLevel.NONE).setEndpoint(MyEndPoint(context.getResources().getString(R.string.git_url))).setClient(OkClient()).build()
        service = adapter.create<RestService>(javaClass<RestService>())

        userSubject = BehaviorSubject.create<List<User>>()
        repoSubject = BehaviorSubject.create<List<Repo>>()
        searchSubject = BehaviorSubject.create<String>()
        errorSubject = BehaviorSubject.create<Int>()

        initSubject()
    }

    public fun getErrorSubject(): Observable<Int> {
        return errorSubject
    }

    public fun getUserSubject(): Observable<GitData> {
        return userSubject.zipWith(repoSubject, object : Func2<List<User>, List<Repo>, GitData> {
            override fun call(users: List<User>, repos: List<Repo>): GitData {
                val data = GitData()
                data.setUserList(users)
                data.setRepoList(repos)
                return data
            }
        })
    }

    private fun initSubject() {
        searchSubject.debounce(500, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).subscribe(object : Subscriber<String>() {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                handleError(e)
            }

            override fun onNext(s: String) {
                searchRepos(s)
                searchUsers(s)
            }
        })
    }

    private fun searchRepos(s: String) {
        service.getRepos(s).subscribe(object : Subscriber<GitResponse<Repo>>() {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                handleError(e)
            }

            override fun onNext(repoGitResponse: GitResponse<Repo>) {
                repoSubject.onNext(repoGitResponse.items)
            }
        })
    }

    private fun searchUsers(s: String) {
        service.getUsers(s).subscribe(object : Subscriber<GitResponse<User>>() {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                handleError(e)
            }

            override fun onNext(userGitResponse: GitResponse<User>) {
                userSubject.onNext(userGitResponse.items)
            }
        })
    }

    private fun handleError(e: Throwable) {
        if (e is RetrofitError) {
            if (e.getResponse() != null) errorSubject.onNext(e.getResponse().getStatus())
            else errorSubject.onNext(-2)
        } else {
            errorSubject.onNext(-1)
        }
    }

    public fun search(s: String) {
        searchSubject.onNext(s)
    }

    companion object {

        private var instance: GitInteractorImpl? = null

        synchronized public fun getInstance(context: Context): GitInteractorImpl {
            if (instance == null) {
                instance = GitInteractorImpl(context)
            }
            return instance
        }
    }
}
