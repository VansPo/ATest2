package avito.test.ui.presenter

import avito.test.data.GitInteractorImpl
import avito.test.data.model.GitData
import avito.test.ui.MainActivity
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

public class MainActivityPresenter(private val view: MainActivity) {
    private val interactor: GitInteractorImpl

    init {
        interactor = GitInteractorImpl.getInstance(view)
    }

    public fun init() {
        interactor.getUserSubject().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(object : Subscriber<GitData>() {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                //TODO do smth
                view.setRefreshing(false)
            }

            override fun onNext(gitData: GitData) {
                view.setRefreshing(false)
                view.setData(gitData)
            }
        })

        interactor.getErrorSubject().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(object : Subscriber<Int>() {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
            }

            override fun onNext(i: Int?) {
                view.setRefreshing(false)
                view.showError(i!!)
            }
        })

    }

    public fun search(s: String) {
        if (s.isEmpty()) {
            view.clearList()
        } else {
            view.setRefreshing(true)
            interactor.search(s)
        }
    }
}
