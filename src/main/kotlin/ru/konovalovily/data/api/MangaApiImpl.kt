package ru.konovalovily.data.api

import kotlinx.coroutines.*
import ru.konovalovily.data.models.Category
import ru.konovalovily.data.models.Chapter
import ru.konovalovily.data.models.Manga
import ru.konovalovily.data.models.SearchResponce
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import ru.konovalovily.domain.models.SubscribeRequestDto
import ru.konovalovily.domain.models.SubscribeResponseDto


internal class MangaApiImpl : MangaApi {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override suspend fun lastUpdated(): List<Manga> {
        val doc: Document = Jsoup.connect(BASE_URL).get()
        return extractMangasByResponce(doc)
    }

    override suspend fun findMangasByName(name: String): List<Manga> {
        val doc: Document = Jsoup.connect("${BASE_URL}dosearch?do=search&subaction=search&query=$name").get()
        val element = doc.getElementsByClass("manga-list")[0]
        val listMangas = mutableListOf<Manga>()
        val startId = 28

        element.children().forEach {
            val link = it.getElementsByClass("alpha-link").attr("href")
            val elements = it.getElementsByClass("vis")
            val imageLink = it.getElementsByClass("img-responsive").attr("alt")
            if (imageLink.isNotEmpty()) {
                listMangas.add(
                    Manga(
                        id = link.drop(startId),
                        title = it.getElementsByClass("img-responsive").attr("alt"),
                        status = extractOtherPart(elements, "Статус:"),
                        genre = extractOtherPart(elements, "Жанр (вид):"),
                        anotherTitle = extractOtherPart(elements, "Другие названия:"),
                        link = link,
                        author = extractOtherPart(elements, "Автор(ы):"),
                        drawer = extractOtherPart(elements, "Художник(и):"),
                        views = extractOtherPart(elements, "Просмотры:"),
                        translator = extractOtherPart(elements, "Переводчик::"),
                        image = it.getElementsByClass("img-responsive").attr("src"),
                        category = extractCategory(elements)
                    )
                )
            }
        }
        return listMangas
    }

    override suspend fun getMangaById(id: String): Manga {
        val link = "${BASE_URL}manga/$id"
        val doc: Document = Jsoup.connect(link).get()
        val el = doc.getElementsByClass("flist-col")[0]
        val elements = el.getElementsByClass("vis")
        val chapterElem = doc.getElementsByAttributeValue("style", "padding: 3px 0;")
        val chapters = chapterElem.map {
            coroutineScope.async {
                val download = it.getElementsByClass("btn-filt2").attr("href")
                val date = it.getElementsByClass("date-chapter-title-rtl").text()
                val links = it.getElementsByTag("a")[1].attr("href")
                val ind = "https://mangabook.org/manga/".length + id.length + 1
                val titles = it.getElementsByTag("a")[1].text()
                Chapter(links.subSequence(ind, links.length).toString(), download, titles, date, links)
            }
        }.awaitAll()
        val description = doc.getElementsByClass("fdesc slice-this ficon clearfix")

        return Manga(
            id = id,
            title = doc.getElementsByClass("fheader")[0].text(),
            status = extractOtherPart(elements, "Статус:"),
            genre = extractOtherPart(elements, "Жанр (вид):"),
            anotherTitle = extractOtherPart(elements, "Другие названия:"),
            link = link,
            author = extractOtherPart(elements, "Автор(ы):"),
            drawer = extractOtherPart(elements, "Художник(и):"),
            views = extractOtherPart(elements, "Просмотры:"),
            translator = extractOtherPart(elements, "Переводчик::"),
            description = description.text(),
            image = doc.getElementsByClass("img-responsive")[0].attr("src"),
            chapters = chapters,
            category = extractCategory(elements)
        )
    }

    override suspend fun getMangaChapterByLink(mangaId: String?, chapterId: String?): List<String> {
        val doc: Document = Jsoup.connect("${BASE_URL}manga/$mangaId/$chapterId").get()
        val listIndex = doc.getElementsByClass("btn-filt2").select("select").text().split(" ")
        return listIndex.map {
            coroutineScope.async { extractImageLink("https://mangabook.org/manga/$mangaId/$chapterId/$it") }
        }.awaitAll()
    }

    override suspend fun getMangasByCategoryId(categoryId: String, page: Int): SearchResponce {
        val link = "https://mangabook.org/manga-list/category/$categoryId?page=$page"
        val doc: Document = Jsoup.connect(link).get()
        var lastItem = 0
        try {
            val elements = doc.getElementsByClass("page-link")
            val list = elements.text().split(" ")
            lastItem = list[list.lastIndex - 1].toInt()
        } catch (_: Exception) {
        }

        return SearchResponce(
            mangas = extractMangasByResponce(doc),
            pagesMax = lastItem
        )
    }

    override suspend fun getMangaByPopularity(page: Int): SearchResponce {
        val link =
            "${BASE_URL}filterList?page=$page&ftype[]=1&cat=&status[]=0&alpha=&year_min=&year_max=&sortBy=views&asc=true&author=&artist=&tag="
        val doc: Document = Jsoup.connect(link).get()
        var lastItem = 0
        try {
            val elements = doc.getElementsByClass("page-link")
            val list = elements.text().split(" ")
            lastItem = list[list.lastIndex - 1].toInt()
        } catch (_: Exception) {
        }

        return SearchResponce(
            mangas = extractMangasByResponce(doc),
            pagesMax = lastItem
        )
    }

    override suspend fun getSubscribedCountChapters(data: SubscribeRequestDto): SubscribeResponseDto {
        val doc: Document = Jsoup.connect("${BASE_URL}manga/${data.mangaId}").get()
        val chapterElem = doc.getElementsByAttributeValue("style", "padding: 3px 0;")
        val baseLinkLen = "https://mangabook.org/manga/${data.mangaId}/".length
        var counter = 0

        for (elem in chapterElem) {
            val links = elem.getElementsByTag("a")[1].attr("href")
            val id = links.subSequence(baseLinkLen, links.length).toString()
            if (data.lastChapter == id) break
            counter++
        }
        return SubscribeResponseDto(data.lastChapter, counter)
    }

    private fun extractImageLink(linkPage: String): String {
        val doc: Document = Jsoup.connect(linkPage).get()
        return doc.getElementsByClass("img-responsive scan-page").attr("src")
    }

    private fun extractCategory(element: Elements): List<Category> {
        val categoryList = mutableListOf<Category>()
        for (el in element) {
            if (el.getElementsByTag("span").text() == "Категории:") {
                val teg = el.getElementsByTag("a")
                teg.forEach {
                    categoryList.add(
                        Category(
                            id = it.attr("href").subSequence(42, it.attr("href").length).toString(),
                            name = it.text()
                        )
                    )
                }
                break
            }
        }
        return categoryList
    }

    private fun extractOtherPart(element: Elements, partName: String): String? {
        for (el in element) {
            val text = el.child(0).text()
            if (text == partName) {
                return el.text().subSequence(partName.length + 1, el.text().length).toString()
            }
        }
        return null
    }

    private suspend fun extractMangasByResponce(elements: Element): List<Manga> {
        val element = elements.getElementsByClass("short clearfix")
        return element.map {
            coroutineScope.async {
                val title = it.getElementsByClass("sh-title").text()
                val link = it.getElementsByTag("a").attr("href")
                val id = link.subSequence(28, link.length)
                val imageLink = it.getElementsByTag("img").attr("src")
                Manga(
                    id = id.toString(),
                    title = title,
                    link = link,
                    image = imageLink
                )
            }
        }.awaitAll()
    }

    private companion object {

        const val BASE_URL = "https://mangabook.org/"
    }
}