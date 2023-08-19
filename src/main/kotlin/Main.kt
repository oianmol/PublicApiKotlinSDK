import dev.baseio.videoimpl.DownloadRequest
import dev.baseio.videoimpl.VideoDownloader
import dev.baseio.videoimpl.VideoDownloaderProvider
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val downloader: VideoDownloader = VideoDownloaderProvider.provider()
    // downloader as ExpertVideoDownloader // fails since ExpertVideoDownloader is not visible
    with(downloader) {
        val request = DownloadRequest(
            user = "5", // change user to see how the flow fails
            fileUrl = "https://netflix.com/file.mp4"
        )
        val flow = download(downloadRequest = request)
        this@runBlocking.launch {
            flow.collect {
                println(it)
                if (it.isDownloadComplete) {
                    cancel()
                }
                it.exception?.let { throwable ->
                    println(throwable)
                    cancel()
                }
            }
        }.join()
    }
}