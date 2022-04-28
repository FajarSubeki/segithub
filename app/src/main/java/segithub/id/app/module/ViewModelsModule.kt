package segithub.id.app.module

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import segithub.id.presentation.viewmodel.UserListViewModel

val usersListViewModel = module {
    viewModel { UserListViewModel(get(), get()) }
}