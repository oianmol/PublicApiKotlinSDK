package dev.baseio.videoimpl

object VideoDownloaderProvider {
    fun provider(fileDownloader: FileDownloader = SmartFileDownloader()): VideoDownloader {
        return ExpertVideoDownloader(fileDownloader)
    }
}