package me.fabricyo.quemdeve.feature.devedordetails.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import me.fabricyo.quemdeve.bases.BaseActivity
import me.fabricyo.quemdeve.databinding.ActivityDevedorDetailsBinding
import me.fabricyo.quemdeve.feature.devedor.view.DevedorActivity
import me.fabricyo.quemdeve.feature.devedordetails.viewmodel.DevedorDetailsViewModel
import me.fabricyo.quemdeve.feature.listadevedor.view.ListaDeContatosActivity
import me.fabricyo.quemdeve.helpers.HelperDB
import me.fabricyo.quemdeve.helpers.ImageUtil

class DevedorDetailsActivity : BaseActivity() {

    private var idDevedor: Int = -1
    var viewModel: DevedorDetailsViewModel? = null

    private val binding by lazy { ActivityDevedorDetailsBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel = DevedorDetailsViewModel(
            helperDB = HelperDB(this)
        )
        setupButtons()
    }

    override fun onResume() {
        super.onResume()
        setupDevedor()
        ImageUtil.lookAndErasePrints(this)
    }

    private fun setupButtons() {
        binding.btnClose.setOnClickListener { finish() }
        binding.btnShare.setOnClickListener {
            ImageUtil.share(this, binding.mcvContent)
        }
        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, DevedorActivity::class.java)
            intent.putExtra("index", idDevedor)
            startActivity(intent)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupDevedor() {
        idDevedor = intent.getIntExtra("index", -1 )
        if(idDevedor == -1){
            val intent = Intent(this, ListaDeContatosActivity::class.java)
            startActivity(intent)
        }
        viewModel?.getDevedor(
            idDevedor,
            onSucesso = { devedor ->
                runOnUiThread {
                    binding.tvName.setText(devedor.name)
                    binding.tvValue.setText("R$ ${devedor.value}")
                    binding.tvLastValor.setText("R$ ${devedor.lastValue}")
                    binding.tvLastUpdate.setText(devedor.lastMod)
                }
            },
            onError = {
                showError("Não foi possível completar a sua solicitação")
            }
        )
    }
}