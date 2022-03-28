package me.fabricyo.quemdeve.feature.devedordetails.viewmodel

import me.fabricyo.quemdeve.feature.devedordetails.repository.DevedorDetailsRepository
import me.fabricyo.quemdeve.data.Devedor
import me.fabricyo.quemdeve.helpers.HelperDB
import java.lang.Exception

class DevedorDetailsViewModel(
    helperDB: HelperDB? = null,
    private var repository: DevedorDetailsRepository? =  DevedorDetailsRepository(helperDB)
){
    fun getDevedor(
        idDevedor: Int,
        onSucesso: ((Devedor) -> Unit),
        onError: ((Exception) -> Unit)
    ){
        Thread(Runnable {
            repository?.getDevedor(
                idDevedor,
                onSucesso = { devedor ->
                    onSucesso(devedor)
                }, onError = {
                    onError(it)
                }
            )
        }).start()
    }
}