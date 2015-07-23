package avito.test.ui.presenter

import avito.test.data.GitInteractorImpl
import avito.test.data.model.GitData
import avito.test.ui.MainActivity
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

public class MainActivityPresenter(private val view: MainActivity) {
    private val interactor: GitInteractorImpl?

    init {
        interactor = GitInteractorImpl.getInstance(view)
    }

    public fun init() {
        interactor!!.getUserSubject()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({gitData ->
                    view.setRefreshing(false)
                    view.setData(gitData)
                }, {e -> view.setRefreshing(false)})

        interactor!!.getErrorSubject()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({error ->
                    view.setRefreshing(false)
                    view.showError(error)
                }, {e -> view.setRefreshing(false)})

    }

    public fun search(s: String) {
        if (s.isEmpty()) {
            view.clearList()
        } else {
            view.setRefreshing(true)
            interactor!!.search(s)
        }
    }
}
