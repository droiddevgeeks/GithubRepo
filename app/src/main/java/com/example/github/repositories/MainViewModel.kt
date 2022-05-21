package com.example.github.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.github.repositories.data.ApiState
import com.example.github.repositories.data.Response
import com.example.github.repositories.usecases.SearchRepositoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val searchRepositoryUseCase: SearchRepositoryUseCase
) : ViewModel() {

    private val _repositories: MutableLiveData<ApiState<Response>> by lazy { MutableLiveData() }
    val repositories: LiveData<ApiState<Response>> by lazy { _repositories }


    fun fetchItems() {
        _repositories.value = ApiState.Loading(true)
        searchRepositoryUseCase.invoke(
            scope = viewModelScope,
            params = Unit,
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

    fun refresh() {
        _repositories.value = ApiState.Loading(true)
        searchRepositoryUseCase.invoke(
            scope = viewModelScope,
            params = Unit,
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