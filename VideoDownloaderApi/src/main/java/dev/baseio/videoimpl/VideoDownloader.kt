package dev.baseio.videoimpl

interface VideoDownloader {
    fun download(downloadRequest: DownloadRequest, receive: (DownloadPromise) -> Unit)

    fun cancelAllDownloads()
    fun isDownloading(request: DownloadRequest): Boolean
    fun cancel(request: DownloadRequest)
}

data class DownloadRequest(
    val user: String,
    val fileUrl: String,
)

class UnknownVideoException : Throwable()

sealed class VideoType {
    object FourK : VideoType()
    object P1080 : VideoType()
    object P720 : VideoType()
    object Unknown : VideoType()
}

data class DownloadPromise(
    val downloadedPath: String? = null,
    val exception: Throwable? = null,
    val progress: Int? = null,
    val isDownloadComplete: Boolean = false,
    val videoType: VideoType? = null,
)