package avito.test.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import avito.test.R;
import avito.test.data.model.GitData;
import avito.test.data.model.Repo;
import avito.test.data.model.User;
import avito.test.ui.MainActivity;
import com.squareup.picasso.Picasso;

public class GitAdapter extends RecyclerView.Adapter<GitAdapter.ViewHolder> {

  private static final int TYPE_REPO = 0;
  private static final int TYPE_USER = 1;

  private MainActivity activity;
  private GitData data;

  public GitAdapter(MainActivity activity) {
    this.activity = activity;
    data = new GitData();
  }

  @Override public int getItemViewType(int position) {
    if (position % 2 == 0) {
      if (data.getRepoList().size() > position / 2) {
        return TYPE_REPO;
      }
      return TYPE_USER;
    } else {
      if (data.getUserList().size() > position / 2) {
        return TYPE_USER;
      }
      return TYPE_REPO;
    }
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    View v;
    switch (i) {
      case TYPE_REPO:
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.repo_item, viewGroup, false);
        return new RepoViewHolder(v);
      case TYPE_USER:
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_item, viewGroup, false);
        return new UserViewHolder(v);
      default:
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.repo_item, viewGroup, false);
        return new RepoViewHolder(v);
    }
  }

  @Override public void onBindViewHolder(ViewHolder viewHolder, int i) {
    int position = i/2;
    switch (viewHolder.getType()) {
      case TYPE_REPO:
        if (i/2 >= data.getUserList().size())
          position = i - data.getUserList().size();
        ((RepoViewHolder)viewHolder).setData(data.getRepoList().get(position));
        break;
      case TYPE_USER:
        if (i/2 >= data.getRepoList().size())
          position = i - data.getRepoList().size();
        ((UserViewHolder)viewHolder).setData(data.getUserList().get(position));
        break;
    }
  }

  @Override public int getItemCount() {
    return data.getRepoList().size() + data.getUserList().size();
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    public ViewHolder(View itemView) {
      super(itemView);
    }
    public int getType() {
      return TYPE_REPO;
    }
  }

  class UserViewHolder extends ViewHolder {
    private TextView name;
    private ImageView pic;

    public UserViewHolder(View itemView) {
      super(itemView);
      name = (TextView) itemView.findViewById(R.id.name);
      pic = (ImageView) itemView.findViewById(R.id.pic);
    }

    public void setData(User user) {
      name.setText(user.login);
      Picasso.with(activity).load(user.avatar_url).into(pic);
    }

    @Override public int getType() {
      return TYPE_USER;
    }
  }

  class RepoViewHolder extends ViewHolder {
    private TextView name;
    private TextView description;
    private TextView forks;

    public RepoViewHolder(View itemView) {
      super(itemView);
      name = (TextView) itemView.findViewById(R.id.name);
      description = (TextView) itemView.findViewById(R.id.description);
      forks = (TextView) itemView.findViewById(R.id.forks);
    }

    public void setData(Repo repo) {
      name.setText(repo.name);
      description.setText(repo.description);
      forks.setText(String.valueOf(repo.forks_count));
    }

    @Override public int getType() {
      return TYPE_REPO;
    }
  }

  public void replaceWith(GitData data) {
    this.data = data;
    notifyDataSetChanged();
  }

  public void clear() {
    data = new GitData();
    notifyDataSetChanged();
  }

  public GitData getData() {
    return data;
  }
}