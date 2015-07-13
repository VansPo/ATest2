package avito.test.data.model;

import java.util.ArrayList;
import java.util.List;

public class GitData {

  private List<User> userList;
  private List<Repo> repoList;

  public List<User> getUserList() {
    if (userList == null)
      userList = new ArrayList<>();
    return userList;
  }

  public void setUserList(List<User> userList) {
    this.userList = userList;
  }

  public List<Repo> getRepoList() {
    if (repoList == null)
      repoList = new ArrayList<>();
    return repoList;
  }

  public void setRepoList(List<Repo> repoList) {
    this.repoList = repoList;
  }
}
