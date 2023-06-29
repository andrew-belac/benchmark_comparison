package actions

import (
	"buffalo_bench/services"
	"github.com/gobuffalo/buffalo"
	"net/http"
)

func GetRandomHandler(c buffalo.Context) error {
	return c.Render(http.StatusOK, r.JSON(services.GetRandomOrders()))
}
