package segithub.id.domain.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import segithub.id.data.model.GithubUser
import segithub.id.data.response.GithubUserResponse

interface GithubApi {

    @GET("search/users?q=repos:>1")
    suspend fun getListUser(
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): Response<GithubUserResponse>

    @GET("users/{username}")
    suspend fun getUserInfo(
        @Path("username") username: String
    ): Response<GithubUser>

}