package com.ekochkov.santehnikatestapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ekochkov.santehnikatestapp.App
import com.ekochkov.santehnikatestapp.data.entity.toPointAdress
import com.ekochkov.santehnikatestapp.utils.Constants.ADDRESS_NOT_SET
import com.ekochkov.santehnikatestapp.utils.Constants.ERROR_RESPONSE
import com.ekochkov.santehnikatestapp.utils.YandexRetrofitInterface
import kotlinx.coroutines.*
import javax.inject.Inject

class HomeFragmentViewModel: ViewModel() {

    val pointAddressLiveData = MutableLiveData<String>()
    private lateinit var scope: CoroutineScope
    private val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
        throwable.printStackTrace()
        pointAddressLiveData.postValue(ERROR_RESPONSE)
    }

    @Inject
    lateinit var retrofit: YandexRetrofitInterface

    init {
        App.instance.dagger.inject(this)
        pointAddressLiveData.postValue(ADDRESS_NOT_SET)
    }

    fun getAddress(geocode: String) {
        scope = CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).also { scope ->
            scope.launch {
                val response = retrofit.getAdressByCoords(geocode)
                val pointAddress = response?.toPointAdress()
                if (pointAddress!=null) {
                    val address = "${pointAddress.adress}\n(${pointAddress.latLon})"
                    pointAddressLiveData.postValue(address)
                } else {
                    pointAddressLiveData.postValue(ADDRESS_NOT_SET)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}