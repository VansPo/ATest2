package avito.test.ui.adapter

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

public class GitAdapter(private val activity: MainActivity) : RecyclerView.Adapter<GitAdapter.ViewHolder>() {
    public var data: GitData? = null
        private set

    init {
        data = GitData()
    }

    override fun getItemViewType(position: Int): Int {
        if (position % 2 == 0) {
            if (data!!.getRepoList().size() > position / 2) {
                return TYPE_REPO
            }
            return TYPE_USER
        } else {
            if (data!!.getUserList().size() > position / 2) {
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
        when (viewHolder.getType()) {
            TYPE_REPO -> {
                if (i / 2 >= data!!.getUserList().size()) position = i - data!!.getUserList().size()
                (viewHolder as RepoViewHolder).setData(data!!.getRepoList().get(position))
            }
            TYPE_USER -> {
                if (i / 2 >= data!!.getRepoList().size()) position = i - data!!.getRepoList().size()
                (viewHolder as UserViewHolder).setData(data!!.getUserList().get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return data!!.getRepoList().size() + data!!.getUserList().size()
    }

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        public open fun getType(): Int {
            return TYPE_REPO
        }
    }

    inner class UserViewHolder(itemView: View) : ViewHolder(itemView) {
        private val name: TextView
        private val pic: ImageView

        init {
            name = itemView.findViewById(R.id.name) as TextView
            pic = itemView.findViewById(R.id.pic) as ImageView
        }

        public fun setData(user: User) {
            name.setText(user.login)
            Picasso.with(activity).load(user.avatar_url).into(pic)
        }

        override fun getType(): Int {
            return TYPE_USER
        }
    }

    inner class RepoViewHolder(itemView: View) : ViewHolder(itemView) {
        private val name: TextView
        private val description: TextView
        private val forks: TextView

        init {
            name = itemView.findViewById(R.id.name) as TextView
            description = itemView.findViewById(R.id.description) as TextView
            forks = itemView.findViewById(R.id.forks) as TextView
        }

        public fun setData(repo: Repo) {
            name.setText(repo.name)
            description.setText(repo.description)
            forks.setText(String.valueOf(repo.forks_count))
        }

        override fun getType(): Int {
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

    companion object {

        private val TYPE_REPO = 0
        private val TYPE_USER = 1
    }
}