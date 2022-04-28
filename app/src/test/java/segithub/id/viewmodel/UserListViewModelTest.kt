package segithub.id.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import kotlinx.coroutines.coroutineScope
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import segithub.id.data.model.GithubUser
import segithub.id.domain.api.GithubApiClient
import segithub.id.domain.repository.GithubRepository
import segithub.id.presentation.datasource.UserListDataSource
import segithub.id.presentation.datasource.UserListDataSourceFactory
import segithub.id.presentation.viewmodel.UserListViewModel
import java.text.SimpleDateFormat

@RunWith(JUnit4::class)
class UserListViewModelTest {

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var githubRepository: GithubRepository

    @Mock
    lateinit var userListDataSourceFactory: UserListDataSourceFactory

    @Mock
    lateinit var githubApiClient: GithubApiClient

    @Mock
    lateinit var userPagedList: Observer<PagedList<GithubUser>>

    @Mock
    lateinit var dataSource: Observer<UserListDataSource>

    @Mock
    lateinit var dataUser: Observer<GithubUser>

    private lateinit var viewModel: UserListViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        viewModel = UserListViewModel(userListDataSourceFactory, githubApiClient)
        viewModel.usersLiveData.observeForever(userPagedList)
        viewModel.dataSource.observeForever(dataSource)
        viewModel.dataUser.observeForever(dataUser)

    }


    @Test
    suspend fun testSearchSuccess() {

        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val text = "Thu May 26 01:18:31 GMT+07:00 2011"
        val date = formatter.parse(text)
        val githuUserList = arrayListOf<GithubUser>(

            GithubUser(
                810438,
                "https://avatars.githubusercontent.com/u/810438?v=4",
                "gaearon",
                "htmlUrl=https://github.com/gaearon",
                "dan.abramov@gmail.com",
                date,
            )
        )

     }
}