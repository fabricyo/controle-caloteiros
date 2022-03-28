package me.fabricyo.quemdeve.feature.devedor.viewmodel

import me.fabricyo.quemdeve.feature.devedor.repository.DevedorRepository
import me.fabricyo.quemdeve.data.Devedor
import me.fabricyo.quemdeve.helpers.HelperDB
import java.lang.Exception

class DevedorViewModel(
    helperDB: HelperDB? = null,
    var repository: DevedorRepository? = DevedorRepository(helperDB)
) {

    fun getDevedor(
        idDevedor: Int,
        onSucesso: ((Devedor) -> Unit),
        onError: ((Exception) -> Unit)
        ){
        Thread(Runnable {
            repository?.getDevedor(
                idDevedor,
                onSucesso = { devedor ->
                    if(devedor == null) onError(Exception("Não foi possível completar a sua solicitação"))
                    else onSucesso(devedor)
                }, onError = {
                    onError(it)
                }
            )
        }).start()
    }

    fun saveDevedor(
        devedor: Devedor,
        isUpdate: Boolean,
        onSucesso: (() -> Unit),
        onError: ((Exception) -> Unit)
    ){
        Thread(Runnable{
            if(isUpdate){
                repository?.updateDevedor(
                    devedor,
                    onSucesso = {
                        onSucesso()
                    }, onError = {
                        onError(it)
                    }
                )
            }else{
                repository?.saveDeveodor(
                    devedor,
                    onSucesso = {
                        onSucesso()
                    }, onError = {
                        onError(it)
                    }
                )
            }
        }).start()
    }

    fun deleteDevedor(
        idDevedor: Int,
        onSucesso: () -> Unit,
        onError: (Exception) -> Unit
    ){
        Thread( Runnable {
            repository?.deleteDevedor(
                idDevedor,
                onSucesso = {
                    onSucesso()
                }, onError ={
                    onError(it)
                }
            )
        }).start()
    }
}