package com.example.neugelb.compose.component

import android.content.Context
import android.graphics.Color
import android.util.SparseArray
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidViewBinding
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.example.neugelb.databinding.ActivityPlayerBinding
import com.example.neugelb.ui.MoviesViewModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource

@Composable
fun VideoPlayer(uri: String, viewModel: MoviesViewModel) {
    val context = LocalContext.current
    val playTrailer by viewModel.playTrailerLiveData.observeAsState()
    val exoPlayer = remember {
        SimpleExoPlayer.Builder(context).build()
    }

    AndroidViewBinding(
        ActivityPlayerBinding::inflate, modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        videoView.setBackgroundColor(Color.TRANSPARENT)
        videoView.player = exoPlayer
        exoPlayer.playWhenReady = false
        playYoutubeVideo(context, uri, exoPlayer)
    }
    if (playTrailer == null) {
        exoPlayer.release()
        exoPlayer.stop()
    }
}

fun playYoutubeVideo(context: Context, uri: String, player: SimpleExoPlayer) {
    YoutubeVideoExtractor(context, player).extract(uri, true, true)
}

class YoutubeVideoExtractor(context: Context, val player: SimpleExoPlayer) :
    YouTubeExtractor(context) {
    override fun onExtractionComplete(ytFiles: SparseArray<YtFile>?, videoMeta: VideoMeta?) {
        ytFiles?.let {
            val videoTag = 137
            val audioTag = 140
            val audioSource: MediaSource =
                ProgressiveMediaSource.Factory(DefaultHttpDataSource.Factory())
                    .createMediaSource(MediaItem.fromUri(ytFiles.get(audioTag).url))
            val videoSource: MediaSource =
                ProgressiveMediaSource.Factory(DefaultHttpDataSource.Factory())
                    .createMediaSource(MediaItem.fromUri(ytFiles.get(videoTag).url))
            player.setMediaSource(MergingMediaSource(true, videoSource, audioSource), true)
        }
    }
}




