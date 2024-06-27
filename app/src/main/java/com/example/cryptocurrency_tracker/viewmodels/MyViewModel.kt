package com.example.cryptocurrency_tracker.viewmodels

import android.app.Application
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.AndroidViewModel
import com.example.cryptocurrency_tracker.database.UserDao
import com.example.cryptocurrency_tracker.database.UserEntity
import com.example.cryptocurrency_tracker.database.DatabaseInstance

class MyViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao: UserDao = DatabaseInstance.getDatabase(application).getUserDao()

    private val _selectedCoin = MutableLiveData<UserEntity>()
    val selectedCoin: LiveData<UserEntity> get() = _selectedCoin

    private val _favourites = MutableLiveData<Set<UserEntity>>()
    val favourites: LiveData<Set<UserEntity>> get() = _favourites

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    init {
        loadFavourites()
    }

    private fun loadFavourites() {
        viewModelScope.launch(Dispatchers.IO) {
            val favouriteItems = userDao.favourites()
            _favourites.postValue(favouriteItems.toSet())
        }
    }

    fun selectCoin(coin: UserEntity) {
            _selectedCoin.value = coin
    }

    fun handleFavourite(coin: UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            val existingCoin = userDao.findCoinBySymbol(coin.symbol)
            if (existingCoin == null || !existingCoin.favourite) {
                userDao.save(coin.copy(favourite = true))
                _toastMessage.postValue("${coin.name} added to favourites!")
            } else {
                userDao.save(coin.copy(favourite = false))
                _toastMessage.postValue("${coin.name} removed from favourites!")
            }
            loadFavourites()
        }
    }

    fun addedToFavourites(coinSymbol: String): Boolean {
        return _favourites.value?.any { it.symbol == coinSymbol } ?: false
    }
}
