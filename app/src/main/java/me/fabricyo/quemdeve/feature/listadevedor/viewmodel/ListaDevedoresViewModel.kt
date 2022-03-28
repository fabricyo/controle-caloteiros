package me.fabricyo.quemdeve.feature.listadevedor.viewmodel

import me.fabricyo.quemdeve.data.Devedor
import me.fabricyo.quemdeve.feature.listadevedor.repository.ListaDevedoresRepository

class ListaDevedoresViewModel(
    var repository: ListaDevedoresRepository? = null
) {
    fun getListaDevedores(
        busca: String,
        onSucesso: ((List<Devedor>) -> Unit),
        onError: ((Exception) -> Unit)
    ) {
        Thread(Runnable {
            repository?.requestListaDevedores(
                busca,
                onSucesso = { lista ->
                    // Aqui seria a tratativa de filtro por usuário
                    onSucesso(lista)
                },
                onError = { ex ->
                    onError(ex)
                }
            )
        }).start()
    }

    fun updateDevedor(
        devedor: Devedor,
        onSucesso: (() -> Unit),
        onError: ((Exception) -> Unit)
    ) {
        Thread(Runnable {
            repository?.updateDevedor(
                devedor,
                onSucesso = {
                    onSucesso()
                }, onError = {
                    onError(it)
                }
            )
        }).start()
    }
}