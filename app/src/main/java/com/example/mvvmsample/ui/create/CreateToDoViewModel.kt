package com.example.mvvmsample.ui.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmsample.repository.todo.ToDoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateToDoViewModel @Inject constructor(
    // コンストラクタで受け取る
    private val repo: ToDoRepository
) : ViewModel() {
    // MutableStateFlowで用意
    val errorMessage = MutableStateFlow("")
    val done = MutableStateFlow(false)

    fun save(title: String, detail: String) {
        if (title.trim().isEmpty()) {
            errorMessage.value = "Input title"
            return
        }
        viewModelScope.launch {
            try {
                repo.create(title, detail)
                done.value = true
            } catch (e: Exception) {
                errorMessage.value = e.message ?: ""
            }
        }
    }
}