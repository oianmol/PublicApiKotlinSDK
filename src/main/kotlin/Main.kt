import dev.baseio.videoimpl.DownloadRequest
import dev.baseio.videoimpl.VideoDownloader
import dev.baseio.videoimpl.VideoDownloaderProvider
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val downloader: VideoDownloader = VideoDownloaderProvider.provider()
    with(downloader) {
        val flow = download(
            DownloadRequest(
                user = "5", // change user to see how the flow fails
                fileUrl = "https://netflix.com/file.mp4"
            )
        )
        this@runBlocking.launch {
            flow.collect {
                println(it)
                if (it.isDownloadComplete) {
                    cancel()
                }
                it.exception?.let {
                    println(it)
                    cancel()
                }
            }
        }.join()
    }
}