package com.example.neugelb.compose.templates

import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.ExperimentalAnimatedInsets
import com.google.accompanist.insets.ProvideWindowInsets

@ExperimentalMaterialApi
@OptIn(ExperimentalAnimatedInsets::class)
@Composable
fun MainLayoutWithBottomSheet(
    onBackPressedDispatcherOwner: OnBackPressedDispatcherOwner,
    bodyContentPadding: PaddingValues = PaddingValues(),
    sheetState: ModalBottomSheetState,
    sheetContent: @Composable () -> Unit,
    bodyContent: @Composable ColumnScope.() -> Unit
) {
    ProvideWindowInsets(consumeWindowInsets = false) {
        CompositionLocalProvider(LocalOnBackPressedDispatcherOwner provides onBackPressedDispatcherOwner) {
            BottomSheetWithContent(
                state = sheetState,
                content = sheetContent
            ) {
                MainContent(
                    bodyContentPadding = bodyContentPadding,
                    bodyContent = bodyContent
                )
            }
        }
    }
}

@Composable
private fun MainContent(
    contentModifier: Modifier = Modifier,
    bodyContentPadding: PaddingValues = PaddingValues(),
    bodyContent: @Composable ColumnScope.() -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .then(contentModifier)
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(bodyContentPadding)
                ) {
                    bodyContent()
                }
            }
        }
    }
}
