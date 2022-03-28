package me.fabricyo.quemdeve.feature.devedor.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import me.fabricyo.quemdeve.data.Devedor
import me.fabricyo.quemdeve.bases.BaseActivity
import me.fabricyo.quemdeve.databinding.ActivityDevedorBinding
import me.fabricyo.quemdeve.feature.devedor.viewmodel.DevedorViewModel
import me.fabricyo.quemdeve.feature.listadevedor.view.ListaDeContatosActivity
import me.fabricyo.quemdeve.helpers.HelperDB
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DevedorActivity : BaseActivity() {

    private var idDevedor: Int = -1
    var viewModel: DevedorViewModel? = null

    private val binding by lazy { ActivityDevedorBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel = DevedorViewModel(
            helperDB = HelperDB(this)
        )
        setupDevedor()
        binding.btnConfirm.setOnClickListener { onClickSalvarDevedor() }
        binding.btnDelete.setOnClickListener { onClickExcluirDevedor() }
        binding.btnClose.setOnClickListener { finish() }
    }

    @SuppressLint("SetTextI18n")
    private fun setupDevedor() {
        idDevedor = intent.getIntExtra("index", -1 )
        if(idDevedor == -1){
            binding.tvTitle.text = "Adicionar caloteiro"
            return
        }
        binding.tvTitle.text = "Editar caloteiro"
        binding.btnConfirm.text = "Editar"
        binding.btnDelete.visibility = View.VISIBLE
        viewModel?.getDevedor(
            idDevedor,
            onSucesso = { devedor ->
                runOnUiThread {
                    binding.tilName.editText?.setText(devedor.name)
                    binding.tilValue.editText?.setText(devedor.value.toString())
                }
            }, onError = {
                showError("Não foi possível completar a sua solicitação")
            }
        )
    }

    private fun onClickSalvarDevedor() {
        val nome = binding.tilName.editText?.text.toString()
        var value = binding.tilValue.editText?.text.toString()
        value = value.replace(",", ".")
        val now = LocalDateTime.now()
        val nowFormatted = now.format(DateTimeFormatter.ofPattern("dd/MM HH:mm"))
        val devedor = Devedor(
            id = idDevedor,
            name = nome,
            value = value.toInt(),
            lastValue = value.toInt(),
            lastMod = nowFormatted
        )
        val isUpdate = idDevedor != -1
        viewModel?.saveDevedor(
            devedor,
            isUpdate,
            onSucesso = {
                runOnUiThread{finish()}
            }, onError = { showError("Não foi possível salvar o devedor") }
        )
    }

    fun onClickExcluirDevedor(){
        if(idDevedor > -1){
            viewModel?.deleteDevedor(
                idDevedor,
                onSucesso = {
                    runOnUiThread {
                        val intent = Intent(this, ListaDeContatosActivity::class.java)
                        startActivity(intent)
                    }
                }, onError = {showError("Não foi possível excluir")}
            )
        }
    }



}