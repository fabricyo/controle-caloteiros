package me.fabricyo.quemdeve.feature.listadevedor.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import me.fabricyo.quemdeve.R
import me.fabricyo.quemdeve.bases.BaseActivity
import me.fabricyo.quemdeve.databinding.ActivityMainBinding
import me.fabricyo.quemdeve.databinding.InputDebtDialogBinding
import me.fabricyo.quemdeve.feature.devedor.view.DevedorActivity
import me.fabricyo.quemdeve.feature.devedordetails.view.DevedorDetailsActivity
import me.fabricyo.quemdeve.feature.listadevedor.adapter.DevedorAdapter
import me.fabricyo.quemdeve.data.Devedor
import me.fabricyo.quemdeve.feature.listadevedor.repository.ListaDevedoresRepository
import me.fabricyo.quemdeve.feature.listadevedor.viewmodel.ListaDevedoresViewModel
import me.fabricyo.quemdeve.helpers.HelperDB
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ListaDeContatosActivity : BaseActivity() {

    private var adapter: DevedorAdapter? = null

    var viewModel : ListaDevedoresViewModel? = null

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if(viewModel == null) viewModel = ListaDevedoresViewModel(
            ListaDevedoresRepository(
                HelperDB(this)
            )
        )
        setupListView()
        setupOnClicks()
        window.setBackgroundDrawableResource(R.drawable.minimal_material)
    }

    override fun onResume() {
        super.onResume()
        onClickBuscar()
    }

    private fun setupListView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun onClickItemRecyclerView(index: Int) {
        val intent = Intent(this, DevedorDetailsActivity::class.java)
        intent.putExtra("index", index)
        startActivity(intent)
    }

    private fun setupOnClicks() {
        binding.fab.setOnClickListener { onClickAdd() }
        binding.etBuscar.addTextChangedListener{
            onClickBuscar()
        }
    }

    private fun onClickBuscar() {
        val busca = binding.etBuscar.text.toString()
        binding.progressCircular.visibility = View.VISIBLE
        viewModel?.getListaDevedores(
            busca,
            onSucesso = { list ->
                runOnUiThread {
                    adapter = DevedorAdapter(this, list)
                    adapter!!.onClick = {
                        onClickItemRecyclerView(it)
                    }
                    adapter!!.addDebt ={
                        showdialog(it)
                    }
                    binding.recyclerView.adapter = adapter
                    binding.progressCircular.visibility = View.GONE
                    if(busca != "")
                        Toast.makeText(this,"Buscando por $busca", Toast.LENGTH_SHORT).show()
                }
            },
            onError = { exception ->
                runOnUiThread {
                    AlertDialog.Builder(this)
                        .setTitle("Atenção")
                        .setMessage("Não foi possível recuperar os dados")
                        .setPositiveButton("Ok") {alert, i ->
                            alert.dismiss()
                        }
                }
            }
        )
    }

    private fun onClickAdd() {
        val intent = Intent(this, DevedorActivity::class.java)
        startActivity(intent)

    }


    private fun showdialog(devedor: Devedor) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Adicionar débito")
        val inputBinding = InputDebtDialogBinding.inflate(layoutInflater)
        builder.setView(inputBinding.root)

        // Set up the buttons
        builder.setPositiveButton("OK") { dialog, _ ->
            if (inputBinding.tilDebtDialog.editText?.text.toString().isNotEmpty()) {
                try {
                    val valor = inputBinding.tilDebtDialog.editText?.text.toString().toInt()
                    val now = LocalDateTime.now()
                    val nowFormatted = now.format(DateTimeFormatter.ofPattern("dd/MM HH:mm"))
                    devedor.lastMod = nowFormatted
                    devedor.lastValue = valor
                    devedor.value = devedor.value.plus(valor)
                    viewModel?.updateDevedor(
                        devedor,
                        onSucesso = {
                            Snackbar.make(binding.fab, "Débito modificado", Snackbar.LENGTH_LONG).show()
                            onClickBuscar()
                        },
                        onError = {
                            showError("Não foi possível alterar o devedor")
                        }
                    )
                } catch (e: Exception) {
                    Snackbar.make(binding.fab, "Ocorreu um erro", Snackbar.LENGTH_LONG).show()
                } finally {
                    dialog.dismiss()
                }
            }
        }
        builder.setNegativeButton("Cancelar") { dialog, _ -> dialog.cancel() }
        val dialog: AlertDialog = builder.create()
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        dialog.show()
        inputBinding.tilDebtDialog.requestFocusFromTouch()
    }

}