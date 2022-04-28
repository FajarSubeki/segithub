package segithub.id.data.model

import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName
import java.util.*

data class GithubUser(
    @SerializedName("id") var id: Long,
    @SerializedName("avatar_url") var avatarUrl: String,
    @SerializedName("login") var login: String,
    @SerializedName("html_url") var htmlUrl: String,
    @SerializedName("email") var email: String,
    @SerializedName("created_at") var createdAt: Date,
){
    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<GithubUser>() {
            override fun areItemsTheSame(oldItem: GithubUser, newItem: GithubUser): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: GithubUser, newItem: GithubUser): Boolean {
                return oldItem.id == newItem.id && oldItem.avatarUrl == newItem.avatarUrl && oldItem.login == newItem.login
            }

        }
    }
}
