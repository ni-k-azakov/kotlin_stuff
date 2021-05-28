package com.example.hydrateme.waterfall

class WaterInfo(val storage: DataStorage) {
    fun addWater(amount: Short) {
        storage.addWater(amount)
    }
    fun getCurrentWater(): Short {
        return storage.getCurrentWater()
    }
}