package segithub.id.model

import org.junit.Assert
import org.junit.Test
import segithub.id.data.model.GithubUser
import java.text.SimpleDateFormat

class GithubUserTest {

    val formatter = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
    val text = "Thu May 26 01:18:31 GMT+07:00 2011"
    val date = formatter.parse(text)!!

    val oldGithubUser =  GithubUser(
        810438,
        "https://avatars.githubusercontent.com/u/810438?v=4",
        "gaearon",
        "htmlUrl=https://github.com/gaearon",
        "dan.abramov@gmail.com",
        date,
    )

    @Test
    fun testAreItemsTheSame() {
        val newGithubUser = GithubUser(
            810438,
            "https://avatars.githubusercontent.com/u/810438?v=4",
            "gaearon",
            "htmlUrl=https://github.com/gaearon",
            "dan.abramov@gmail.com",
            date,
        )

        val isSame = GithubUser.DIFF_UTIL.areItemsTheSame(oldGithubUser, newGithubUser)
        Assert.assertEquals(isSame, true)
    }

    @Test
    fun testAreItemsDifferent() {
        val differentUser = GithubUser(
            810437,
            "https://avatars.githubusercontent.com/u/811338?v=4",
            "fajar",
            "htmlUrl=https://github.com/fajar",
            "fajar@gmail.com",
            date,
        )
        val isSame = GithubUser.DIFF_UTIL.areItemsTheSame(oldGithubUser, differentUser)
        Assert.assertEquals(isSame, false)
    }

}