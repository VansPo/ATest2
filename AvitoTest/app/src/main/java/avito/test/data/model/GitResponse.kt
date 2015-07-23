package avito.test.data.model

import java.util.*

data class GitResponse<T>(val totalCount : Int, val incompleteResults : Boolean, val items : List<T>)