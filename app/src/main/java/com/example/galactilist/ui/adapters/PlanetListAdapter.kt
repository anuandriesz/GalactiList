import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.galactilist.constants.Constants.Companion.IMAGE_URL_LOGO
import com.example.galactilist.databinding.ListItemPlanetBinding
import com.example.galactilist.network.responses.Planet

class PlanetListAdapter(private val onPlanetClick: (Planet) -> Unit) : ListAdapter<Planet, PlanetListAdapter.PlanetViewHolder>(PlanetDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanetViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemPlanetBinding.inflate(inflater, parent, false)
        return PlanetViewHolder(binding, onPlanetClick)
    }

    override fun onBindViewHolder(holder: PlanetViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PlanetViewHolder(private val binding: ListItemPlanetBinding,
                           private val onPlanetClick: (Planet) -> Unit
        ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(planet: Planet) {
            binding.tvName.text = planet.name
            binding.tvClimate.text = "Climate: ${planet.climate}"
            Glide.with(binding.root)
                .load(IMAGE_URL_LOGO)
                .into(binding.ivImage)

            binding.root.setOnClickListener {
                onPlanetClick(planet)
            }
        }
    }

    class PlanetDiffCallback : DiffUtil.ItemCallback<Planet>() {
        override fun areItemsTheSame(oldItem: Planet, newItem: Planet): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Planet, newItem: Planet): Boolean {
            return oldItem == newItem
        }
    }
}
