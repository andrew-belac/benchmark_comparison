package services

import (
	"buffalo_bench/dtos"
	"buffalo_bench/models"
	"github.com/gobuffalo/envy"
	"github.com/gobuffalo/pop"
	"math/rand"
)

var Divisions = [2]string{"DURBAN", "CAPETOWN"}

const alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

func Create() dtos.OrderDto {
	db, err := pop.Connect(envy.Get("GO_ENV", "development"))
	if err != nil {
		panic(err)
	}

	// Create a new User model instance
	order := &models.Order{
		OrderCode: generateRandomString(20),
		Division:  getRandomDivision(),
	}
	err = db.Create(order)
	if err != nil {
		panic(err)
	}

	return mapOrder(*order)

}

func GetRandomOrders() []dtos.OrderDto {
	db, err := pop.Connect(envy.Get("GO_ENV", "development"))
	if err != nil {
		panic(err)
	}

	division := getRandomDivision()
	//get the count of the orders within a division partition
	query := db.Q()
	count, err := query.Where("division = ?", division).Count(&models.Order{})
	if err != nil {
		panic(err)
	}

	num := rand.Intn(10) + 1
	offset := rand.Intn(count) + 1
	var orders []models.Order

	err = db.RawQuery("SELECT * FROM orders WHERE division = ? LIMIT ? OFFSET ?", division, num, offset).All(&orders)
	if err != nil {
		panic(err)
	}

	orderDtos := make([]dtos.OrderDto, len(orders))
	for i, p := range orders {
		orderDtos[i] = mapOrder(p)
	}
	return orderDtos
}

func GetOne() dtos.OrderDto {

	order := models.Order{}
	err := models.DB.Find(&order, rand.Intn(999999))
	if err != nil {
		panic(err)
	}
	return mapOrder(order)
}

func generateRandomString(length int) string {
	b := make([]byte, length)
	for i := range b {
		b[i] = alphabet[rand.Intn(len(alphabet))]
	}
	return string(b)
}

func mapOrder(order models.Order) dtos.OrderDto {
	out := &dtos.OrderDto{
		Id:        order.ID,
		OrderCode: order.OrderCode,
		Division:  order.Division,
	}
	return *out
}

func getRandomDivision() string {
	return Divisions[rand.Intn(1)]
}
