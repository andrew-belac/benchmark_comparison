package actions

import (
	"buffalo_bench/services"
	"github.com/gobuffalo/buffalo"
	"net/http"
)

// HomeHandler is a default handler to serve up
// a home page.
func CreateHandler(c buffalo.Context) error {
	orderDto := services.Create()
	return c.Render(http.StatusOK, r.JSON(orderDto))
}
