package com.geeklabs.spiffyshow.ui.components.feedback

import com.geeklabs.spiffyshow.ui.base.BaseContract

interface FeedbackContract {
    interface View : BaseContract.View {
        fun initUI()
        fun showToast(message: String)
        fun navigateToHome()
    }
    interface Presenter : BaseContract.Presenter<View> {
        fun onSubmitClicked(feedbackText: String, rating: Float)
    }
}