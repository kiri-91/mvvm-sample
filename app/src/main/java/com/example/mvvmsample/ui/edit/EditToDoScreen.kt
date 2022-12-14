package com.example.mvvmsample.ui.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mvvmsample.model.todo.ToDo

@Composable
fun EditToDoScreen(
    navController: NavController,
    viewModel: EditToDoViewModel,
) {
    val scaffoldState = rememberScaffoldState()
    val todo = viewModel.todo.collectAsState(initial = emptyToDo)
    // 読込中を表示
    if (todo.value._id == emptyToDoId) {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                EditTopBar(navController, null)
            },
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val title = rememberSaveable { mutableStateOf(todo.value.title) }
    val detail = rememberSaveable { mutableStateOf(todo.value.detail) }

    val errorMessage = viewModel.errorMessage.collectAsState()
    val done = viewModel.done.collectAsState()

    if (errorMessage.value.isNotEmpty()) {
        LaunchedEffect(scaffoldState.snackbarHostState) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = errorMessage.value
            )
            viewModel.errorMessage.value = ""
        }
    }

    if (done.value) {
        // 再コンポーズ時にもう一度実行されたら困る
        viewModel.done.value = false
        navController.popBackStack()
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            EditTopBar(navController) {
                viewModel.save(todo.value, title.value, detail.value)
            }
        },
    ) {
        EditToDoBody(title = title, detail = detail)
    }
}

@Composable
fun EditTopBar(navController: NavController, save: (() -> Unit)?) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(Icons.Filled.ArrowBack, "Back")
            }
        },
        title = {
            Text(text = "Edit TO DO")
        },
        actions = {
            if (save != null) {
                IconButton(onClick = save) {
                    Icon(Icons.Filled.Done, "Save")
                }
            }
        }
    )
}

@Composable
fun EditToDoBody(
    title: MutableState<String>,
    detail: MutableState<String>
) {
    Column {
        TextField(
            value = title.value,
            onValueChange = { title.value = it },
            label = { Text(text = "Title") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
        TextField(
            value = detail.value,
            onValueChange = { detail.value = it },
            label = { Text(text = "Detail") },
            modifier = Modifier
                .weight(1.0f, true)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}


private const val emptyToDoId = -1
private val emptyToDo = ToDo(
    _id = emptyToDoId,
    title = "",
    detail = "",
    created = 0,
    modified = 0
)