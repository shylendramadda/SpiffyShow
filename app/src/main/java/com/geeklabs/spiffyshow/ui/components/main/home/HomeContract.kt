package com.geeklabs.spiffyshow.ui.components.main.home

import com.geeklabs.spiffyshow.data.local.models.item.Trim
import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.models.FileMetaData
import com.geeklabs.spiffyshow.ui.base.BaseContract

interface HomeContract {
    interface View : BaseContract.View {
        fun initUI()
        fun setState(progress: Boolean = false, empty: Boolean = false, error: Boolean = false)
        fun showItems(
            items: MutableList<Trim>,
            user: User?
        )
        fun showToast(title: String)
        fun showMessage(message: Int)
        fun showAlertDialog(message: Int)
        fun navigateToTrim(item: Trim)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun onSearch(query: String)
        fun setFileMetaData(fileMetaData: FileMetaData)
        fun onEditClicked(item: Trim)
        fun onDeleteClicked(item: Trim)
    }
}