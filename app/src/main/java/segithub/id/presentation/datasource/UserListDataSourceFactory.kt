package segithub.id.presentation.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import segithub.id.data.model.GithubUser
import segithub.id.domain.api.GithubApiClient

class UserListDataSourceFactory(private val githubApiClient: GithubApiClient): DataSource.Factory<Int, GithubUser>() {

    val liveData: MutableLiveData<UserListDataSource> = MutableLiveData()

    override fun create(): DataSource<Int, GithubUser> {
        val usersListDataSource = UserListDataSource(githubApiClient)
        liveData.postValue(usersListDataSource)
        return usersListDataSource
    }


}