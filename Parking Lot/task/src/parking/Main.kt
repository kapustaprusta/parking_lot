package parking

data class Car(
    val plate: String,
    val color: String,
    var spot: Int = -1,
)

class CarHolder {
    private var spotsByColorIndex = mutableMapOf<String, MutableList<Int>>()
    private var spotsByPlateIndex = mutableMapOf<String, MutableList<Int>>()
    private var platesByColorIndex = mutableMapOf<String, MutableList<String>>()

    fun addCar(car: Car) {
        val carColor = car.color.lowercase()
        val carPlate = car.plate.lowercase()

        if (!spotsByColorIndex.containsKey(carColor)) {
            spotsByColorIndex[carColor] = mutableListOf(car.spot)
        } else {
            spotsByColorIndex[carColor]?.add(car.spot)
        }

        if (!spotsByPlateIndex.containsKey(carPlate)) {
            spotsByPlateIndex[carPlate] = mutableListOf(car.spot)
        } else {
            spotsByPlateIndex[carPlate]?.add(car.spot)
        }

        if (!platesByColorIndex.containsKey(carColor)) {
            platesByColorIndex[carColor] = mutableListOf(car.plate)
        } else {
            platesByColorIndex[carColor]?.add(car.plate)
        }
    }

    fun removeCar(car: Car) {
        val carColor = car.color.lowercase()
        val carPlate = car.plate.lowercase()

        if (spotsByColorIndex.containsKey(carColor)) {
            spotsByColorIndex[carColor]?.remove(car.spot)
        }

        if (spotsByPlateIndex.containsKey(carPlate)) {
            spotsByPlateIndex[carPlate]?.remove(car.spot)
        }

        if (platesByColorIndex.containsKey(carColor)) {
            platesByColorIndex[carColor]?.remove(car.plate)
        }
    }

    fun getSpotsByColor(color: String): List<Int> {
        return spotsByColorIndex[color] ?: listOf()
    }

    fun getSpotsByPlate(plate: String): List<Int> {
        return spotsByPlateIndex[plate] ?: listOf()
    }

    fun getPlatesByColor(color: String): List<String> {
        return platesByColorIndex[color] ?: listOf()
    }
}

class Parking(size: Int) {
    private val carHolder = CarHolder()
    private val spots = MutableList<Car?>(size){ null }

    init {
        println("Created a parking lot with $size spots.")
    }

    fun park(car: Car){
        for (idx in spots.indices) {
            if (spots[idx] == null) {
                spots[idx] = car
                spots[idx]!!.spot = idx + 1
                carHolder.addCar(spots[idx]!!)

                println("${spots[idx]!!.color} car parked in spot ${spots[idx]!!.spot}.")
                return
            }
        }

        println("Sorry, the parking lot is full.")
    }

    fun leave(spot: Int) {
        if (spots[spot-1] == null) {
            println("There is no car in spot $spot.")
            return
        }

        carHolder.removeCar(spots[spot-1]!!)
        spots[spot-1] = null
        println("Spot $spot is free.")
    }

    fun printSpotsByColor(color: String) {
        val spots = carHolder.getSpotsByColor(color.lowercase())
        if (spots.isEmpty()) {
            println("No cars with color $color were found.")
            return
        }

        println(spots.joinToString())
    }

    fun printPlatesByColor(color: String) {
        val plates = carHolder.getPlatesByColor(color.lowercase())
        if (plates.isEmpty()) {
            println("No cars with color $color were found.")
            return
        }

        println(plates.joinToString())
    }

    fun printSpotsByPlate(plate: String) {
        val spots = carHolder.getSpotsByPlate(plate.lowercase())
        if (spots.isEmpty()) {
            println("No cars with registration number $plate were found.")
            return
        }

        println(spots.joinToString())
    }

    fun status() {
        var isEmpty = true
        for (spot in spots) {
            if (spot != null) {
                isEmpty = false
                println("${spot.spot} ${spot.plate} ${spot.color}")
            }
        }

        if (isEmpty) {
            println("Parking lot is empty.")
        }
    }
}

fun main() {
    var parking: Parking? = null
    whileLoop@ while (true) {
        val userInput = readln()
        val splittedUserInput = userInput.split(" ")
        when (splittedUserInput[0]) {
            "create" -> {
                val parkingSize = splittedUserInput[1].toInt()
                parking = Parking(parkingSize)
            }
            "status" -> {
                if (parking == null) {
                    println("Sorry, a parking lot has not been created.")
                    continue
                }

                parking.status()
            }
            "park" -> {
                if (parking == null) {
                    println("Sorry, a parking lot has not been created.")
                    continue
                }

                val carPlate = splittedUserInput[1]
                val carColor = splittedUserInput[2]

                parking.park(Car(carPlate, carColor))
            }
            "leave" -> {
                if (parking == null) {
                    println("Sorry, a parking lot has not been created.")
                    continue
                }

                val parkingSpot = splittedUserInput[1].toInt()

                parking.leave(parkingSpot)
            }
            "reg_by_color" -> {
                if (parking == null) {
                    println("Sorry, a parking lot has not been created.")
                    continue
                }

                val carColor = splittedUserInput[1]

                parking.printPlatesByColor(carColor)
            }
            "spot_by_color" -> {
                if (parking == null) {
                    println("Sorry, a parking lot has not been created.")
                    continue
                }

                val carColor = splittedUserInput[1]

                parking.printSpotsByColor(carColor)
            }
            "spot_by_reg" -> {
                if (parking == null) {
                    println("Sorry, a parking lot has not been created.")
                    continue
                }

                val carPlate = splittedUserInput[1]

                parking.printSpotsByPlate(carPlate)
            }
            "exit" -> {
                break
            }
        }
    }

}
