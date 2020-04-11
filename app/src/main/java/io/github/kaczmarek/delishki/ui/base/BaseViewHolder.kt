package io.github.kaczmarek.delishki.ui.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    open fun bind() {}
}