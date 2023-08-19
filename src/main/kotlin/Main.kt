import dev.baseio.videoimpl.DownloadRequest
import dev.baseio.videoimpl.VideoDownloader
import dev.baseio.videoimpl.VideoDownloaderProvider
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val downloader: VideoDownloader = VideoDownloaderProvider.provider()
    // downloader as ExpertVideoDownloader // fails since ExpertVideoDownloader is not visible
    with(downloader) {
        val request = DownloadRequest(
            user = "1", // change user to see how the flow fails
            fileUrl = "https://netflix.com/file.mp4"
        )
        download(downloadRequest = request,
            receive = {
                println(it)
            })
        val downloadStartTime = System.currentTimeMillis()
        while (downloader.isDownloading(request)) {
            println("downloading....")
            if (System.currentTimeMillis().minus(downloadStartTime) > 5000) { // more than 5 seconds
                downloader.cancel(request)
            }
        }
        println("downloading finidhed....")
    }
}