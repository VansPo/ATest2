package avito.test.ui.presenter;

import avito.test.data.GitInteractorImpl;
import avito.test.data.model.GitData;
import avito.test.ui.MainActivity;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivityPresenter {

  private final MainActivity view;
  private final GitInteractorImpl interactor;

  public MainActivityPresenter(MainActivity view) {
    this.view = view;
    interactor = GitInteractorImpl.getInstance(view);
  }

  public void init() {
    interactor.getUserSubject()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<GitData>() {
          @Override public void onCompleted() {}
          @Override public void onError(Throwable e) {
            //TODO do smth
            view.setRefreshing(false);
          }
          @Override public void onNext(GitData gitData) {
            view.setRefreshing(false);
            view.setData(gitData);
          }
        });

    interactor.getErrorSubject()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<Integer>() {
          @Override public void onCompleted() {}
          @Override public void onError(Throwable e) {}
          @Override public void onNext(Integer i) {
            view.setRefreshing(false);
            view.showError(i);
          }
        });
  }

  public void search(String s) {
    if (s.isEmpty()) {
      view.clearList();
    } else {
      view.setRefreshing(true);
      interactor.search(s);
    }
  }
}
