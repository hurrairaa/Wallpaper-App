package com.rigrex.wallpaperapp.activities

import android.content.Intent
import android.os.Bundle
import android.text.style.TtsSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.rigrex.wallpaperapp.R
import com.rigrex.wallpaperapp.adapters.RelatedAdapter
import com.rigrex.wallpaperapp.adapters.TagAdapter
import com.rigrex.wallpaperapp.adapters.TagItemDecortor
import com.rigrex.wallpaperapp.database.RoomRepository
import com.rigrex.wallpaperapp.database.dataModels.DownloadModel
import com.rigrex.wallpaperapp.database.dataModels.DownloadStatus
import com.rigrex.wallpaperapp.database.dataModels.WallPaperModel
import com.rigrex.wallpaperapp.databinding.ActivityWalPaperInfoBinding
import com.rigrex.wallpaperapp.helpUtility.HelpUtil
import com.rigrex.wallpaperapp.helpUtility.WallPaperHelper
import com.rigrex.wallpaperapp.network.DownloadUtil
import com.rigrex.wallpaperapp.network.helpUtil.DownloadCallBacks
import com.rigrex.wallpaperapp.network.helpUtil.NetworkHelpNew
import com.rigrex.wallpaperapp.network.helpUtil.UnSplashDescriptorFactory
import io.reactivex.rxjava3.disposables.Disposable

class WalPaperInfoActivity : AppCompatActivity(), DownloadCallBacks {
    companion object {
        const val MODEL = "model"
    }

    private lateinit var binding: ActivityWalPaperInfoBinding
    private var currentModel: WallPaperModel? = null
    private lateinit var tagAdapter: TagAdapter
    private lateinit var relatedAdapter: RelatedAdapter
    private var fav = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalPaperInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        currentModel = intent?.getParcelableExtra(MODEL)
        initTagAdapter(currentModel)
        checkSetDesc(currentModel)
        initRelatedAdapter(currentModel)
        checkFav(currentModel?.id)
        initListeners()
    }

    private fun initListeners() {
        binding.bottomSheet.favorite.setOnClickListener {
            RoomRepository.addOrDeleteFavorite(currentModel?.castToFav()!!)
            fav = !fav
            runOnUiThread {
                if (fav) {
                    binding.bottomSheet.favorite.setImageResource(R.drawable.heart_full)
                } else {
                    binding.bottomSheet.favorite.setImageResource(R.drawable.favorite_icon)
                }
            }
        }
        RoomRepository.isDownloaded(currentModel?.id!!) { isDownloaded ->
            if (isDownloaded) {
                if (DownloadUtil.isDownloaded(currentModel?.getWallPaperName()!!)) {
                    runOnUiThread { binding.bottomSheet.download.setImageResource(R.drawable.downloaded) }
                } else {
                    RoomRepository.deleteDownloading(currentModel?.id!!)
                    runOnUiThread { binding.bottomSheet.download.setImageResource(R.drawable.download) }
                }
            } else {
                RoomRepository.isDownloading(currentModel?.id!!) { isDownloading ->
                    if (isDownloading) {
                        runOnUiThread { binding.bottomSheet.download.setImageResource(R.drawable.cancel_download) }
                    } else {
                        runOnUiThread { binding.bottomSheet.download.setImageResource(R.drawable.download) }
                    }
                }
            }
        }
        binding.bottomSheet.share.setOnClickListener {
            HelpUtil.share(this, currentModel?.imageReferer ?: "")
        }
        binding.bottomSheet.download.setOnClickListener {
            RoomRepository.getDownloading(currentModel?.id!!) {
                if (it == null || it.downloadStatus == DownloadStatus.NORMAL) {
                    DownloadUtil.download(DownloadModel().apply {
                        id = currentModel?.id ?: ""
                        imageUrl = currentModel?.largeImageUrl
                    }, this)
                } else {
                    if (it.isDownloading()) {
                        DownloadUtil.cancelDOwnload(it.pId?.toInt()!!)
                    } else {
                        startActivity(Intent(this, DownloadInfoActivity::class.java).putExtra(DownloadInfoActivity.DOWNLOAD_MODEL, it))
                    }
                }
            }
        }
        binding.bottomSheet.gallery.setOnClickListener {
            RoomRepository.getDownload(currentModel?.id!!) {
                if (it == null || it.downloadStatus == DownloadStatus.NORMAL) {
                    DownloadUtil.download(
                            DownloadModel().apply {
                                id = currentModel?.id!!
                                imageUrl = currentModel?.largeImageUrl
                            }, this)
                    DownloadUtil.attachQueueCalls(currentModel?.id!!) { model ->
                        runOnUiThread { WallPaperHelper.createWalPaperChooser(this) { Log.d("WallPaperHelper", "initListeners: ");WallPaperHelper.setWallPaper(model, this, it) } }
                    }
                } else if (it.isDownloading()) {
                    runOnUiThread { Toast.makeText(this, "Please Wait While Downloading Complete", Toast.LENGTH_SHORT).show() }
                } else if (it.isDownloaded()) {
                    runOnUiThread { WallPaperHelper.createWalPaperChooser(this) { choice -> Log.d("WallPaperHelper", "initListeners: ");WallPaperHelper.setWallPaper(it, this, choice) } }
                }
            }
        }
    }

    private fun checkFav(id: String?) {
        RoomRepository.checkIfFav(id!!) {
            fav = it
            runOnUiThread {
                if (it) {
                    binding.bottomSheet.favorite.setImageResource(R.drawable.heart_full)
                } else {
                    binding.bottomSheet.favorite.setImageResource(R.drawable.favorite_icon)
                }
            }
        }
    }

    private fun initRelatedAdapter(model: WallPaperModel?) {
        relatedAdapter = RelatedAdapter(mutableListOf()) {
            startActivity(Intent(this, WalPaperInfoActivity::class.java).putExtra(MODEL, it))
        }
        binding.bottomSheet.relatedRec.addItemDecoration(
                TagItemDecortor(
                        resources.getDimension(R.dimen.dp_10).toInt()
                )
        )
        binding.bottomSheet.relatedRec.setHasFixedSize(true)
        binding.bottomSheet.relatedRec.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.bottomSheet.relatedRec.adapter = relatedAdapter
        RoomRepository.getOfflineData(model?.imageCategory ?: "") {
            it?.subscribe({ relatedAdapter.setData(it ?: mutableListOf()) }) {
                Log.d(
                        "WallPaperInfoItem",
                        "initRelatedAdapter: error occured"
                )
            }
        }
    }

    private fun initTagAdapter(model: WallPaperModel?) {
        tagAdapter = TagAdapter(model?.getTags() ?: emptyList()) {
            startActivity(
                    Intent(this, SearchActivity::class.java)
                            .putExtra(SearchActivity.SEARCHED_WORD, it)
            )
        }
        binding.bottomSheet.tagRecView.addItemDecoration(
                TagItemDecortor(
                        resources.getDimension(R.dimen.dp_10).toInt()
                )
        )
        binding.bottomSheet.tagRecView.setHasFixedSize(true)
        binding.bottomSheet.tagRecView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.bottomSheet.tagRecView.adapter = tagAdapter
    }

    override fun onPause() {
        super.onPause()
        DownloadUtil.unRegisterListener(currentModel?.id!!)
    }

    override fun onResume() {
        super.onResume()
        DownloadUtil.registerListener(currentModel?.id!!, this)
    }

    private fun checkSetDesc(model: WallPaperModel?) {
        if (model?.descriptionAvl == false) {
            NetworkHelpNew.getDetail(model.id, UnSplashDescriptorFactory(model), { runOnUiThread { setDesc(it) } }) { runOnUiThread { setDesc(model) } }
        } else {
            setDesc(model)
        }
    }

    private fun setDesc(model: WallPaperModel?) {
        if (isDestroyed) {
            return
        }
        binding.infoContent.drawerBackButton.setOnClickListener { finish() }
        Glide.with(this)
                .load(model?.largeImageUrl)
                .into(binding.infoContent.mainImage)
        tagAdapter.setItems(model?.getTags() ?: mutableListOf())
        binding.bottomSheet.downloads.text = model?.imageDownloads
        binding.bottomSheet.size.text = model?.imageFileSize
        binding.bottomSheet.dimensions.text = model?.imageDimensions
    }

    override fun onCancel() {
        runOnUiThread {
            Toast.makeText(this, "Canceled...", Toast.LENGTH_SHORT).show()
            binding.bottomSheet.downloadProgress.visibility = View.GONE
            binding.bottomSheet.download.setImageResource(R.drawable.download)
        }
    }

    override fun onProgress(progress: Int) {
        runOnUiThread {
            binding.bottomSheet.downloadProgress.visibility = View.VISIBLE
            binding.bottomSheet.downloadProgress.progress = progress
        }
    }

    override fun onPauseDC() {
        runOnUiThread {
            Toast.makeText(this, "pause...", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStartResume() {
        runOnUiThread {
            Toast.makeText(this, "Downloading...", Toast.LENGTH_SHORT).show()
            binding.bottomSheet.downloadProgress.visibility = View.VISIBLE
            binding.bottomSheet.download.setImageResource(R.drawable.cancel_download)
        }
    }

    override fun onComplete() {
        runOnUiThread {
            Toast.makeText(this, "Downloading Complete...", Toast.LENGTH_SHORT).show()
            binding.bottomSheet.downloadProgress.visibility = View.GONE
            binding.bottomSheet.download.setImageResource(R.drawable.downloaded)
        }
    }

    override fun onError(errorMessage: String) {
        runOnUiThread {
            Log.d("WalPaperInfo", "error obscured while downloading the app $errorMessage")
            Toast.makeText(this, "Error...", Toast.LENGTH_SHORT).show()
            binding.bottomSheet.download.setImageResource(R.drawable.download)
            binding.bottomSheet.downloadProgress.visibility = View.GONE
        }
    }
}