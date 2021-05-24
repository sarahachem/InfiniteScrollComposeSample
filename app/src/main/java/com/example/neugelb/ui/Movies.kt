package com.example.neugelb.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.neugelb.R
import com.example.neugelb.compose.component.ClickableCircleProgress
import com.example.neugelb.compose.component.LabelIconCell
import com.example.neugelb.compose.component.MovieCard
import com.example.neugelb.compose.theme.EightDp
import com.example.neugelb.compose.theme.FiftySixDp
import com.example.neugelb.compose.theme.NeugelbTheme
import com.example.neugelb.compose.theme.TwelveDp
import com.example.neugelb.compose.theme.TwentyFourDp
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.toPaddingValues
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun Movies(
    bottomSheetState: ModalBottomSheetState,
    scope: CoroutineScope,
    state: LazyListState,
    viewModel: MoviesViewModel
) {
    val isLoadingMovieInfo by viewModel.isLoadingMovieInfoLiveData.observeAsState()
    val isLoadingMovies by viewModel.isLoadingMoviesLiveData.observeAsState()
    val shouldScrollUp by viewModel.shouldScrollUpLiveData.observeAsState()
    val foundMovies by viewModel.foundItemsLiveData.observeAsState()
    val movies by viewModel.moviesLiveData.observeAsState()
    val actualMovies = foundMovies ?: movies
    val localKeyboard = LocalSoftwareKeyboardController.current

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(false),
            onRefresh = { viewModel.refresh() },
        ) {
            LazyColumn(
                state = state,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = FiftySixDp),
                contentPadding = LocalWindowInsets.current.navigationBars.toPaddingValues()
            ) {
                val groupedMovies = actualMovies?.groupBy { it.releaseDate }
                groupedMovies?.forEach { (date, movie) ->
                    stickyHeader {
                        StickyHeaderRow(date)
                    }
                    items(movie) { item ->
                        MovieCard(
                            modifier = Modifier.padding(horizontal = TwentyFourDp),
                            title = item.title,
                            url = item.posterPath ?: item.backdropPath,
                            enabled = isLoadingMovies == false && state.isScrollInProgress.not(),
                            onMovieClicked = {
                                scope.launch {
                                    if (isLoadingMovieInfo?.not() == true) {
                                        localKeyboard?.hide()
                                        viewModel.isLoadingInfo(true)
                                        actualMovies.takeIf { it.indexOf(item) < it.lastIndex }
                                            ?.let {
                                                state.animateScrollToItem(
                                                    it.indexOf(item) + groupedMovies.keys.indexOf(
                                                        date
                                                    ) + 1
                                                )
                                            }
                                        viewModel.onMovieClicked(item)
                                        bottomSheetState.show()
                                    }
                                }
                            }
                        )
                        //to add the days grouped in previews map entries
                        if (movies?.lastIndex == movies?.indexOf(item)) {
                            viewModel.fetchMovies()
                        }
                    }
                }
            }
            movies?.takeIf { it.isNotEmpty() }?.let {
                AutoCompleteMoviesSearchBar(viewModel, scope)
            }
        }
        if (isLoadingMovieInfo == true || isLoadingMovies == true) {
            ClickableCircleProgress(
                Modifier.matchParentSize(),
                isLoadingMovieInfo == false && isLoadingMovies == false
            )
        }
        if (shouldScrollUp == true && actualMovies?.isNotEmpty() == true && state.isScrollInProgress.not()) {
            scope.launch {
                state.animateScrollToItem(0)
            }
            viewModel.shouldScrollUpLiveData.postValue(false)
        }
    }
}

@Composable
fun StickyHeaderRow(date: String) {
    Row {
        LabelIconCell(
            icon = {
                Icon(
                    modifier = Modifier.fillMaxSize(0.3f),
                    painter = painterResource(id = R.drawable.ic_tmdb),
                    contentDescription = null,
                    tint = NeugelbTheme.colors.iconMain
                )
            },
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(NeugelbTheme.colors.divider)
                .padding(horizontal = TwelveDp, vertical = EightDp),
            text = stringResource(R.string.release_date) + " $date"
        )
    }
}
