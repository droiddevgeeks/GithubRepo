package com.example.github.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.github.repositories.data.ApiState
import com.example.github.repositories.data.RepositoryDTO
import com.example.github.repositories.data.UserDTO
import com.example.github.repositories.usecases.GetUserRepositoryUseCase
import com.example.github.repositories.usecases.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getUserRepoUseCase: GetUserRepositoryUseCase
) : ViewModel() {


    private val _user: MutableLiveData<ApiState<UserDTO>> by lazy { MutableLiveData() }
    val user: LiveData<ApiState<UserDTO>> by lazy { _user }

    private val _repositories: MutableLiveData<ApiState<List<RepositoryDTO>>> by lazy { MutableLiveData() }
    val repositories: LiveData<ApiState<List<RepositoryDTO>>> by lazy { _repositories }

    internal fun fetchUser(username: String) {
        getUserUseCase.invoke(
            scope = viewModelScope,
            params = username,
            onSuccess = {
                _user.postValue(ApiState.Success(it))
                it.repos_url?.let { repoUrl -> fetchRepositories(repoUrl) }
            },
            onFailure = { _user.postValue(ApiState.Failure(it)) }
        )
    }

    private fun fetchRepositories(reposUrl: String) {
        _repositories.value = ApiState.Loading(true)
        getUserRepoUseCase.invoke(
            scope = viewModelScope,
            params = reposUrl,
            onSuccess = {
                _repositories.value = ApiState.Loading(false)
                _repositories.value = ApiState.Success(it)
            },
            onFailure = {
                _repositories.value = ApiState.Loading(false)
                _repositories.value = ApiState.Failure(it)
            }
        )
    }
}