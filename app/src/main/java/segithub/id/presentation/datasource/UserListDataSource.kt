package segithub.id.presentation.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import segithub.id.data.base.BaseStatus
import segithub.id.data.model.GithubUser
import segithub.id.domain.api.GithubApiClient

class UserListDataSource(private val githubApiClient: GithubApiClient): PageKeyedDataSource<Int, GithubUser>() {

    private val dataSourceJob = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + dataSourceJob)
    val loadStateLiveData: MutableLiveData<String> = MutableLiveData()
    val totalCount: MutableLiveData<Long> = MutableLiveData()
    val currentPage: MutableLiveData<Int> = MutableLiveData()

    companion object{
        const val PAGE_SIZE = 10
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, GithubUser>) {
        scope.launch {
            currentPage.postValue(params.key + 1)
            val response = githubApiClient.getUsersList(params.key, PAGE_SIZE)
            response.data?.let {
                callback.onResult(it.items, params.key + 1)
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, GithubUser>) {
        TODO("Not yet implemented")
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, GithubUser>
    ) {
        scope.launch {
            currentPage.postValue(1)
            loadStateLiveData.postValue("loading")
            totalCount.postValue(0)

            val response = githubApiClient.getUsersList(1, PAGE_SIZE)
            when(response.status) {
                BaseStatus.ERROR -> loadStateLiveData.postValue(response.code.toString())
                else -> {
                    response.data?.let {
                        callback.onResult(it.items, null, 2)
                        loadStateLiveData.postValue("success")
                        totalCount.postValue(it.totalCount)
                    }
                }
            }
        }
    }
}