package com.example.neugelb.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import com.example.neugelb.autocomplete.autocomplete.AutoCompleteBox
import com.example.neugelb.compose.component.input.TextInputField
import com.example.neugelb.compose.component.text.ContentText
import com.example.neugelb.compose.theme.NeugelbTheme
import com.example.neugelb.compose.theme.SixteenDp
import com.example.neugelb.model.MovieResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun AutoCompleteMoviesSearchBar(viewModel: MoviesViewModel, scope: CoroutineScope) {

    var value by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    val view = LocalView.current
    val foundMovies by viewModel.foundItemsLiveData.observeAsState()
    val localKeyboard = LocalSoftwareKeyboardController.current

    AutoCompleteBox(
        isSearching = isSearching,
        items = foundMovies ?: emptyList(),
        itemContent = { movie -> MovieAutoCompleteItem(movie) },
        content = {
            TextInputField(
                modifier = Modifier.fillMaxWidth(),
                text = value,
                onValueChange = {
                    value = it
                    viewModel.filter(it)
                },
                placeHolder = "search movies",
                icon = {
                    IconButton(enabled = value.isNotEmpty(),
                        onClick = {
                            value = ""
                            viewModel.filter(value)
                            view.clearFocus()
                        }) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "Clear",
                            tint = NeugelbTheme.colors.textPlaceholder
                        )
                    }
                },
                imeAction = ImeAction.Done,
                onImeAction = {
                    view.clearFocus()
                    isSearching = false
                },
                onFocusChange = {
                    isSearching = it == FocusState.Active
                    if (isSearching.not()) scope.launch { localKeyboard?.hide() }
                }
            )
        }
    ) {
        viewModel.selectMovie(it)
        value = it.title
        view.clearFocus()
    }

}

@Composable
fun MovieAutoCompleteItem(movie: MovieResult) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(SixteenDp)
    ) {
        ContentText(text = movie.title, textAlign = TextAlign.Start)
    }
}
