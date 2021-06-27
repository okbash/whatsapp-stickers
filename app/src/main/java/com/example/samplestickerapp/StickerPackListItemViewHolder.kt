/*
 * Copyright (c) WhatsApp Inc. and its affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */
package com.example.samplestickerapp

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StickerPackListItemViewHolder(val container: View) : RecyclerView.ViewHolder(
    container
) {
    @JvmField
    val titleView: TextView = itemView.findViewById(R.id.sticker_pack_title)

    @JvmField
    val publisherView: TextView = itemView.findViewById(R.id.sticker_pack_publisher)

    @JvmField
    val filesizeView: TextView = itemView.findViewById(R.id.sticker_pack_filesize)

    @JvmField
    val addButton: ImageView = itemView.findViewById(R.id.add_button_on_list)

    @JvmField
    val animatedStickerPackIndicator: ImageView = itemView.findViewById(R.id.sticker_pack_animation_indicator)

    @JvmField
    val imageRowView: LinearLayout = itemView.findViewById(R.id.sticker_packs_list_item_image_list)

}