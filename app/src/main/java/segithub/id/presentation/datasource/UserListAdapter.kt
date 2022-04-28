package segithub.id.presentation.datasource

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.card.MaterialCardView
import segithub.id.R
import segithub.id.data.model.GithubUser

class UserListAdapter(private val listener: UserListListener): PagedListAdapter<GithubUser, UserListAdapter.ViewHolder>(usersDiffCallback) {

    lateinit var context: Context

    interface UserListListener {
        fun onItemClick(githubUser: GithubUser)
        fun onUrlClick(githubUser: GithubUser)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userItem: MaterialCardView = itemView.findViewById(R.id.view_item)
        val imgAvatar: ImageView = itemView.findViewById(R.id.img_user)
        val txtUserName: AppCompatTextView = itemView.findViewById(R.id.text_username)
        val txtUserUrl: AppCompatTextView = itemView.findViewById(R.id.text_usr_url)
        val txtUserId: AppCompatTextView = itemView.findViewById(R.id.text_user_id)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = getItem(position)
        item?.let {
            Glide.with(context)
                .load(it.avatarUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.imgAvatar)

            holder.txtUserName.text = it.login
            holder.txtUserUrl.text = it.htmlUrl
            holder.txtUserId.text = "#" + it.id.toString()

            holder.userItem.setOnClickListener {
                listener.onItemClick(item)
            }
            holder.txtUserUrl.setOnClickListener {
                listener.onUrlClick(item)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.list_user, parent, false)
        return ViewHolder(view)
    }

    companion object {
        val usersDiffCallback = object : DiffUtil.ItemCallback<GithubUser>() {
            override fun areItemsTheSame(oldItem: GithubUser, newItem: GithubUser): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: GithubUser, newItem: GithubUser): Boolean {
                return oldItem == newItem
            }
        }
    }

}