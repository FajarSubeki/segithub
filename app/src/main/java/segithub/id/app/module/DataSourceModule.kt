package segithub.id.app.module

import org.koin.dsl.module
import segithub.id.presentation.datasource.UserListDataSourceFactory

val usersListDataSourceFactory = module {
    single { UserListDataSourceFactory(get()) }
}