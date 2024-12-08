package robi.saputra.nutapos.ui.portrait

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import robi.saputra.nutapos.R
import robi.saputra.nutapos.databinding.ItemPortraidUangMasukBinding
import robi.saputra.nutapos.models.FinanceIn
import robi.saputra.nutapos.utils.dateTimeToTime
import robi.saputra.nutapos.utils.formatToRupiah
import robi.saputra.nutapos.utils.millisecondsToDateTime

class FinanceAdapter: RecyclerView.Adapter<FinanceAdapter.ViewHolder>() {
    private var transactionData: List<FinanceIn> = emptyList()
    lateinit var adapterCallback: AdapterCallBack

    interface AdapterCallBack {
        fun onEdit(result: FinanceIn)
        fun onDelete(result: FinanceIn)
        fun onViewImage(result: FinanceIn)
    }

    fun setTransactionData(data: List<FinanceIn>) {
        transactionData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPortraidUangMasukBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = transactionData[position]
        holder.bind(data, position)
        holder.binding.btnEdit.setOnClickListener {
            adapterCallback.onEdit(data)
        }
        holder.binding.btnDelete.setOnClickListener {
            adapterCallback.onDelete(data)
        }
        holder.binding.btnLihatFoto.setOnClickListener {
            adapterCallback.onViewImage(data)
        }
    }

    override fun getItemCount(): Int = transactionData.size

    class ViewHolder(val binding: ItemPortraidUangMasukBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(finance: FinanceIn, position: Int) {
            binding.tvTime.text = finance.date.millisecondsToDateTime().dateTimeToTime("dd-MM-yyyy HH:mm:ss")
            binding.tvAmount.text = finance.amount.formatToRupiah()
            binding.tvStatus.text = "Dari ${finance.insertFrom} ke ${finance.insertTo}"
            binding.tvDesc.text = finance.desc
            if (finance.image.isNotEmpty()) binding.btnLihatFoto.visibility = View.VISIBLE
        }
    }
}