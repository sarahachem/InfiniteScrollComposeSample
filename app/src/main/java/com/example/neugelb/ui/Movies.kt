package com.example.neugelb.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.neugelb.R
import com.example.neugelb.compose.component.text.BodyText
import com.example.neugelb.compose.component.MovieCard
import com.example.neugelb.compose.theme.NeugelbTheme
import com.example.neugelb.compose.theme.TwelveDp
import com.example.neugelb.compose.theme.TwentyFourDp
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.toPaddingValues
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
    val movies by viewModel.moviesLiveData.observeAsState()
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(false),
            onRefresh = { viewModel.refresh() },
        ) {
            LazyColumn(
                state = state,
                modifier = Modifier.fillMaxSize(),
                contentPadding = LocalWindowInsets.current.navigationBars.toPaddingValues()
            ) {
                val groupedMovies = movies?.groupBy { it.releaseDate }
                groupedMovies?.forEach { (date, movie) ->
                    stickyHeader {
                        BodyText(
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(NeugelbTheme.colors.divider)
                                .padding(TwelveDp),
                            text = stringResource(R.string.release_date) + " $date"
                        )
                    }
                    itemsIndexed(movie) { index, item ->
                        MovieCard(
                            modifier = Modifier.padding(horizontal = TwentyFourDp),
                            title = item.title,
                            url = item.posterPath,
                            onMovieClicked = {
                                scope.launch {
                                    if (isLoadingMovieInfo?.not() == true) {
                                        viewModel.onMovieClicked(item)
                                        bottomSheetState.show()

                                        movies?.takeIf { it.indexOf(item) < it.lastIndex }?.let {
                                            state.animateScrollToItem(
                                                //take sticky headers into account
                                                it.indexOf(item) + groupedMovies.keys.indexOf(date) + 1
                                            )
                                        }
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
        }
        if (isLoadingMovieInfo == true || isLoadingMovies == true)
            CircularProgressIndicator(color = NeugelbTheme.colors.mainColor)
    }
}