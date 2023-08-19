package dev.baseio.videoimpl

import kotlinx.coroutines.flow.Flow

interface VideoDownloader {
    fun download(downloadRequest: DownloadRequest): Flow<DownloadPromise>
    fun cancelAllDownloads()
}

data class DownloadRequest(
    val user: String,
    val fileUrl: String,
)

sealed class VideoType {
    object FourK : VideoType()
    object P1080 : VideoType()
    object P720 : VideoType()
}

data class DownloadPromise(
    val downloadedPath: String? = null,
    val exception: Throwable? = null,
    val progress: Int? = null,
    val isDownloadComplete: Boolean = false,
    val videoType: VideoType? = null,
)