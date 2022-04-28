package segithub.id.presentation.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import kotlinx.coroutines.launch
import segithub.id.data.base.BaseStatus
import segithub.id.data.model.GithubUser
import segithub.id.domain.api.GithubApiClient
import segithub.id.presentation.datasource.UserListDataSource
import segithub.id.presentation.datasource.UserListDataSourceFactory
import java.util.concurrent.Executors

class UserListViewModel(private val userListDataSourceFactory: UserListDataSourceFactory, private val githubApiClient: GithubApiClient): ViewModel() {

    var dataSource: MutableLiveData<UserListDataSource>
    lateinit var usersLiveData: LiveData<PagedList<GithubUser>>
    val isWaiting: ObservableField<Boolean> = ObservableField()
    val errorMessage: ObservableField<String> = ObservableField()
    val errorCode: ObservableField<String> = ObservableField()
    val totalCount: ObservableField<Long> = ObservableField()
    val currentPage: ObservableField<Int> = ObservableField()
    val dataUser: MutableLiveData<GithubUser> = MutableLiveData()
    lateinit var listUser: LiveData<PagedList<GithubUser>>

    init {
        currentPage.set(1)
        isWaiting.set(true)
        errorMessage.set(null)
        errorCode.set(null)
        dataSource = userListDataSourceFactory.liveData
        initUsersListFactory()
    }

    fun getInitialList(){
        viewModelScope.launch {
            val result = githubApiClient.getUsersList(1, UserListDataSource.PAGE_SIZE)
            if (result.status == BaseStatus.SUCCESS) {
                initUsersListFactory()
                errorMessage.set(null)
                errorCode.set(null)
            } else {
                errorCode.set(result.code.toString())
                errorMessage.set(result.message)
            }

            isWaiting.set(false)
        }
    }

    fun getUserInfoByUsername(username: String) {
        viewModelScope.launch {
            val result = githubApiClient.getUserInfo(username)
            if (result.status == BaseStatus.SUCCESS) {
                dataUser.value = result.data
                errorMessage.set(null)
                errorCode.set(null)
            } else {
                errorMessage.set(result.message)
                errorCode.set(result.code.toString())
            }

            isWaiting.set(false)
        }
    }

    fun initUsersListFactory() {

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(UserListDataSource.PAGE_SIZE)
            .setPageSize(UserListDataSource.PAGE_SIZE)
            .setPrefetchDistance(1)
            .build()

        val executor = Executors.newFixedThreadPool(5)

        usersLiveData = LivePagedListBuilder(userListDataSourceFactory, config)
            .setFetchExecutor(executor)
            .build()
    }

}