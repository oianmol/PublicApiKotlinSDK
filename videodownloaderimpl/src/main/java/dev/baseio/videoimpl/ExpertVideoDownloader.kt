package dev.baseio.videoimpl

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

internal class ExpertVideoDownloader(
    fileDownloader: FileDownloader
) : VideoDownloader,
    FileDownloader by fileDownloader,
    CoroutineScope by CoroutineScope(
        SupervisorJob() + Dispatchers.IO
    ) {

    override fun download(downloadRequest: DownloadRequest): Flow<DownloadPromise> {
        return MutableSharedFlow<DownloadPromise>().apply {
            downloadFile(this@ExpertVideoDownloader, downloadRequest, this)
        }
    }

    override fun cancelAllDownloads() {
        cancel()
    }
}