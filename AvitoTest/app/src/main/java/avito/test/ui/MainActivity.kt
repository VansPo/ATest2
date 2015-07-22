package avito.test.ui

import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import avito.test.R
import avito.test.data.model.GitData
import avito.test.ui.adapter.GitAdapter
import avito.test.ui.presenter.MainActivityPresenter
import avito.test.util.ConstUtil

public class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private var presenter: MainActivityPresenter? = null

    private var messageText: TextView? = null
    private var pb: ProgressBar? = null
    private var adapter: GitAdapter? = null
    private var list: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        messageText = findViewById(R.id.message) as TextView
        pb = findViewById(R.id.progressbar) as ProgressBar
        list = findViewById(R.id.list) as RecyclerView

        initViews()
        initListeners()

        presenter = MainActivityPresenter(this)
        presenter!!.init()
    }

    private fun initViews() {
        adapter = GitAdapter(this)
        list!!.setLayoutManager(LinearLayoutManager(this))
        list!!.addItemDecoration(SimpleDividerItemDecoration(getResources()))
        list!!.setAdapter(adapter)
    }

    private fun initListeners() {
    }

    public fun setData(data: GitData) {
        if (data.getUserList().size() > 0 && data.getRepoList().size() > 0) adapter!!.replaceWith(data)
        else showError(ConstUtil.NO_DATA)
    }

    public fun clearList() {
        adapter!!.clear()
        showError(ConstUtil.NO_SEARCH_SEQUENCE)
    }

    public fun setRefreshing(isResreshing: Boolean) {
        if (isResreshing) {
            list!!.setVisibility(View.GONE)
            messageText!!.setVisibility(View.GONE)
            pb!!.setVisibility(View.VISIBLE)
        } else {
            list!!.setVisibility(View.VISIBLE)
            messageText!!.setVisibility(View.GONE)
            pb!!.setVisibility(View.GONE)
        }
    }

    public fun showError(e: Int) {
        list!!.setVisibility(View.GONE)
        messageText!!.setVisibility(View.VISIBLE)
        pb!!.setVisibility(View.GONE)
        when (e) {
            ConstUtil.FORBIDDEN -> messageText!!.setText(R.string.api_rate_limit)
            ConstUtil.RETROFIT_ERROR -> messageText!!.setText(R.string.failed_to_connect)
            ConstUtil.UNKNOWN_ERROR -> messageText!!.setText(R.string.error)
            ConstUtil.NO_DATA -> messageText!!.setText(R.string.nothing_found)
            ConstUtil.NO_SEARCH_SEQUENCE -> messageText!!.setText(R.string.enter_search)
            else -> messageText!!.setText(getResources().getString(R.string.error) + " " + e)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = getMenuInflater()
        inflater.inflate(R.menu.menu_main, menu)

        val searchItem = menu!!.findItem(R.id.search)
        val searchView = MenuItemCompat.getActionView(searchItem) as SearchView
        searchView.setQueryHint(getString(R.string.search))
        searchView.setOnQueryTextListener(this)

        return true
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        presenter!!.search(query)
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        presenter!!.search(newText)
        return true
    }
}
