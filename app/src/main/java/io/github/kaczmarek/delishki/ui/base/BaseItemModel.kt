package io.github.kaczmarek.delishki.ui.base

import android.view.View
import androidx.annotation.IntDef
import androidx.annotation.StringRes
import io.github.kaczmarek.delishki.R
import kotlinx.android.synthetic.main.rv_empty_list_item.view.*

const val TYPE_EMPTY_LIST = R.layout.rv_empty_list_item

@IntDef(
    TYPE_EMPTY_LIST
)
@Retention(AnnotationRetention.SOURCE)
annotation class ItemViewType

interface ItemBase : DiffComparable {

    @ItemViewType
    val itemViewType: Int
}

data class ItemEmptyList(
    @StringRes val messageId: Int,
    override val itemViewType: Int = TYPE_EMPTY_LIST
) : ItemBase {

    override fun getItemId() = hashCode()
}

class EmptyListViewHolder(view: View, private val item: ItemBase) : BaseViewHolder(view) {
    private val tvMessage = itemView.tv_empty_list_message

    override fun bind() {
        val itemEmptyList = item as ItemEmptyList
        tvMessage.setText(itemEmptyList.messageId)
    }
}