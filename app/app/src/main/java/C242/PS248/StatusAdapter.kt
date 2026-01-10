package C242.PS248

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class StatusAdapter(private val statusList: List<FoodRecommendationResponse>) :
    RecyclerView.Adapter<StatusAdapter.StatusViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_status, parent, false)
        return StatusViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        val statusItem = statusList[position]

        holder.bmi.text = "BMI: ${statusItem.bmi}"
        holder.description.text = statusItem.description
        holder.prediction.text = statusItem.prediction

        when (statusItem.prediction) {
            "Gizi Baik" -> holder.prediction.setBackgroundColor(
                ContextCompat.getColor(
                    holder.prediction.context,
                    android.R.color.holo_green_light
                )
            )
            "Gizi Kurang" -> holder.prediction.setBackgroundColor(
                ContextCompat.getColor(
                    holder.prediction.context,
                    android.R.color.holo_red_light
                )
            )
            "Gizi Lebih" -> holder.prediction.setBackgroundColor(
                ContextCompat.getColor(
                    holder.prediction.context,
                    android.R.color.holo_red_dark
                )
            )
        }
    }

    override fun getItemCount() = statusList.size

    class StatusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bmi: TextView = itemView.findViewById(R.id.index_masa_tubuh)
        val description: TextView = itemView.findViewById(R.id.desc)
        val prediction: TextView = itemView.findViewById(R.id.status_gizi)
    }
}
