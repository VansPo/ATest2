package avito.test.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import avito.test.R
import avito.test.data.model.GitData
import avito.test.data.model.Repo
import avito.test.data.model.User
import avito.test.ui.MainActivity
import com.squareup.picasso.Picasso

private val TYPE_REPO = 0
private val TYPE_USER = 1

public class GitAdapter(private val activity: MainActivity) : RecyclerView.Adapter<GitAdapter.ViewHolder>() {
    public var data: GitData
        private set

    init {
        $data = GitData()
    }

    override fun getItemViewType(position: Int): Int {
        if (position % 2 == 0) {
            if (data.repoList.size() > position / 2) {
                return TYPE_REPO
            }
            return TYPE_USER
        } else {
            if (data.userList.size() > position / 2) {
                return TYPE_USER
            }
            return TYPE_REPO
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v: View
        when (i) {
            TYPE_REPO -> {
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.repo_item, viewGroup, false)
                return RepoViewHolder(v)
            }
            TYPE_USER -> {
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_item, viewGroup, false)
                return UserViewHolder(v)
            }
            else -> {
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.repo_item, viewGroup, false)
                return RepoViewHolder(v)
            }
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        var position = i / 2
        when (viewHolder) {
            is RepoViewHolder -> {
                if (i / 2 >= data.userList.size()) position = i - data.userList.size()
                viewHolder.setData(activity, data.repoList.get(position))
            }
            is UserViewHolder -> {
                if (i / 2 >= data.repoList.size()) position = i - data.repoList.size()
                viewHolder.setData(activity, data.userList.get(position))
            }
        }
    }

    override fun getItemCount(): Int = data.repoList.size() + data.userList.size()

    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        public open fun getType(): Int {
            return TYPE_REPO
        }
    }

    public fun replaceWith(data: GitData) {
        this.data = data
        notifyDataSetChanged()
    }

    public fun clear() {
        data = GitData()
        notifyDataSetChanged()
    }
}

class UserViewHolder(itemView: View) : GitAdapter.ViewHolder(itemView) {
    private val name: TextView
    private val pic: ImageView

    init {
        name = itemView.findViewById(R.id.name) as TextView
        pic = itemView.findViewById(R.id.pic) as ImageView
    }

    public fun setData(context : Context, user: User) {
        name.setText(user.login)
        Picasso.with(context).load(user.avatar_url).into(pic)
    }

    override fun getType(): Int {
        return TYPE_USER
    }
}

class RepoViewHolder(itemView: View) : GitAdapter.ViewHolder(itemView) {
    private val name: TextView
    private val description: TextView
    private val forks: TextView

    init {
        name = itemView.findViewById(R.id.name) as TextView
        description = itemView.findViewById(R.id.description) as TextView
        forks = itemView.findViewById(R.id.forks) as TextView
    }

    public fun setData(context : Context, repo: Repo) {
        name.setText(repo.name)
        description.setText(repo.description)
        forks.setText("${repo.forks_count}")
    }

    override fun getType(): Int {
        return TYPE_REPO
    }
}