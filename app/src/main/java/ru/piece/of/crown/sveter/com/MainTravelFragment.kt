package ru.piece.of.crown.sveter.com

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class MainTravelFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_travel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.iAmPassengerButton).setOnClickListener {
            startChoosingPathLike("passenger")
        }

        view.findViewById<Button>(R.id.iAmDriverButton).setOnClickListener {
            startChoosingPathLike("driver")
        }
    }

    private fun startChoosingPathLike(role: String) {
        startActivity(Intent(requireActivity(), ChoosingPathActivity::class.java).apply {
            putExtra("role", role)
        })
    }

    companion object {
        const val ITEM_INDEX: Int = 1
    }
}