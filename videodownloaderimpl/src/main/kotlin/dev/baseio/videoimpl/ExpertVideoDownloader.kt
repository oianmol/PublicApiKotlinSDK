package dev.baseio.videoimpl

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

internal class ExpertVideoDownloader(
    fileDownloader: FileDownloader
) : VideoDownloader,
    FileDownloader by fileDownloader,
    CoroutineScope by CoroutineScope(
        SupervisorJob() + Dispatchers.IO
    ) {
    private val downloadRequests = hashMapOf<DownloadRequest, Job>()
    override fun download(
        downloadRequest: DownloadRequest,
        receive: (DownloadPromise) -> Unit
    ) {
        launch(CoroutineExceptionHandler { _, throwable ->
            receive(DownloadPromise(exception = throwable))
        }) {
            downloadFile(
                this,
                downloadRequest
            ) { downloadPromise ->
                receive(downloadPromise)
                if (downloadPromise.isDownloadComplete || downloadPromise.exception != null) {
                    downloadRequests[downloadRequest]?.cancel() // cancel the job
                    downloadRequests.remove(downloadRequest) // remove the request
                }
            }
        }.also {
            downloadRequests[downloadRequest] = it
        }
    }

    override fun cancelAllDownloads() {
        cancel()
    }

    override fun isDownloading(request: DownloadRequest): Boolean {
        return downloadRequests.containsKey(request)
    }

    override fun cancel(request: DownloadRequest) {
        downloadRequests[request]?.cancel() // cancel the job
        downloadRequests.remove(request) // remove the request
        println("download cancelled! $request")
    }
}