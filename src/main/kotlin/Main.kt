import dev.baseio.videoimpl.DownloadRequest
import dev.baseio.videoimpl.VideoDownloader
import dev.baseio.videoimpl.VideoDownloaderProvider
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) = runBlocking {
    val downloader: VideoDownloader = VideoDownloaderProvider.provider()
    with(downloader) {
        val flow = download(DownloadRequest("5", "https://netflix.com/file.mp4"))
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