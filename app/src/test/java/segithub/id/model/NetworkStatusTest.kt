package segithub.id.model

import org.junit.Assert
import org.junit.Test

class NetworkStatusTest {

    enum class BaseStatus{
        SUCCESS,
        ERROR,
        LOADING,
    }

    @Test
    fun testNetworkStateSuccess() {
        val networkStatus = BaseStatus.SUCCESS
        Assert.assertEquals(BaseStatus.SUCCESS, networkStatus)
    }

    @Test
    fun testNetworkStateError() {
        val networkStatus = BaseStatus.ERROR
        Assert.assertEquals(BaseStatus.ERROR, networkStatus)
    }

    @Test
    fun testNetworkStateLoading() {
        val networkStatus = BaseStatus.LOADING
        Assert.assertEquals(BaseStatus.LOADING, networkStatus)
    }

}