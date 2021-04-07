package com.rigrex.wallpaperapp.network.helpUtil

import android.util.Log
import com.rigrex.wallpaperapp.database.dataModels.WallPaperModel
import com.rigrex.wallpaperapp.database.unsplash.unsplashDescriptionData.UnSplashModel
import com.shehzad.wallpaperapp.pixabey.Hit
import com.shehzad.wallpaperapp.pixabey.HitModel
import com.shehzad.wallpaperapp.unsplash.UnsplashModel
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.internal.notify
import okhttp3.internal.wait
import java.lang.IllegalArgumentException
import java.math.RoundingMode
import kotlin.coroutines.EmptyCoroutineContext

interface ConverterFactory<T, U> {
    fun convert(t: T): U
}

class FactoryProvider {
    companion object {

        fun <T> provideFactory(
                clazz: Class<T>,
                category: String
        ): ConverterFactory<T, WallPaperModel> {
            return when (clazz) {
                Hit::class.java -> {
                    PixaBayConverter(category) {
                        "${
                            ((it / 1024.0) / 1024.0).toBigDecimal()
                                    .setScale(3, RoundingMode.HALF_EVEN).toDouble()
                        } MB"
                    }
                }
                UnsplashModel::class.java -> {
                    UnSplashConverter(category)
                }
                else -> throw IllegalArgumentException("wrong argument provide for list check the documentation")
            } as ConverterFactory<T, WallPaperModel>
        }
    }
}

class PixaBayConverter(var category: String, var sizeCal: (Int) -> String) :
        ConverterFactory<Hit, WallPaperModel> {

    override fun convert(t: Hit): WallPaperModel {
        var model = WallPaperModel()
        model.apply {
            id = "${t.id}"
            descriptionAvl = true
            imageCategory = this@PixaBayConverter.category
            previewImageUrl = t.webformatURL
            largeImageUrl = t.largeImageURL
            imageDownloads = "${t.downloads}"
            imageReferer = t.previewURL
            imageDimensions = "${t.imageWidth} x ${t.imageHeight}"
            imageFileSize = sizeCal(t.imageSize ?: 0)
            imageTags = t.tags?.split(",")?.map { it.trim().replace(",", "") }?.joinToString(separator = "|")
        }

        return model
    }
}

class UnSplashDescriptorFactory(var model: WallPaperModel) : ConverterFactory<UnSplashModel, WallPaperModel> {

    override fun convert(t: UnSplashModel): WallPaperModel {
        model.apply {
            descriptionAvl = true
            t.tags?.forEach {
                Log.d("UnSplashDescriptor", "convert: ${it.title}")
            }
            imageTags = t.tags?.map { it.title }?.joinToString(separator = "|")
            imageDownloads = "${t.downloads}"
            imageFileSize = "${NetworkHelp.getImageSize(t.urls?.full!!)} MB"
        }

        return model
    }
}

class UnSplashConverter(var category: String) : ConverterFactory<UnsplashModel, WallPaperModel> {
    override fun convert(t: UnsplashModel): WallPaperModel {
        var model = WallPaperModel()
        model.apply {
            id = "${t.id}"
            imageDimensions = "${t.width} x ${t.height}"
            imageCategory = this@UnSplashConverter.category
            imageReferer = "https://unsplash.com/photos/$id"
            previewImageUrl = t.urls?.thumb ?: t.urls?.small
            largeImageUrl = t.urls?.full ?: t.urls?.regular
        }

        return model
    }
}