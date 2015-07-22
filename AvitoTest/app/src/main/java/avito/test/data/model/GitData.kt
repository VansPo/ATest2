package avito.test.data.model

import java.util.ArrayList

public class GitData {

    private var userList: List<User>? = null
    private var repoList: List<Repo>? = null

    public fun getUserList(): List<User> {
        if (userList == null) userList = ArrayList<User>()
        return userList
    }

    public fun setUserList(userList: List<User>) {
        this.userList = userList
    }

    public fun getRepoList(): List<Repo> {
        if (repoList == null) repoList = ArrayList<Repo>()
        return repoList
    }

    public fun setRepoList(repoList: List<Repo>) {
        this.repoList = repoList
    }
}
