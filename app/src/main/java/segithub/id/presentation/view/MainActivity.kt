package segithub.id.presentation.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Transformations
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import segithub.id.R
import segithub.id.data.model.GithubUser
import segithub.id.databinding.ActivityMainBinding
import segithub.id.presentation.datasource.UserListAdapter
import segithub.id.presentation.viewmodel.UserListViewModel


class MainActivity : AppCompatActivity(), UserListAdapter.UserListListener {

    private val usersListViewModel: UserListViewModel by viewModel()
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeList: SwipeRefreshLayout
    private lateinit var errorLayout: LinearLayout
    private lateinit var shimmerView: ShimmerFrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_main
        )

        binding.usersListViewModel = usersListViewModel

        recyclerView = binding.root.findViewById(R.id.recycler_userlist)
        swipeList = binding.root.findViewById(R.id.swipe)
        shimmerView = binding.root.findViewById(R.id.shimmer_view)
        errorLayout = binding.root.findViewById(R.id.error_layout)
        initAdapterAndObserve()
    }

    private fun initAdapterAndObserve() {

        val userListAdapter = UserListAdapter(this)

        swipeList.setOnRefreshListener {
            usersListViewModel.getInitialList()
            swipeList.isRefreshing = false
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = userListAdapter

        Transformations.switchMap(usersListViewModel.dataSource) { dataSource -> dataSource.loadStateLiveData }
            .observe(this) {
                when {
                    it.equals("loading") -> {
                        usersListViewModel.isWaiting.set(true)
                        usersListViewModel.errorMessage.set(null)
                        swipeList.isRefreshing = false
                    }
                    it.equals("success") -> {
                        usersListViewModel.isWaiting.set(false)
                        usersListViewModel.errorMessage.set(null)
                    }
                    else -> {
                        val currentPage = usersListViewModel.currentPage.get()
                        usersListViewModel.isWaiting.set(false)
                        usersListViewModel.errorMessage.set(getString(R.string.msg_fetch_users_list_has_error))
                        if (currentPage == 1){
                            shimmerView.visibility = View.GONE
                            errorLayout.visibility = View.VISIBLE
                        }else{
                            errorLayout.visibility = View.VISIBLE
                            shimmerView.visibility = View.GONE
                        }
                        usersListViewModel.errorCode.set(it)
                    }
                }
            }


        Transformations.switchMap(usersListViewModel.dataSource) { dataSource -> dataSource.totalCount }
            .observe(this) { totalCount ->
                totalCount?.let { usersListViewModel.totalCount.set(it) }
            }

        Transformations.switchMap(usersListViewModel.dataSource) { dataSource -> dataSource.currentPage }
            .observe(this) { currentPage ->
                currentPage?.let { usersListViewModel.currentPage.set(it) }
            }

        usersListViewModel.usersLiveData.observe(this) {
            userListAdapter.submitList(it)
        }

        usersListViewModel.dataUser.observe(this) {
            Toast.makeText(
                this,
                it.login + " - " + it.email + " - " + it.createdAt,
                Toast.LENGTH_SHORT
            ).show()
        }


    }

    override fun onItemClick(githubUser: GithubUser) {
        usersListViewModel.getUserInfoByUsername(githubUser.login)
    }

    override fun onUrlClick(githubUser: GithubUser) {
        val url = githubUser.htmlUrl
        if (!TextUtils.isEmpty(url)){
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }else{
            Toast.makeText(this, "Url repository not found...", Toast.LENGTH_SHORT).show()
        }

    }
}