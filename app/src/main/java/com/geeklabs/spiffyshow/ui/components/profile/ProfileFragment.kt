package com.geeklabs.spiffyshow.ui.components.profile

import android.annotation.SuppressLint
import com.bumptech.glide.Glide
import com.geeklabs.spiffyshow.R
import com.geeklabs.spiffyshow.data.local.models.item.Original
import com.geeklabs.spiffyshow.data.local.models.item.Trim
import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.extensions.setEmptyStateView
import com.geeklabs.spiffyshow.extensions.shouldShow
import com.geeklabs.spiffyshow.extensions.toast
import com.geeklabs.spiffyshow.extensions.visible
import com.geeklabs.spiffyshow.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.view_state_layout.*
import kotlinx.android.synthetic.main.view_state_layout.view.*
import javax.inject.Inject

class ProfileFragment : BaseFragment<ProfileContract.View, ProfileContract.Presenter>(),
    ProfileContract.View {

    @Inject
    lateinit var profilePresenter: ProfilePresenter
    private lateinit var trimAdapter: UserTrimAdapter
    private lateinit var originalAdapter: UserOriginalAdapter
    private var user: User? = null

    override fun initUI() {
        if (user == null) return
        setUserInfo(user!!)
        followText.setOnClickListener {
            followText.text = getString(R.string.following)
        }
        trimAdapter = UserTrimAdapter()
        originalAdapter = UserOriginalAdapter()
        recyclerViewTrims.adapter = trimAdapter
        trimAdapter.setEmptyStateView(stateLayout)
        recyclerViewOriginals.adapter = originalAdapter
        originalAdapter.setEmptyStateView(stateLayout)
    }

    @SuppressLint("SetTextI18n")
    private fun setUserInfo(user: User) {
        nameTV.text = user.name
        bioTV.text = user.bio
        followersTV.text = "101"
        trimsTV.text = "51"
        originalsTV.text = "201"

        if (user.imageUrl?.isNotEmpty() == true) {
            Glide.with(context!!).load(user.imageUrl).placeholder(R.drawable.ic_icon_user)
                .dontAnimate()
                .into(userImage)
            userImage.shouldShow = true
            userImageText.shouldShow = false
        } else if (user.name?.isNotEmpty() == true) {
            userImageText.text = user.name?.substring(0, 1)
            userImage.shouldShow = false
            userImageText.shouldShow = true
        }
    }

    override fun setState(progress: Boolean, empty: Boolean, error: Boolean) {
        stateLayout.stateProgress.visible = progress
        stateLayout.progressText.visible = progress
        stateLayout.stateEmpty.visible = empty
        stateLayout.stateError.visible = error
        stateLayout.stateEmpty.emptyTitle.text = getString(R.string.no_items)
        stateLayout.stateEmpty.emptyDescription.visible = false
    }

    override fun showTrimItems(
        items: MutableList<Trim>,
        user: User?
    ) {
        trimAdapter.user = user
        trimAdapter.items = items
        trimAdapter.notifyDataSetChanged()
        val isMoreThanOne = items.size > 1
        if (isMoreThanOne) recyclerViewTrims.smoothScrollToPosition(0)
    }

    override fun showOriginalItems(
        originals: MutableList<Original>,
        user: User?
    ) {
        originalAdapter.user = user
        originalAdapter.items = originals
        originalAdapter.notifyDataSetChanged()
        val isMoreThanOne = originals.size > 1
        if (isMoreThanOne) recyclerViewOriginals.smoothScrollToPosition(0)
    }

    override fun showToast(message: String) {
        activity?.toast(message)
    }

    override fun initPresenter() = profilePresenter

    override fun injectDependencies() = getApplicationComponent().inject(this)

    override fun getLayoutResId() = R.layout.fragment_profile

    companion object {
        fun newInstance(user: User): ProfileFragment {
            return ProfileFragment().apply {
                this.user = user
            }
        }
    }
}