package com.geeklabs.spiffyshow.ui.components.main.original

import com.geeklabs.spiffyshow.data.local.models.item.Item
import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.models.FileMetaData
import com.geeklabs.spiffyshow.ui.base.BaseContract

interface OriginalContract {
    interface View : BaseContract.View {
        fun initUI()
        fun setState(progress: Boolean = false, empty: Boolean = false, error: Boolean = false)
        fun showItems(
            items: MutableList<Item>,
            user: User?
        )
        fun showToast(title: String)
        fun navigateToTrim(item: Item)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun onSearch(query: String)
        fun setFileMetaData(fileMetaData: FileMetaData)
        fun onEditClicked(item: Item)
        fun onDeleteClicked(item: Item)
    }
}