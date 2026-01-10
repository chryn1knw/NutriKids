package C242.PS248

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FoodAdapter(private val foodList: List<FoodItem>) :
    RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_food, parent, false)
        return FoodViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val foodItem = foodList[position]
        holder.foodName.text = foodItem.food
        holder.caloricValue.text = "Caloric Value: ${foodItem.caloricValue} kcal"
        holder.protein.text = "Protein: ${foodItem.protein} g"
    }

    override fun getItemCount() = foodList.size

    class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodName: TextView = itemView.findViewById(R.id.food_name)
        val caloricValue: TextView = itemView.findViewById(R.id.food_calories)
        val protein: TextView = itemView.findViewById(R.id.food_proteins)
    }
}