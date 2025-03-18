package com.example.galactilist.ui.planetDetailView

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.galactilist.R
import com.example.galactilist.constants.Constants.Companion.IMAGE_URL_BANNER
import com.example.galactilist.constants.Constants.Companion.PLANET_DETAILS
import com.example.galactilist.databinding.FragmentPlanetDetailViewBinding
import com.example.galactilist.network.responses.Planet
import com.example.galactilist.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlanetDetailViewFragment : Fragment() {
    private var planetDetails: Planet? = null
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
        planetDetails =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arguments?.getParcelable(PLANET_DETAILS, Planet::class.java)
            } else {
                arguments?.getParcelable(PLANET_DETAILS)
            }
        setupToolbar()
        initViews()
    }

    private fun setupToolbar() {
        (activity as? MainActivity)?.binding?.toolbarLayout?.apply {
            toolbarTitle.text = getString(R.string.planet_details)
            toolbarBackButton.visibility = View.VISIBLE
            toolbarBackButton.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun initViews() {
        Glide.with(binding.root).load(IMAGE_URL_BANNER).into(binding.ivBanner)
        binding.apply {
            tvPlanetName.text = planetDetails?.name
            tvOrbitalPeriod.text = planetDetails?.orbitalPeriod
            tvGravity.text = planetDetails?.gravity
        }
    }
}