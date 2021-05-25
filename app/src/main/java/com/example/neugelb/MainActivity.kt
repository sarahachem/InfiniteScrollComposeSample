package com.example.neugelb

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.neugelb.apis.ApiBuilders
import com.example.neugelb.compose.component.VideoPlayer
import com.example.neugelb.compose.templates.MainLayoutWithBottomSheet
import com.example.neugelb.compose.theme.NeugelbTheme
import com.example.neugelb.ui.MovieInfoBottomSheet
import com.example.neugelb.ui.MoviesViewModel
import com.example.neugelb.ui.MoviesViewModelFactory
import com.example.neugelb.ui.Movies
import com.google.accompanist.insets.ExperimentalAnimatedInsets
import com.google.accompanist.insets.ProvideWindowInsets
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MoviesViewModel

    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    @ExperimentalAnimatedInsets
    @ExperimentalComposeUiApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        setContent {
            NeugelbTheme {
                val scope = rememberCoroutineScope()
                val listState = rememberLazyListState()
                val movieInfo by viewModel.movieCreditsAndInfoLiveData.observeAsState()
                val playTrailer by viewModel.playTrailerLiveData.observeAsState()
                val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
                ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                    MainLayoutWithBottomSheet(
                        onBackPressedDispatcherOwner = this@MainActivity,
                        sheetState = bottomSheetState,
                        sheetContent = {
                            MovieInfoBottomSheet(info = movieInfo, viewModel)
                        }
                    ) {
                        BackHandler {
                            scope.launch {
                                when {
                                    playTrailer != null -> removeTrailer()
                                    bottomSheetState.isVisible -> scope.launch { bottomSheetState.hide() }
                                    listState.firstVisibleItemIndex > 1 -> listState.animateScrollToItem(0)
                                    else -> finish()
                                }
                            }
                        }
                        Movies(bottomSheetState, scope, listState, viewModel)
                        playTrailer?.let {
                            Popup(
                                alignment = Alignment.Center,
                                onDismissRequest = { removeTrailer() },
                                properties = PopupProperties(
                                    dismissOnBackPress = true,
                                    dismissOnClickOutside = true
                                )
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1f)
                                        .background(
                                            color = NeugelbTheme.colors.playerBackground,
                                            shape = RoundedCornerShape(16.dp)
                                        ), contentAlignment = Alignment.Center
                                ) {
                                    //TODO: add loading when preparing video and fullscreen option.
                                    VideoPlayer(uri = it, viewModel)
                                    IconButton(
                                        modifier = Modifier.align(Alignment.TopEnd),
                                        onClick = {
                                            removeTrailer()
                                        }) {
                                        Icon(
                                            imageVector = Icons.Filled.Clear,
                                            contentDescription = "Clear",
                                            tint = NeugelbTheme.colors.textPlaceholder
                                        )

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewModel.fetchMovies()
        }
    }

    private fun initViewModel() {
        val factory = MoviesViewModelFactory(
            application, ApiBuilders.getTMDBApi(),
            (this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
        )
        viewModel = ViewModelProvider(this, factory).get(MoviesViewModel::class.java)
        viewModel.errorLiveData.observe(this) {
            it?.takeIf { it.isNotEmpty() }?.let { error ->
                Toast.makeText(this@MainActivity, error, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun removeTrailer() {
        viewModel.playTrailerLiveData.postValue(null)
    }
}