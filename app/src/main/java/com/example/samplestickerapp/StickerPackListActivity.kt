/*
 * Copyright (c) WhatsApp Inc. and its affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */
package com.example.samplestickerapp

import android.os.AsyncTask
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.samplestickerapp.StickerPackListAdapter.OnAddButtonClickedListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import java.lang.ref.WeakReference
import java.util.*

class StickerPackListActivity : AddStickerPackActivity() {
    private var packLayoutManager: LinearLayoutManager? = null
    private var packRecyclerView: RecyclerView? = null
    private var allStickerPacksListAdapter: StickerPackListAdapter? = null
    private var whiteListCheckAsyncTask: WhiteListCheckAsyncTask? = null
    private var stickerPackList: ArrayList<StickerPack>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sticker_pack_list)
        packRecyclerView = findViewById(R.id.sticker_pack_list)
        stickerPackList = intent.getParcelableArrayListExtra(EXTRA_STICKER_PACK_LIST_DATA)
        showStickerPackList(stickerPackList)
        if (supportActionBar != null) {
            supportActionBar!!.title = resources.getQuantityString(
                R.plurals.title_activity_sticker_packs_list,
                stickerPackList!!.size
            )
        }

        val adView: AdView = findViewById(R.id.adView)
        adView.loadAd(AdRequest.Builder().build())
    }

    override fun onResume() {
        super.onResume()
        whiteListCheckAsyncTask = WhiteListCheckAsyncTask(this)
        whiteListCheckAsyncTask!!.execute(*stickerPackList!!.toTypedArray())
    }

    override fun onPause() {
        super.onPause()
        if (whiteListCheckAsyncTask != null && !whiteListCheckAsyncTask!!.isCancelled) {
            whiteListCheckAsyncTask!!.cancel(true)
        }
    }

    private fun showStickerPackList(stickerPackList: List<StickerPack>?) {
        allStickerPacksListAdapter =
            StickerPackListAdapter(stickerPackList!!, onAddButtonClickedListener)
        packRecyclerView!!.adapter = allStickerPacksListAdapter
        packLayoutManager = LinearLayoutManager(this)
        packLayoutManager!!.orientation = RecyclerView.VERTICAL
        val dividerItemDecoration = DividerItemDecoration(
            packRecyclerView!!.context,
            packLayoutManager!!.orientation
        )
        packRecyclerView!!.addItemDecoration(dividerItemDecoration)
        packRecyclerView!!.layoutManager = packLayoutManager
        packRecyclerView!!.viewTreeObserver.addOnGlobalLayoutListener { recalculateColumnCount() }
    }

    private val onAddButtonClickedListener = object : OnAddButtonClickedListener {
        override fun onAddButtonClicked(stickerPack: StickerPack?) {
            addStickerPackToWhatsApp(
                stickerPack!!.identifier!!, stickerPack.name!!
            )
        }
    }

    private fun recalculateColumnCount() {
        val previewSize =
            resources.getDimensionPixelSize(R.dimen.sticker_pack_list_item_preview_image_size)
        val firstVisibleItemPosition = packLayoutManager!!.findFirstVisibleItemPosition()
        val viewHolder =
            packRecyclerView!!.findViewHolderForAdapterPosition(firstVisibleItemPosition) as StickerPackListItemViewHolder?
        if (viewHolder != null) {
            val widthOfImageRow = viewHolder.imageRowView.measuredWidth
            val max = Math.max(widthOfImageRow / previewSize, 1)
            val maxNumberOfImagesInARow = Math.min(STICKER_PREVIEW_DISPLAY_LIMIT, max)
            val minMarginBetweenImages =
                (widthOfImageRow - maxNumberOfImagesInARow * previewSize) / (maxNumberOfImagesInARow - 1)
            allStickerPacksListAdapter!!.setImageRowSpec(
                maxNumberOfImagesInARow,
                minMarginBetweenImages
            )
        }
    }

    internal class WhiteListCheckAsyncTask(stickerPackListActivity: StickerPackListActivity) : AsyncTask<StickerPack?, Void?, List<StickerPack>>() {
        private val stickerPackListActivityWeakReference: WeakReference<StickerPackListActivity> =
            WeakReference(stickerPackListActivity)

        override fun doInBackground(vararg stickerPackArray: StickerPack?): MutableList<StickerPack> {
            val stickerPackListActivity = stickerPackListActivityWeakReference.get()
                ?: return Arrays.asList(*stickerPackArray)
            for (stickerPack in stickerPackArray) {
                stickerPack!!.isWhitelisted = WhitelistCheck.isWhitelisted(
                    stickerPackListActivity,
                    stickerPack.identifier!!
                )
            }
            return Arrays.asList(*stickerPackArray)
        }

        override fun onPostExecute(stickerPackList: List<StickerPack>) {
            val stickerPackListActivity = stickerPackListActivityWeakReference.get()
            if (stickerPackListActivity != null) {
                stickerPackListActivity.allStickerPacksListAdapter!!.setStickerPackList(
                    stickerPackList
                )
                stickerPackListActivity.allStickerPacksListAdapter!!.notifyDataSetChanged()
            }
        }

    }

    companion object {
        const val EXTRA_STICKER_PACK_LIST_DATA = "sticker_pack_list"
        private const val STICKER_PREVIEW_DISPLAY_LIMIT = 5
    }
}