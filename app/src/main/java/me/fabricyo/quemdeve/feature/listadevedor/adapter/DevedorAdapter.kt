package me.fabricyo.quemdeve.feature.listadevedor.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.fabricyo.quemdeve.databinding.ItemDevedorBinding
import me.fabricyo.quemdeve.data.Devedor

class DevedorAdapter(
    private val context: Context,
    private val lista: List<Devedor>,

    ) : RecyclerView.Adapter<DevedorAdapter.DevedorViewHolder>(){
    var onClick: (Int) -> Unit = {}
    var addDebt: (Devedor) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DevedorViewHolder {
//        val view = LayoutInflater.from(context).inflate(R.layout.item_devedor, parent, false)
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDevedorBinding.inflate(inflater, parent, false )
        return DevedorViewHolder(binding)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: DevedorViewHolder, position: Int) {
        holder.bind(lista[position])
    }


    inner class DevedorViewHolder(private val binding: ItemDevedorBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(devedor: Devedor) {
            binding.tvName.text = devedor.name
            binding.tvValue.text = "R$ ${devedor.value.toString().replace(".",",")}"
            binding.tvLastValue.text =
                "R$ ${devedor.lastValue.toString().replace(".",",")}"
            binding.tvLastMod.text = devedor.lastMod
            binding.mcvContent.setOnClickListener{onClick(devedor.id)}
            binding.btnAddDebt.setOnClickListener { addDebt(devedor) }
        }
    }
}
