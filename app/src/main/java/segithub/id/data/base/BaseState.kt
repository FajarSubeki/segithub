package segithub.id.data.base

data class BaseState<out T>(val status: BaseStatus, val data: T?, val code: Int, val message: String?) {

    companion object {

        fun <T> success(data: T? = null): BaseState<T> {
            return BaseState(BaseStatus.SUCCESS, data, 0, "")
        }

        fun <T> error(code: Int, msg: String): BaseState<T> {
            return BaseState(BaseStatus.ERROR, null, code, msg)
        }

    }

}
