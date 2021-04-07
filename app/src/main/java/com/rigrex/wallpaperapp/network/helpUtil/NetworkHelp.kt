package com.rigrex.wallpaperapp.network.helpUtil

import android.util.Log
import com.rigrex.wallpaperapp.database.RoomRepository
import com.rigrex.wallpaperapp.database.dataModels.WallPaperModel
import com.rigrex.wallpaperapp.database.unsplash.unsplashDescriptionData.UnSplashModel
import com.rigrex.wallpaperapp.network.ApiClient
import com.rigrex.wallpaperapp.network.ApiInterface
import com.shehzad.wallpaperapp.pixabey.Hit
import com.shehzad.wallpaperapp.unsplash.UnsplashModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposables
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.math.RoundingMode
import java.net.URL

class NetworkHelp {
    companion object {

        private var list = mutableListOf<ValueHolder>()
        private var noOfApi = 2

        fun flushData() {
            list = mutableListOf()
        }

        fun getNetworkWithOfflineSearch(
                search: String,
                offline: Boolean,
                informer: (Observable<MutableList<WallPaperModel>>?) -> Unit
        ) {
            saveDataRef(search)
            if (offline) {
                RoomRepository.getOfflineData(search) {
                    informer(it?.switchMap {
                        if (it?.isNotEmpty() == true) {
                            Observable.just(it.toMutableList())
                        } else {
                            getNetworkData(query = search)
                        }
                    })
                }
            } else {
                informer(getNetworkData(query = search))
            }
        }

        fun getDetail(photo_id: String, converterFactory: ConverterFactory<UnSplashModel, WallPaperModel>): Observable<WallPaperModel> {
            return ApiClient
                    .getClient(ApiClient.CLIENT_TYPE_UNSPLASH)
                    .create(ApiInterface::class.java)
                    .unSplashImageDetail(photo_id = "photos/$photo_id/")
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io(), true)
                    .map { converterFactory.convert(it).also { RoomRepository.updateWallPaperModel(it) } }
        }

        private fun saveDataRef(ref: String) {
            list.indexOfFirst { it.category == ref }.also {
                if (it < 0) {
                    list.add(ValueHolder(MutableList(noOfApi) { 1 }, ref, 1))
                }
            }
        }

        fun getNetworkWithOffline(
                category: String,
                offline: Boolean,
                informer: (Observable<MutableList<WallPaperModel>>?) -> Unit
        ) {
            saveDataRef(category)
            if (offline) {
                Log.d("NetworkHelp", "offline is true")
                RoomRepository.getOfflineData(category) {
                    informer(it?.switchMap {
                        if (it?.isNotEmpty() == true) {
                            Log.d("NetworkHelp", "getNetworkWithOfflineSearch: calling from offline list")
                            Observable.just(it.toMutableList())
                        } else {
                            Log.d("NetworkHelp", "getNetworkWithOfflineSearch: calling from online list")
                            getNetworkData(category = category)
                        }
                    })
                }
            } else {
                informer(getNetworkData(category = category))
            }
        }

        fun giveDetail(wallPaperModel: WallPaperModel): Observable<WallPaperModel> {
            return if (wallPaperModel.descriptionAvl) {
                Observable.just(wallPaperModel)
            } else {
                Observable.just(wallPaperModel)
            }
        }

        private fun getNetworkData(
                category: String = "",
                query: String = ""
        ): Observable<MutableList<WallPaperModel>>? {
            var value = list[list.indexOfFirst { if (query.isEmpty()) it.category == category else it.category == query }]
            return if (value.index == 0) {
                Log.d("NetworkHelp", "getNetworkData: getting the pixabay data")
                getPixaBayData(
                        query = query,
                        category = category,
                        factory = FactoryProvider.provideFactory(Hit::class.java, if (category.isEmpty()) query else category),
                        page = value
                )?.also { value.index = 1 }
            } else {
                Log.d("NetworkHelp", "getNetworkData: getting unsplash data")
                getUnSplashData(
                        query = if (query.isEmpty()) category else query,
                        factory = FactoryProvider.provideFactory(UnsplashModel::class.java, if (category.isEmpty()) query else category),
                        page = value
                )?.also { value.index = 0 }
            }
        }


        private fun getUnSplashData(
                query: String = "",
                page: ValueHolder,
                factory: ConverterFactory<UnsplashModel, WallPaperModel>
        ): Observable<MutableList<WallPaperModel>>? {
            return ApiClient
                    .getClient(ApiClient.CLIENT_TYPE_UNSPLASH)
                    .create(ApiInterface::class.java)
                    .searchUnSplashPhotos(page = "${page.paging[1]}", query = query)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread(), true)
                    .map {
                        it?.results?.map { factory.convert(it) }?.requireNoNulls()?.also {
                            RoomRepository.addCatData(it)
                            Log.d("NetworkHelp", "getUnSplashData: saving unsplash data to room ${it.size}")
                        }?.toMutableList() ?: mutableListOf()
                    }?.also { page.apply { paging[1] += paging[1] } }
        }

        private fun getPixaBayData(
                query: String = "",
                page: ValueHolder,
                category: String,
                factory: ConverterFactory<Hit, WallPaperModel>
        ): Observable<MutableList<WallPaperModel>>? {
            return ApiClient
                    .getClient(ApiClient.CLIENT_TYPE_PIXABAY)
                    .create(ApiInterface::class.java)
                    .searchPixabay(category = category, page = "${page.paging[0]}", q = query)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread(), true)
                    .map {
                        it.hits?.map { factory.convert(it) }?.requireNoNulls()?.also {
                            RoomRepository.addCatData(it)
                            Log.d("NetworkHelp", "getPixaBayData: saving pixabay data to room ${it.size}")
                        }?.toMutableList() ?: mutableListOf()
                    }?.also { page.apply { paging[0] += paging[0] } }
        }

        fun getImageSize(url: String, withCookie: Boolean = false, cookie: String = "") =
                try {
                    URL(url).openConnection().let {
                        if (withCookie)
                            it.setRequestProperty("Cookie", cookie)
                        it.connect()
                        ((it.contentLength.toDouble() / 1024.0) / 1024.0).toBigDecimal()
                                .setScale(3, RoundingMode.HALF_EVEN).toDouble()?.also {
                                    Log.d("NetworkHelp", "getImageSize: is called with $it")
                                }
                    }
                } catch (e: Exception) {
                    Log.d("NetworkHelp", "getImageSize: exception $e 0.0")
                    0.toDouble()
                }
    }
}