@file:OptIn(ExperimentalCoroutinesApi::class)

package dev.baseio.videoimpl

import dev.baseio.videoimpl.VideoType.FourK
import dev.baseio.videoimpl.VideoType.P1080
import dev.baseio.videoimpl.VideoType.P720
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest

interface FileDownloader {
    fun downloadFile(
        coroutineScope: CoroutineScope,
        downloadRequest: DownloadRequest,
        receive: (DownloadPromise) -> Unit
    )
}

internal class SmartFileDownloader : FileDownloader {
    override fun downloadFile(
        coroutineScope: CoroutineScope,
        downloadRequest: DownloadRequest,
        receive: (DownloadPromise) -> Unit
    ) {
        downloadAndTranscode(downloadRequest).mapLatest {
            receive(it)
        }.catch {
            receive(DownloadPromise(exception = it))
        }.launchIn(coroutineScope)
    }

    private fun downloadAndTranscode(downloadRequest: DownloadRequest): Flow<DownloadPromise> {
        // implement your downloading here :)
        return flow {
            emit(downloadRequest.user.getType())
        }.catch {
            this.emit(VideoType.Unknown)
        }.flatMapLatest { type ->
            // fake it here!
            if (type == VideoType.Unknown) {
                throw UnknownVideoException()
            } else {
                flowOf(
                    DownloadPromise(
                        downloadedPath = "",
                        exception = null,
                        progress = -1,
                        videoType = type,
                        isDownloadComplete = false
                    ),
                    DownloadPromise(
                        downloadedPath = "",
                        exception = null,
                        progress = 100,
                        videoType = type,
                        isDownloadComplete = true
                    ),
                )
            }

        }
    }

}

private suspend fun String.getType(): VideoType {
    // maybe fetch the user about the download type from the remote call 
    return when (this) {
        "1" -> FourK
        "2" -> P1080
        "3" -> P720
        "test" -> {
            delay(10000) // delay for 10 seconds :D
            P720
        }

        else -> throw Exception("cannot download url not available!")
    }
}