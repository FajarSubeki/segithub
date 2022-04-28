package segithub.id.domain.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import segithub.id.data.base.BaseState
import segithub.id.data.model.GithubUser
import segithub.id.data.response.GithubUserResponse
import segithub.id.domain.api.GithubApi
import segithub.id.domain.api.GithubApiClient

class GithubRepository(private val api: GithubApi): GithubApiClient {

    override suspend fun getUsersList(page: Int, pageSize: Int): BaseState<GithubUserResponse> = withContext(Dispatchers.IO){
        try {
            val response = api.getListUser(page, pageSize)
            if (response.isSuccessful){
                BaseState.success(response.body())
            }else{
                when {
                    response.code() == 401 -> {
                        BaseState.error(401, response.message())
                    }
                    response.code() == 403 -> {
                        BaseState.error(403, response.message())
                    }
                    response.code() == 404 -> {
                        BaseState.error(404, response.message())
                    }
                    else -> {
                        BaseState.error(500, response.message())
                    }
                }
            }
        }catch (ex: Throwable){
            BaseState.error(0,"${ex.message}")
        }
    }

    override suspend fun getUserInfo(username: String): BaseState<GithubUser> = withContext(Dispatchers.IO){
        try {
            val response = api.getUserInfo(username)
            if (response.isSuccessful) {
                BaseState.success(response.body())
            } else {
                when {
                    response.code() == 401 -> {
                        BaseState.error(401, response.message())
                    }
                    response.code() == 403 -> {
                        BaseState.error(403, response.message())
                    }
                    response.code() == 404 -> {
                        BaseState.error(404, response.message())
                    }
                    else -> {
                        BaseState.error(500, response.message())
                    }
                }
            }
        } catch (ex: Throwable) {
            BaseState.error(0, "${ex.message}")
        }
    }
}