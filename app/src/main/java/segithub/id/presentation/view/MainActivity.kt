package segithub.id.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import segithub.id.R
import segithub.id.data.base.BaseState
import segithub.id.data.base.BaseStatus
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
                        if (currentPage == 1){
                            shimmerView.visibility = View.VISIBLE
                            errorLayout.visibility = View.GONE
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
}