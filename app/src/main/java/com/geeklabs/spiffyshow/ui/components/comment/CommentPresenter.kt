package com.geeklabs.spiffyshow.ui.components.comment

import com.geeklabs.spiffyshow.data.local.models.item.Comment
import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.models.ApplicationState
import com.geeklabs.spiffyshow.ui.base.BasePresenter
import com.geeklabs.spiffyshow.utils.Utils
import javax.inject.Inject

class CommentPresenter @Inject constructor(
    private val applicationState: ApplicationState
) : BasePresenter<CommentContract.View>(),
    CommentContract.Presenter {

    private var user: User? = null
    val list = mutableListOf<Comment>()

    override fun onCreated() {
        super.onCreated()
        getView()?.initUI()
        this.user = applicationState.user
        loadComments()
    }

    private fun loadComments() {
        for (i in 1..5) {
            val comment = Comment(
                id = i.toLong(),
                text = "Test Comment $i",
                userName = user?.name,
                imageUrl = user?.imageUrl,
                time = Utils.getCurrentTime()
            )
            list.add(comment)
        }
        list.sortByDescending { it.time }
        getView()?.showItems(list)
    }

    override fun onPostCommentClicked(text: String) {
        val comment = Comment(
            text = text,
            userName = user?.name,
            imageUrl = user?.imageUrl,
            time = Utils.getCurrentTime()
        )
        list.add(0, comment)
        getView()?.notifyAdapter()
    }
}