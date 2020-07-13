package com.geeklabs.spiffyshow.ui.components.feedback

import com.geeklabs.spiffyshow.R
import com.geeklabs.spiffyshow.enums.Navigation
import com.geeklabs.spiffyshow.extensions.toast
import com.geeklabs.spiffyshow.ui.base.BaseFragment
import com.geeklabs.spiffyshow.ui.components.main.MainActivity
import kotlinx.android.synthetic.main.fragment_feedback.*
import javax.inject.Inject

class FeedbackFragment : BaseFragment<FeedbackContract.View, FeedbackContract.Presenter>(),
    FeedbackContract.View {

    @Inject
    lateinit var feedbackPresenter: FeedbackPresenter

    override fun initUI() {
        submitButton.setOnClickListener {
            val feedbackText = feedbackET.text.toString()
            val rating = ratingBar.rating
            presenter?.onSubmitClicked(feedbackText, rating)
        }
    }

    override fun navigateToHome() {
        (activity as MainActivity).navigateToScreen(Navigation.HOME)
    }

    override fun showToast(message: String) {
        activity?.toast(message)
    }

    override fun initPresenter() = feedbackPresenter

    override fun injectDependencies() = getApplicationComponent().inject(this)

    override fun getLayoutResId() = R.layout.fragment_feedback
}