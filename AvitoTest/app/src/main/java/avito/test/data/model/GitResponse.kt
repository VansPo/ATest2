package avito.test.data.model

public class GitResponse<T> {

    public var totalCount: Int = 0
    public var incompleteResults: Boolean = false
    public var items: List<T>
}
