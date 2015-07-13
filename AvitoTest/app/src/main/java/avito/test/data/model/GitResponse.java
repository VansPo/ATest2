package avito.test.data.model;

import java.util.List;

public class GitResponse<T> {

  public int totalCount;
  public boolean incompleteResults;
  public List<T> items;
}
