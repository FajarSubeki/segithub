package segithub.id.data.response

import com.google.gson.annotations.SerializedName
import segithub.id.data.model.GithubUser

data class GithubUserResponse(
    @SerializedName("total_count") var totalCount: Long,
    @SerializedName("incomplete_results") var incompleteResults: Boolean,
    @SerializedName("items") var items: List<GithubUser>
)
