package com.rigrex.wallpaperapp.network.helpUtil

import com.rigrex.wallpaperapp.database.dataModels.WallPaperModel
import com.rigrex.wallpaperapp.database.unsplash.unsplashDescriptionData.UnSplashModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable

class NetworkHelpNew {
    companion object {
        private var disposable: CompositeDisposable = CompositeDisposable()

        fun flushData() {
            disposable.clear()
            NetworkHelp.flushData()
        }

        fun getNetworkWithOffline(
                category: String,
                offline: Boolean,
                subscriber: (MutableList<WallPaperModel>) -> Unit,
                onFail : ((Throwable?)->Unit)? = null
        ){
            NetworkHelp.getNetworkWithOffline(category,offline){
                disposable.add(it?.subscribe(subscriber,onFail))
            }
        }

        fun getDetail(
                photo_id: String,
                converterFactory: ConverterFactory<UnSplashModel, WallPaperModel>,
                subscriber: (WallPaperModel) -> Unit,
                onFail : (Throwable?)->Unit
        ) {
            disposable.add(NetworkHelp.getDetail(photo_id, converterFactory).subscribe(subscriber,onFail))
        }

        fun getNetworkWithOfflineSearch(
                search: String,
                offline: Boolean,
                subscriber: (MutableList<WallPaperModel>) -> Unit
        ) {
            NetworkHelp.getNetworkWithOfflineSearch(search, offline) {
                disposable.add(it?.subscribe(subscriber))
            }
        }
    }
}