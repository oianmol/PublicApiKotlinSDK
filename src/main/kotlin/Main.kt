import dev.baseio.videoimpl.DownloadRequest
import dev.baseio.videoimpl.VideoDownloader
import dev.baseio.videoimpl.VideoDownloaderProvider
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) = runBlocking {
    val downloader: VideoDownloader = VideoDownloaderProvider.provider()
    val flow = downloader.download(DownloadRequest("1", "https://netflix.com/file.mp4"))
    // Uncomment and see downloader as ExpertVideoDownloader // will not work
    flow.onEach {
        // here!
        println(it)
    }.launchIn(this).join()
}