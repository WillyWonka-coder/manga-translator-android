package com.manga.translate.network

import com.manga.translate.platform.AppLogger
import java.io.File
import java.io.FileOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

data class OnlineManga(
    val id: String,
    val title: String,
    val description: String
)

data class OnlineChapter(
    val id: String,
    val chapterNum: String,
    val title: String,
    val language: String
)

object MangaOnlineDownloader {
    private val client = OkHttpClient()
    private const val BASE_URL = "https://api.mangadex.org"

    suspend fun searchManga(query: String): List<OnlineManga> = withContext(Dispatchers.IO) {
        val results = mutableListOf<OnlineManga>()
        try {
            val url = "$BASE_URL/manga?title=${query.trim()}&limit=15&includes[]=cover_art"
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val body = response.body?.string().orEmpty()
            val json = JSONObject(body)
            val data = json.optJSONArray("data") ?: return@withContext emptyList()

            for (i in 0 until data.length()) {
                val item = data.getJSONObject(i)
                val id = item.getString("id")
                val attributes = item.getJSONObject("attributes")
                val titleObj = attributes.getJSONObject("title")
                val title = titleObj.optString("en", titleObj.optString("ja", titleObj.optString("ru", "Untitled")))
                val descObj = attributes.optJSONObject("description")
                val desc = descObj?.optString("en", "") ?: ""
                results.add(OnlineManga(id, title, desc))
            }
        } catch (e: Exception) {
            AppLogger.log("Downloader", "Search failed", e)
        }
        results
    }

    suspend fun getChapters(mangaId: String, lang: String = "ja"): List<OnlineChapter> = withContext(Dispatchers.IO) {
        val chapters = mutableListOf<OnlineChapter>()
        try {
            val url = "$BASE_URL/manga/$mangaId/feed?translatedLanguage[]=$lang&limit=100&order[chapter]=asc"
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val body = response.body?.string().orEmpty()
            val json = JSONObject(body)
            val data = json.optJSONArray("data") ?: return@withContext emptyList()

            for (i in 0 until data.length()) {
                val item = data.getJSONObject(i)
                val id = item.getString("id")
                val attrs = item.getJSONObject("attributes")
                val chapterNum = attrs.optString("chapter", "${i + 1}")
                val title = attrs.optString("title", "Chapter $chapterNum")
                chapters.add(OnlineChapter(id, chapterNum, title, lang))
            }
        } catch (e: Exception) {
            AppLogger.log("Downloader", "Get chapters failed", e)
        }
        chapters
    }

    suspend fun downloadChapter(
        chapterId: String,
        targetDir: File,
        onProgress: (current: Int, total: Int) -> Unit
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            if (!targetDir.exists()) targetDir.mkdirs()
            val metaUrl = "$BASE_URL/at-home/server/$chapterId"
            val metaResponse = client.newCall(Request.Builder().url(metaUrl).build()).execute()
            val metaJson = JSONObject(metaResponse.body?.string().orEmpty())
            val baseUrl = metaJson.getString("baseUrl")
            val chapterObj = metaJson.getJSONObject("chapter")
            val hash = chapterObj.getString("hash")
            val pages = chapterObj.getJSONArray("data")

            for (i in 0 until pages.length()) {
                val fileName = pages.getString(i)
                val pageUrl = "$baseUrl/data/$hash/$fileName"
                val pageReq = Request.Builder().url(pageUrl).build()
                val pageResp = client.newCall(pageReq).execute()
                val bytes = pageResp.body?.bytes() ?: continue

                val ext = fileName.substringAfterLast('.', "jpg")
                val destFile = File(targetDir, String.format("%03d.%s", i + 1, ext))
                FileOutputStream(destFile).use { it.write(bytes) }
                withContext(Dispatchers.Main) {
                    onProgress(i + 1, pages.length())
                }
            }
            true
        } catch (e: Exception) {
            AppLogger.log("Downloader", "Download failed", e)
            false
        }
    }
}