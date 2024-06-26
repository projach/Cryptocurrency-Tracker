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

    private val _favourites = MutableLiveData<List<UserEntity>>()
    val favourites: LiveData<List<UserEntity>> get() = _favourites

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    init {
        loadFavourites()
    }

    private fun loadFavourites() {
        viewModelScope.launch(Dispatchers.IO) {
            _favourites.postValue(userDao.favourites())
        }
    }

    fun selectCoin(coin: UserEntity) {
//        viewModelScope.launch(Dispatchers.IO) {
            _selectedCoin.value = coin
       // }
    }

    fun shareCoin(coin: UserEntity) {
//        viewModelScope.launch(Dispatchers.IO) {
            _selectedCoin.value = coin
            // TODO: delete later
        }

    }

    fun addToFavourites(coin: UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.save(coin.copy(favourite = true))
            loadFavourites()
            _toastMessage.postValue("${coin.name} added to favourites!")
        }
    }

    fun removeFromFavourites(coin: UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.save(coin.copy(favourite = false))
            loadFavourites()
            _toastMessage.postValue("${coin.name} removed from favourites!")
        }
    }

    fun addedToFavourites(coinId: Int): Boolean {
        return _favourites.value?.any { it.id == coinId } ?: false
    }
}