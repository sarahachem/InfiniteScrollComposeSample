package com.example.neugelb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.ViewModelProvider
import com.example.neugelb.compose.templates.MainLayoutWithBottomSheet
import com.example.neugelb.compose.theme.NeugelbTheme
import com.example.neugelb.ui.MovieInfoBottomSheet
import com.example.neugelb.ui.MoviesViewModel
import com.example.neugelb.ui.MoviesViewModelFactory
import com.example.neugelb.ui.Movies
import com.google.accompanist.insets.ExperimentalAnimatedInsets
import com.google.accompanist.insets.ProvideWindowInsets
import kotlinx.coroutines.Dispatchers
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
                val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
                ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                    MainLayoutWithBottomSheet(
                        onBackPressedDispatcherOwner = this@MainActivity,
                        sheetState = bottomSheetState,
                        sheetContent = {
                            MovieInfoBottomSheet(info = movieInfo)
                        }
                    ) {
                        BackHandler {
                            scope.launch {
                                when {
                                    bottomSheetState.isVisible -> scope.launch { bottomSheetState.hide() }
                                    listState.firstVisibleItemIndex > 1 -> listState.animateScrollToItem(0)
                                    else -> finish()
                                }
                            }
                        }
                        Movies(bottomSheetState, scope, listState, viewModel)
                    }
                }
            }
        }
    }

    private fun initViewModel() {
        val factory = MoviesViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory).get(MoviesViewModel::class.java)
        viewModel.errorLiveData.observe(this) {
            it?.takeIf { it.isNotEmpty() }?.let { error ->
                Toast.makeText(this@MainActivity, error, Toast.LENGTH_LONG).show()
            }
        }
    }
}