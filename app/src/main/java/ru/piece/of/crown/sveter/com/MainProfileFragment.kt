package ru.piece.of.crown.sveter.com

import UserData
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import startActivityWithHorizontalSlideAnimation

class MainProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<LinearLayout>(R.id.logoutButton).setOnClickListener {
            requireActivity().apply {
                UserData.logOut(this)
                this.startActivityWithHorizontalSlideAnimation(
                    SelectLoginOrRegistrationActivity::class.java,
                    false
                )
                finish()
            }
        }
    }

    companion object {
        const val ITEM_INDEX: Int = 3
    }
}