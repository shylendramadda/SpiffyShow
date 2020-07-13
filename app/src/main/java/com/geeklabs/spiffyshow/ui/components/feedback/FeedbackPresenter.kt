package com.geeklabs.spiffyshow.ui.components.feedback

import com.geeklabs.spiffyshow.ui.base.BasePresenter
import javax.inject.Inject

class FeedbackPresenter @Inject constructor() : BasePresenter<FeedbackContract.View>(),
    FeedbackContract.Presenter {

    override fun onCreated() {
        super.onCreated()
        getView()?.initUI()
    }

    override fun onSubmitClicked(feedbackText: String, rating: Float) {
        if (rating <= 0) {
            getView()?.showToast("Please give rating")
        } else if (feedbackText.isEmpty()) {
            getView()?.showToast("Please write your feedback")
        } else {
            getView()?.showToast("Feedback submitted")
            getView()?.navigateToHome() // TODO API
        }
    }
}