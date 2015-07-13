package avito.test.data;

import android.content.Context;
import avito.test.BuildConfig;
import avito.test.R;
import avito.test.data.model.GitData;
import avito.test.data.model.GitResponse;
import avito.test.data.model.Repo;
import avito.test.data.model.User;
import avito.test.data.rest.RestService;
import java.util.List;
import java.util.concurrent.TimeUnit;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class GitInteractorImpl {

  private static GitInteractorImpl instance;
  private Context context;
  private RestService service;

  private BehaviorSubject<List<User>> userSubject;
  private BehaviorSubject<List<Repo>> repoSubject;
  private BehaviorSubject<String> searchSubject;
  private BehaviorSubject<Integer> errorSubject;

  public static synchronized GitInteractorImpl getInstance(Context context) {
    if (instance == null) {
      instance = new GitInteractorImpl(context);
    }
    return instance;
  }

  public GitInteractorImpl(Context context) {
    this.context = context;
    RestAdapter adapter = new RestAdapter.Builder()
        .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
        .setEndpoint(new MyEndPoint(context.getResources().getString(R.string.git_url)))
        .setClient(new OkClient())
        .build();
    service = adapter.create(RestService.class);

    userSubject = BehaviorSubject.create();
    repoSubject = BehaviorSubject.create();
    searchSubject = BehaviorSubject.create();
    errorSubject = BehaviorSubject.create();

    initSubject();
  }

  public Observable<Integer> getErrorSubject() {
    return errorSubject;
  }

  public Observable<GitData> getUserSubject() {
    return userSubject
        .zipWith(repoSubject, new Func2<List<User>, List<Repo>, GitData>() {
          @Override public GitData call(List<User> users, List<Repo> repos) {
            GitData data = new GitData();
            data.setUserList(users);
            data.setRepoList(repos);
            return data;
          }
        });
  }

  private void initSubject() {
    searchSubject
        .debounce(500, TimeUnit.MILLISECONDS)
        .subscribeOn(Schedulers.io())
        .subscribe(new Subscriber<String>() {
          @Override public void onCompleted() {}
          @Override public void onError(Throwable e) {handleError(e);}
          @Override public void onNext(String s) {
            searchRepos(s);
            searchUsers(s);
          }
        });
  }

  private void searchRepos(String s) {
    service.getRepos(s)
        .subscribe(new Subscriber<GitResponse<Repo>>() {
          @Override public void onCompleted() {}
          @Override public void onError(Throwable e) {handleError(e);}
          @Override public void onNext(GitResponse<Repo> repoGitResponse) {
            repoSubject.onNext(repoGitResponse.items);
          }
        });
  }

  private void searchUsers(String s) {
    service.getUsers(s)
        .subscribe(new Subscriber<GitResponse<User>>() {
          @Override public void onCompleted() {}
          @Override public void onError(Throwable e) {handleError(e);}
          @Override public void onNext(GitResponse<User> userGitResponse) {
            userSubject.onNext(userGitResponse.items);
          }
        });
  }

  private void handleError(Throwable e) {
    if (e instanceof RetrofitError) {
      if (((RetrofitError) e).getResponse() != null)
        errorSubject.onNext(((RetrofitError) e).getResponse().getStatus());
      else
        errorSubject.onNext(-2);
    }
    else {
      errorSubject.onNext(-1);
    }
  }

  public void search(String s) {
    searchSubject.onNext(s);
  }
}
