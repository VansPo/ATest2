package avito.test.ui;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import avito.test.R;
import avito.test.data.model.GitData;
import avito.test.ui.adapter.GitAdapter;
import avito.test.ui.presenter.MainActivityPresenter;
import avito.test.util.ConstUtil;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

  private MainActivityPresenter presenter;

  private TextView messageText;
  private ProgressBar pb;
  private GitAdapter adapter;
  private RecyclerView list;


  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    messageText = (TextView) findViewById(R.id.message);
    pb = (ProgressBar) findViewById(R.id.progressbar);
    list = (RecyclerView) findViewById(R.id.list);

    initViews();
    initListeners();

    presenter = new MainActivityPresenter(this);
    presenter.init();
  }

  private void initViews() {
    adapter = new GitAdapter(this);
    list.setLayoutManager(new LinearLayoutManager(this));
    list.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
    list.setAdapter(adapter);
  }

  private void initListeners() {
  }

  public void setData(GitData data) {
    if (data.getUserList().size() > 0 && data.getRepoList().size() > 0)
      adapter.replaceWith(data);
    else
      showError(ConstUtil.NO_DATA);
  }

  public void clearList() {
    adapter.clear();
    showError(ConstUtil.NO_SEARCH_SEQUENCE);
  }

  public void setRefreshing(boolean isResreshing) {
      if (isResreshing) {
        list.setVisibility(View.GONE);
        messageText.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);
      } else {
        list.setVisibility(View.VISIBLE);
        messageText.setVisibility(View.GONE);
        pb.setVisibility(View.GONE);
      }
  }

  public void showError(int e) {
    list.setVisibility(View.GONE);
    messageText.setVisibility(View.VISIBLE);
    pb.setVisibility(View.GONE);
    switch (e) {
      case ConstUtil.FORBIDDEN:
        messageText.setText(R.string.api_rate_limit);
        break;
      case ConstUtil.RETROFIT_ERROR:
        messageText.setText(R.string.failed_to_connect);
        break;
      case ConstUtil.UNKNOWN_ERROR:
        messageText.setText(R.string.error);
        break;
      case ConstUtil.NO_DATA:
        messageText.setText(R.string.nothing_found);
        break;
      case ConstUtil.NO_SEARCH_SEQUENCE:
        messageText.setText(R.string.enter_search);
        break;
      default:
        messageText.setText(getResources().getString(R.string.error) + " " + e);
        break;
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_main, menu);

    MenuItem searchItem = menu.findItem(R.id.search);
    SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
    searchView.setQueryHint(getString(R.string.search));
    searchView.setOnQueryTextListener(this);

    return true;
  }

  @Override public boolean onQueryTextSubmit(String query) {
    presenter.search(query);
    return true;
  }

  @Override public boolean onQueryTextChange(String newText) {
    presenter.search(newText);
    return true;
  }
}
