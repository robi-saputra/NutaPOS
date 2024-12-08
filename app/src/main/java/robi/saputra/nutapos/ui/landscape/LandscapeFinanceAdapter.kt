package robi.saputra.nutapos.ui.landscape

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import robi.saputra.nutapos.R
import robi.saputra.nutapos.databinding.ItemLandscapeUangMasukBinding
import robi.saputra.nutapos.models.FinanceIn
import robi.saputra.nutapos.utils.dateTimeToTime
import robi.saputra.nutapos.utils.formatToRupiah
import robi.saputra.nutapos.utils.millisecondsToDateTime
import kotlin.coroutines.coroutineContext

class LandscapeFinanceAdapter: RecyclerView.Adapter<LandscapeFinanceAdapter.ViewHolder>() {
    private var transactionData: List<FinanceIn> = emptyList()

    fun setTransactionData(data: List<FinanceIn>) {
        transactionData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLandscapeUangMasukBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(transactionData[position], position)
    }

    override fun getItemCount(): Int = transactionData.size

    class ViewHolder(private val binding: ItemLandscapeUangMasukBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(finance: FinanceIn, position: Int) {
            binding.tv1.text = finance.date.millisecondsToDateTime().dateTimeToTime("dd-MM-yyyy HH:mm:ss")
            binding.tv2.text = finance.insertTo
            binding.tv3.text = finance.insertFrom
            binding.tv4.text = finance.desc
            binding.tv5.text = finance.amount.formatToRupiah()

            if (position.mod(2)!=0) binding.root.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.broken_white))
        }
    }
}