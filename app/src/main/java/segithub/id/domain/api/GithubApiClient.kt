package segithub.id.domain.api

import segithub.id.data.base.BaseState
import segithub.id.data.model.GithubUser
import segithub.id.data.response.GithubUserResponse

interface GithubApiClient {

    suspend fun getUsersList(page: Int, pageSize: Int): BaseState<GithubUserResponse>

    suspend fun getUserInfo(username: String): BaseState<GithubUser>

}