package com.example.galactilist.ui.planetDetailView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.galactilist.constants.Constants.Companion.IMAGE_URL_BANNER
import com.example.galactilist.databinding.FragmentPlanetDetailViewBinding
import com.example.galactilist.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlanetDetailViewFragment : Fragment() {
    private lateinit var binding: FragmentPlanetDetailViewBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlanetDetailViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        initViews()
    }

    private fun setupToolbar() {
        val mainActivity = activity as? MainActivity
        mainActivity?.binding?.toolbarLayout?.apply {
            toolbarTitle.text = "Planet Details"
            toolbarBackButton.visibility = View.VISIBLE
            toolbarBackButton.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun initViews() {
        // load banner image
        Glide.with(binding.root)
            .load(IMAGE_URL_BANNER)
            .into(binding.ivBanner)

        // data for planet details
        binding.tvPlanetName.text = "Name: Earth"
        binding.tvOrbitalPeriod.text = "orbital period: 365"
        binding.tvGravity.text = "gravity: 9.8"

    }
    companion object {
        const val TAG = "PlanetDetailViewFragment"
    }
}